# Owlen — MVP Architecture
**Version 1.0 · Android · Kotlin · Offline**

---

## 1. Architecture Style

**MVVM + Clean Architecture** with a unidirectional data flow.

Chosen because:
- MVVM is the Android-recommended pattern with full Jetpack support
- Clean Architecture separates the AI pipeline (domain layer) from Android framework concerns (data + presentation layers)
- Foreground service lifecycle is isolated from UI — screen off does not interrupt processing
- Testable in isolation: model inference, scorer, and policy engine can be unit tested without an Android device

---

## 2. Layer Overview

```
┌─────────────────────────────────────────────────────┐
│                  PRESENTATION LAYER                  │
│         (Jetpack Compose UI + ViewModels)            │
├─────────────────────────────────────────────────────┤
│                    DOMAIN LAYER                      │
│     (Use Cases · Scorer · Policy Engine · Models)    │
├─────────────────────────────────────────────────────┤
│                     DATA LAYER                       │
│   (Audio Pipeline · TFLite Inference · Session Log)  │
├─────────────────────────────────────────────────────┤
│                  ANDROID SERVICES                    │
│        (SleepProtectionService · AudioPlayer)        │
└─────────────────────────────────────────────────────┘
```

---

## 3. Component Architecture

```
Microphone (AudioRecord)
        │
        ▼
┌───────────────────┐
│  AudioCapture     │  Captures 16kHz PCM, 960-sample frames (60ms)
│  (Repository)     │  Runs on dedicated IO coroutine
└───────────────────┘
        │
        ▼
┌───────────────────┐
│ FeatureExtractor  │  YAMNet TFLite → 1024-dim embedding per 0.96s window
│ (Data Layer)      │  Batches 16 frames before inference
└───────────────────┘
        │
        ▼
┌───────────────────┐
│  EventDetector    │  MLP classifier → Event label + Confidence (0.0–1.0)
│  (Data Layer)     │  10 classes; Unknown = fallback
└───────────────────┘
        │
        ▼
┌───────────────────┐
│ DisturbanceScorer │  Weighted formula → Score (0–100)
│ (Domain Layer)    │  Inputs: EventWeight + SoundLevel + TimeWeight
└───────────────────┘
        │
        ▼
┌───────────────────┐
│  PolicyEngine     │  Score → Action (Ignore / Start / Increase / Stop)
│  (Domain Layer)   │  Enforces safety rules unconditionally
└───────────────────┘
        │
        ▼
┌───────────────────┐
│   AudioPlayer     │  AudioTrack looping PCM (Brown/Pink/White Noise)
│  (Data Layer)     │  Fade in/out · Volume cap · Detector gate signal
└───────────────────┘
        │
        ▼
┌───────────────────┐
│   SessionLogger   │  Logs events, masking episodes, interruptions
│  (Data Layer)     │  Flat file · 7-day retention · Room DB optional
└───────────────────┘
```

---

## 4. Android Service Architecture

The entire AI pipeline runs inside a **Foreground Service** — independent of any Activity lifecycle.

```
┌──────────────────────────────────────────────────────┐
│              SleepProtectionService                   │
│            (Foreground Service · Kotlin)              │
│                                                       │
│  ┌─────────────────────────────────────────────┐     │
│  │           Coroutine Scope (IO)              │     │
│  │  AudioCapture → FeatureExtractor →          │     │
│  │  EventDetector → DisturbanceScorer →        │     │
│  │  PolicyEngine → AudioPlayer                 │     │
│  └─────────────────────────────────────────────┘     │
│                                                       │
│  WakeLock: PARTIAL_WAKE_LOCK (screen off safe)        │
│  Notification: persistent · tap → ActiveModeScreen    │
│  Declaration: FOREGROUND_SERVICE_TYPE_MICROPHONE      │
└──────────────────────────────────────────────────────┘
        │                          │
        ▼                          ▼
  SessionLogger              AudioPlayer
  (local file)            (AudioTrack PCM)
```

