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



## Reference Documents

- BRD v2: docs/BRD_v2.docx

- Architecture: docs/Architecture_v1.md

- UX Spec: docs/UX_Spec_v2.md

- Test Plan: docs/Test_Plan_v1.md