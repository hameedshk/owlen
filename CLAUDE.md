# Owlen — Claude Code Context



## What This App Does

Owlen is an offline Android app that monitors environmental audio during

sleep and plays masking sounds reactively when a detected event is likely

to disturb sleep. It does NOT play continuously.



## Stack

- Language: Kotlin only (no Java)

- UI: Jetpack Compose (no XML layouts)

- Architecture: MVVM + Clean Architecture (3 layers)

- DI: Hilt

- ML: TensorFlow Lite (YAMNet + MLP classifier)

- Audio capture: AudioRecord (NOT MediaRecorder)

- Audio playback: AudioTrack (NOT MediaPlayer)

- Settings: DataStore Preferences (NOT SharedPreferences)

- Session log: Flat JSON file (NOT Room DB)

- Concurrency: Kotlin Coroutines + StateFlow



## Layer Structure

- data/audio/     → AudioCapture, AudioPlayer

- data/ml/        → FeatureExtractor, EventDetector

- data/log/       → SessionLogger

- domain/model/   → All data classes and enums

- domain/scorer/  → DisturbanceScorer

- domain/policy/  → PolicyEngine

- presentation/   → ViewModels + Compose screens

- service/        → SleepProtectionService



## Hard Rules — Never Violate

- Never use MediaRecorder or MediaPlayer

- Never use SharedPreferences (use DataStore)

- Never use Room DB (flat JSON only)

- Never use NNAPI delegate for TFLite (CPU only)

- Never add cloud or network calls

- Never use XML layouts (Compose only)

- Never put business logic in ViewModels or Composables

- Never use runBlocking in the audio pipeline

- domain/ classes must have zero Android framework imports



## Threading Model

- Main thread:        Compose UI only

- Dispatchers.IO:     AudioCapture, FeatureExtractor, EventDetector, file I/O

- Dispatchers.Default: DisturbanceScorer, PolicyEngine

- AudioTrack thread:  Managed by AudioTrack internally



## Design Tokens

- background:    #0F1117

- surface:       #1A1D27

- primary:       #F5A623  (amber — CTA, scores, accents)

- green:         #4CAF50  (active protection state)

- safety:        #FF5252  (Baby Cry / Smoke Alarm only)

- textPrimary:   #E8E8E8

- textSecondary: #8A8FA8

- Font: Inter (Google Fonts)

- Min tap target: 48×48dp



## Safety Rules (non-negotiable)

- Smoke Alarm and Baby Cry NEVER trigger masking

- These events immediately STOP any active masking

- Full-screen alert must display even from lock screen



## On-Device Debugging — Read Before Changing UI Code

Test device: Samsung SM-A736B, Android 15, 1080×2400, ADB over Wi-Fi.

- NEVER tap coordinates read off a screenshot — screenshots are downscaled
  900×2000 from a 1080×2400 panel (0.833×). Always take coordinates from
  `uiautomator dump` bounds. If you must use an image, multiply by 1.20.

- Taps that silently do nothing low on the screen while the big centre button
  still works = scaled coordinates, NOT a touch bug. The error grows with the
  coordinate, so bottom-nav targets miss first.

- Before blaming the app, run `adb shell dumpsys input`. If the app window has
  `inputConfig=0x0`, a full `touchableRegion`, and `status=NORMAL,
  responsive=true`, injection works and your coordinates are wrong.

- `[Gesture Monitor] secinputdev` and `PalmMotion` are normal Samsung SPY
  monitors. They do NOT block input.

- Compose puts the click handler on a parent View; the label is a
  `clickable="false"` leaf. Walk up to the nearest `clickable="true"` ancestor.

- Verify every tap by asserting the screen content actually changed.

Full procedure + reusable helper: docs/On_Device_Debugging.md



## Reference Documents

- BRD v2: docs/BRD_v2.docx

- Architecture: docs/Architecture_v1.md

- UX Spec: docs/UX_Spec_v2.md

- Test Plan: docs/Test_Plan_v1.md

- On-Device Debugging: docs/On_Device_Debugging.md