**Service lifecycle rules:**
- Started by user toggle on Home Screen
- Stopped by emergency disable or morning dismiss
- On unexpected kill: logs interruption timestamp on next start
- Must be declared in `AndroidManifest.xml` with `FOREGROUND_SERVICE_TYPE_MICROPHONE`

---

## 5. UI Architecture (Jetpack Compose + MVVM)

```
┌─────────────────────────────────────────────────────┐
│                    UI Screens                        │
│  HomeScreen · OnboardingScreen · CalibrationScreen  │
│  SettingsScreen · ActiveModeScreen · SummaryScreen  │
└──────────────────┬──────────────────────────────────┘
                   │ observes StateFlow
                   ▼
┌─────────────────────────────────────────────────────┐
│                   ViewModels                         │
│  HomeViewModel · SettingsViewModel · SessionViewModel│
└──────────────────┬──────────────────────────────────┘
                   │ calls
                   ▼
┌─────────────────────────────────────────────────────┐
│                   Use Cases                          │
│  StartSleepProtection · StopSleepProtection         │
│  GetSessionSummary · SaveSettings · RunCalibration  │
└──────────────────┬──────────────────────────────────┘
                   │ binds to
                   ▼
┌─────────────────────────────────────────────────────┐
│            SleepProtectionService                    │
│         (via ServiceConnection / BoundService)       │
└─────────────────────────────────────────────────────┘
```

**State management:**
- `StateFlow` from ViewModel to Compose UI
- Service publishes pipeline state (monitoring / masking / interrupted) via a shared `StateFlow` in a singleton repository
- UI observes service state — does not control the pipeline directly

---

## 6. AI Pipeline Detail

### 6.1 Audio Capture

```kotlin
// AudioRecord configuration
AudioRecord(
    MediaRecorder.AudioSource.MIC,
    sampleRate = 16000,         // 16kHz required by YAMNet
    channelConfig = CHANNEL_IN_MONO,
    audioFormat = ENCODING_PCM_16BIT,
    bufferSize = 1920           // 120ms buffer (2 × 60ms frames)
)
```

- Reads 960-sample frames (60ms) continuously
- Batches 16 frames → 0.96s window before passing to YAMNet
- Runs on `Dispatchers.IO` coroutine

### 6.2 Feature Extraction (YAMNet)

```
Model:   YAMNet (TFLite, ~3.7 MB, Apache 2.0)
Input:   Float32 array [15360] — 0.96s at 16kHz
Output:  Float32 array [1024] — embedding vector
Latency: ≤ 100ms on Snapdragon 680
```

- Model loaded once at service start; held in memory
- Inference runs every 1 second (not on every frame)
- TFLite Interpreter with 2 threads; NNAPI delegate optional (test per device)

### 6.3 Event Detector (MLP Classifier)

```
Architecture: 3-layer MLP
Input:        1024-dim YAMNet embedding
Hidden:       256 units (ReLU) → 64 units (ReLU)
Output:       10 units (Softmax) → class probabilities
Model size:   < 500 KB
Latency:      < 20ms
```

**Output mapping:**

| Index | Class |
|---|---|
| 0 | Garbage Collection |
| 1 | Human Shouting |
| 2 | Motorcycle |
| 3 | Dog Barking |
| 4 | Construction |
| 5 | Rain |
| 6 | Thunder |
| 7 | Baby Cry |
| 8 | Smoke Alarm |
| 9 | Unknown |

- Predicted class = argmax of output probabilities
- Confidence = probability of predicted class
- If max probability < 0.4 → classify as Unknown regardless of argmax

### 6.4 Disturbance Scorer

```
Score = (EventWeight × 0.5) + (NormSoundLevel × 0.3) + (TimeWeight × 0.2)

NormSoundLevel = clamp((dBSPL - floor) / (90 - floor) × 100, 0, 100)
floor          = calibrated ambient baseline (default 50 dB)

TimeWeight     = 100 if current time within Sleep Window
               =  30 if outside Sleep Window

Rain override  = if Rain Behaviour toggle ON → Score forced to 0
```

