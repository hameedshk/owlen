# Owlen — Claude Code Context



## What This App Does

Owlen is an offline Android app that monitors environmental audio during

sleep and plays masking sounds reactively when a detected event is likely

to disturb sleep. It does NOT play continuously.

See docs/FEATURES.md for the full current feature list (kept in sync with source, not just the original spec docs).



## Stack

- Language: Kotlin only (no Java)

- UI: Jetpack Compose (no XML layouts)

- Architecture: MVVM + Clean Architecture (3 layers)

- DI: Hilt

- ML: TensorFlow Lite — single bundled model `yamnet.tflite` (CPU only, 2 threads). No MLP classifier: `EventDetector` is a pure Kotlin max-score mapping from YAMNet's 521 AudioSet classes down to Owlen's 24 `EventClass` values. The same inference call's 1024-dim embedding output feeds custom sound matching (see below).

- Audio capture: AudioRecord (NOT MediaRecorder) — 16kHz mono, 15600-sample (0.975s) windows

- Audio playback: AudioTrack (NOT MediaPlayer) — all masking sounds are procedurally synthesized on-device (no bundled audio files)

- Settings: DataStore Preferences (NOT SharedPreferences)

- Session log: Flat JSON file (NOT Room DB)

- Concurrency: Kotlin Coroutines + StateFlow



## Layer Structure

- data/audio/      → AudioCapture, AudioPlayer, NoiseGenerator (procedural noise synthesis)

- data/ml/         → FeatureExtractor (YAMNet TFLite), EventDetector (score→class mapping), RmsCalculator

- data/log/        → SessionLogger (flat JSON session files)

- data/settings/   → SettingsRepository (DataStore)

- data/prototypes/ → SoundPrototypeRepository (custom-enrolled sound storage)

- domain/model/    → All data classes and enums (EventClass, MaskingSound, SleepSettings, DetectedEvent, DisturbanceResult, PolicyAction, Sensitivity, SoundPrototype, SleepTimerConfig, SleepTimerSchedule, ...)

- domain/scorer/   → DisturbanceScorer

- domain/policy/   → PolicyEngine

- domain/matcher/  → CustomSoundMatcher, PrototypeBuilder, EventArbiter (custom sound enrollment/matching — see below)

- domain/timer/    → SleepTimerCalculator, ScheduleSummary

- presentation/    → ViewModels (calibration/, enrollment/, home/, session/, settings/, sleeptimer/) + Compose screens (ui/) + reusable components (ui/components/) + theme (ui/theme/) + util/

- service/         → SleepProtectionService (capture→inference→score→policy→playback pipeline), ServiceRepository, ServiceState, NotificationHelper, BootReceiver, SleepTimerScheduler, SleepTimerAlarmReceiver

- di/              → AppModule (Hilt)



## Custom Sound Enrollment (built, but not yet reflected in UX_Spec_v2.md)

- Users can enroll personal sounds (e.g. a specific pet or appliance) by recording 3 takes via EnrollmentScreen/EnrollmentViewModel.

- `PrototypeBuilder` averages and L2-normalizes the takes' YAMNet embeddings into a `SoundPrototype`, deriving a per-prototype similarity threshold from how consistent the takes were (requires ≥0.60 min pairwise similarity; threshold clamped to 0.75–0.90).

- `CustomSoundMatcher` matches live embeddings against stored prototypes via cosine similarity each inference cycle.

- `EventArbiter` resolves precedence: safety events (Baby Cry, Smoke Alarm) always win unconditionally; otherwise a custom match overrides the generic YAMNet class.

- Note: this contradicts docs/UX_Spec_v2.md, which lists custom/imported sounds as out of MVP scope — docs/FEATURES.md flags this gap explicitly.



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

- Dispatchers.IO:     AudioCapture, FeatureExtractor, file I/O

- Dispatchers.Default: EventDetector, CustomSoundMatcher, DisturbanceScorer, PolicyEngine

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

Additional surface/accent tokens (SurfaceElevated, SurfaceSunken, Stroke, Warning, etc.) live in presentation/ui/theme/Color.kt.



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

- Lean PRD: docs/Adaptive_Sleep_Protector_Lean_PRD_v1.md

- BRD v2: docs/BRD_v2.docx

- Architecture: docs/Architecture_v1.md

- UX Spec: docs/UX_Spec_v2.md

- Test Plan: docs/Test_Plan_v1.md

- Manual Test Guide: docs/Manual_Test_Guide.md

- Feature reference (source-of-truth, read before assuming spec docs are current): docs/FEATURES.md

- On-Device Debugging: docs/On_Device_Debugging.md
