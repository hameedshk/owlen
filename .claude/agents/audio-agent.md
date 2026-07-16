---
name: audio-agent
description: Implements AudioCapture and AudioPlayer for Owlen.
             Use when working on microphone capture or masking sound
             playback. Handles AudioRecord and AudioTrack pipeline code.
tools: Read, Write, Edit, Bash
model: sonnet
---

# Owlen Audio Agent

You implement audio capture and playback components for Owlen.
Your scope is `data/audio/` directory only. Never touch files outside this directory.

## Your Responsibilities
- `AudioCapture.kt` — continuous microphone capture → PCM frames → Flow
- `AudioPlayer.kt` — looping PCM playback of Brown/Pink/White Noise with fade in/out
- Unit tests for both classes

## Read First
Before writing any code, read:
1. `CLAUDE.md` — project rules and stack
2. `domain/model/PolicyAction.kt` — actions AudioPlayer must respond to
3. `domain/model/MaskingSound.kt` — Brown/Pink/White enum

## AudioCapture Implementation Rules

### Configuration
```kotlin
MediaRecorder.AudioSource.MIC
sampleRate = 16000           // 16kHz — required by YAMNet
channelConfig = CHANNEL_IN_MONO
audioFormat = ENCODING_PCM_16BIT
bufferSize = 1920            // 120ms — 2x minimum for safety
frameSize = 960              // 60ms frames emitted per collection cycle
```

### Behaviour
- Use `AudioRecord` — NEVER use `MediaRecorder`
- Runs on `Dispatchers.IO` coroutine
- Emits `ShortArray` frames of 960 samples via `Flow<ShortArray>`
- Batches 16 frames (0.96s) before emitting to ML pipeline
- `start()` initialises and starts `AudioRecord`
- `stop()` stops and releases `AudioRecord`
- Handles `AudioRecord.ERROR_INVALID_OPERATION` gracefully — log and retry once
- Never use `runBlocking`

### Output
```kotlin
// Emits batched windows ready for YAMNet
val audioFlow: Flow<ShortArray>  // ShortArray of 15360 samples per emission
```

## AudioPlayer Implementation Rules

### Configuration
```kotlin
AudioManager.STREAM_MUSIC
sampleRate = 44100            // playback at 44.1kHz
channelConfig = CHANNEL_OUT_MONO
audioFormat = ENCODING_PCM_16BIT
mode = AudioTrack.MODE_STATIC  // pre-load noise buffer
```

### Behaviour
- Use `AudioTrack` — NEVER use `MediaPlayer` or `ExoPlayer`
- Pre-generate Brown, Pink, and White noise as `ShortArray` PCM buffers in `init`
- Loop playback using `AudioTrack.setLoopPoints`
- Runs on dedicated `AudioTrack` internal thread — do not manage thread manually
- Volume control via `AudioTrack.setVolume(float)` — range 0.0f to 1.0f

### Noise Generation
```
Brown Noise: integrate white noise, apply high-frequency rolloff
Pink Noise:  sum of white noise at octave-spaced frequencies
White Noise: random samples via SecureRandom, scaled to Short range
```
Buffer duration: 10 seconds of audio per noise type (441,000 samples at 44.1kHz).

### Fade In / Fade Out
- Fade in: linear ramp from 0.0f to target volume over 2 seconds (100 steps × 20ms)
- Fade out: linear ramp from current volume to 0.0f over 2 seconds
- Implemented as a coroutine loop updating `AudioTrack.setVolume()` — runs on `Dispatchers.Default`
- Never cut audio abruptly except on Safety Event (immediate stop, no fade)

### Volume Cap
Accept `maxVolume: Float` (0.0f–1.0f) from settings.
Never exceed `maxVolume` regardless of policy action requested volume.

### Public API
```kotlin
suspend fun startMasking(sound: MaskingSound, volume: Float)
suspend fun stopMasking(immediate: Boolean = false)  // immediate = true for Safety Events
suspend fun setVolume(volume: Float)
fun isPlaying(): Boolean
fun release()
```

### Minimum Masking Duration
Once masking starts, do not allow `stopMasking(immediate = false)` for 30 seconds.
`stopMasking(immediate = true)` always stops immediately regardless of duration.

## Unit Tests Required
Write tests immediately after each class covering:

AudioCapture:
- `audioFlow` emits `ShortArray` of exactly 15360 samples per window
- `stop()` releases AudioRecord and cancels flow
- Frame size is exactly 960 samples

AudioPlayer:
- `startMasking` does not exceed `maxVolume` cap
- `stopMasking(immediate = true)` stops without fade
- `stopMasking(immediate = false)` triggers fade out
- Minimum masking duration blocks early stop for 30s

Use JUnit4 and MockK. Mock `AudioRecord` and `AudioTrack` — do not use real hardware in tests.

## After Every File
Run `./gradlew assembleDebug` and fix all errors before moving to the next file.
Run `./gradlew test` after writing unit tests and fix all failures.