### 6.5 Policy Engine

```
if SafetyEvent (Smoke Alarm | Baby Cry) AND confidence ≥ 0.70:
    → STOP masking immediately
    → suppress engine for 60s
    → trigger full-screen alert

else if Score > (81 - sensitivityOffset):  → START masking at max volume
else if Score > (61 - sensitivityOffset):  → START masking at medium volume
else if Score > (31 - sensitivityOffset):  → START masking at low volume
else:                                       → IGNORE

if masking active AND score < 20 for Auto Stop Duration:
    → STOP masking (fade out)

if Wake Time within 90s:
    → STOP masking (fade out)
    → suspend engine for 120s after Wake Time

sensitivityOffset = +15 (High) | 0 (Medium) | -15 (Low)
```

### 6.6 Detector Gate

```
On masking START:
    → suppress EventDetector output for 5 seconds
    → AudioCapture continues (buffer does not drop)
    → gate lifts automatically at T+5s
```

---

## 7. Data Storage

| Data | Storage | Retention |
|---|---|---|
| User settings | `SharedPreferences` (DataStore) | Persistent |
| Session log (events, episodes, interruptions) | Flat JSON file per session | 7 days |
| Calibration baseline | In-memory (per session only) | Session lifetime |
| YAMNet model | `assets/` folder in APK | Persistent |
| MLP classifier | `assets/` folder in APK | Persistent |

No database required for MVP. Migrate to Room if session history features are added post-MVP.

---

## 8. Permissions

| Permission | Purpose |
|---|---|
| `RECORD_AUDIO` | Microphone access for audio capture |
| `FOREGROUND_SERVICE` | Run pipeline while screen is off |
| `FOREGROUND_SERVICE_MICROPHONE` | Android 14+ foreground service type |
| `WAKE_LOCK` | Prevent CPU sleep during monitoring |
| `RECEIVE_BOOT_COMPLETED` | Optional: auto-restart service after reboot |
| `USE_FULL_SCREEN_INTENT` | Safety Event alert bypasses lock screen |
| `POST_NOTIFICATIONS` | Android 13+ notification permission |

---

## 9. Key Technology Choices

| Concern | Choice | Reason |
|---|---|---|
| Language | Kotlin | Team primary language |
| UI | Jetpack Compose | Modern Android UI; no XML layouts |
| Architecture | MVVM + Clean | Testable; Jetpack native |
| Audio capture | `AudioRecord` | Low-level PCM; lower latency than `MediaRecorder` |
| Audio playback | `AudioTrack` | Low-level PCM loop; minimal battery overhead |
| ML inference | TensorFlow Lite | Offline; Android-optimised; YAMNet available as TFLite |
| Concurrency | Kotlin Coroutines | Native Kotlin; `Dispatchers.IO` for pipeline |
| DI | Hilt | Jetpack-native dependency injection |
| Settings storage | DataStore (Preferences) | Replaces `SharedPreferences`; coroutine-safe |
| Session log | Flat JSON file | Simplest viable for 7-day retention; no DB overhead |

---

## 10. Threading Model

```
Main Thread        →  Compose UI recomposition only
Dispatchers.IO     →  AudioCapture · FeatureExtractor · EventDetector
Dispatchers.Default→  DisturbanceScorer · PolicyEngine
Dispatchers.Main   →  StateFlow updates → UI observation
AudioTrack thread  →  Dedicated audio playback thread (AudioTrack internal)
```

All pipeline components communicate via Kotlin `Channel` or `StateFlow`. No shared mutable state between threads.

---

## 11. Out of Scope for MVP Architecture

- NNAPI / GPU delegate for TFLite (validate per device post-MVP)
- Model update mechanism (OTA model refresh)
- Encrypted session log storage
- Multi-device sync
- Background model retraining
- Widget or notification controls
