---
name: service-agent
description: Implements SleepProtectionService and SessionLogger for Owlen.
             Wires the full pipeline from AudioCapture to AudioPlayer.
             Use only after ml-agent, audio-agent, and domain-agent are complete.
tools: Read, Write, Edit, Bash
model: sonnet
---

# Owlen Service Agent

You wire the complete Owlen pipeline into a foreground service and implement
session logging. Your scope is `service/` and `data/log/` directories only.

## Your Responsibilities
- `service/SleepProtectionService.kt` — foreground service, full pipeline orchestration
- `data/log/SessionLogger.kt` — flat JSON session log, 7-day retention
- Unit/integration tests for SessionLogger

## Read First — In This Order
Before writing a single line, read ALL of these:
1. `CLAUDE.md` — project rules and stack
2. `data/audio/AudioCapture.kt` — understand the API you are wiring
3. `data/audio/AudioPlayer.kt` — understand the API you are wiring
4. `data/ml/FeatureExtractor.kt` — understand the API you are wiring
5. `data/ml/EventDetector.kt` — understand the API you are wiring
6. `domain/scorer/DisturbanceScorer.kt` — understand inputs/outputs
7. `domain/policy/PolicyEngine.kt` — understand inputs/outputs
8. `domain/model/` — all models

Do not assume APIs. Read the actual files. If a file does not exist yet,
stop and report which dependency is missing before proceeding.

## SleepProtectionService

### Android Manifest Requirements
The service must be declared with:
```xml
<service
    android:name=".service.SleepProtectionService"
    android:foregroundServiceType="microphone"
    android:exported="false" />
```

### Required Permissions in Manifest
```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_MICROPHONE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.USE_FULL_SCREEN_INTENT" />
```

### Service Lifecycle
```
startForeground() → acquire WakeLock → start pipeline coroutine
stopSelf() → release WakeLock → cancel pipeline coroutine → log interruption
```

WakeLock type: `PowerManager.PARTIAL_WAKE_LOCK` — keeps CPU awake, screen can off.

### Foreground Notification
- Channel ID: `owlen_protection`
- Channel name: "Sleep Protection"
- Importance: `IMPORTANCE_LOW` (no sound, no heads-up)
- Notification text: "Sleep Protection is active. Tap to view."
- Tap → opens `ActiveModeScreen`
- Must be shown before any long-running work starts

### Pipeline Coroutine
Run on `Dispatchers.IO`. Implement as a single coroutine collecting from `AudioCapture.audioFlow`:

```
AudioCapture.audioFlow
  .collect { pcmWindow ->
      if (detectorGateActive) return@collect        // FR-3.4
      val embedding = featureExtractor.extract(pcmWindow)
      val event = eventDetector.detect(embedding)
      val dBSPL = calculateRMS(pcmWindow)
      val result = disturbanceScorer.score(event, dBSPL, currentHour(), settings)
      val action = policyEngine.evaluate(result, event, System.currentTimeMillis(), settings, isMaskingActive, lowScoreDurationMs)
      handleAction(action, event)
      sessionLogger.logEvent(event, result, action)
      publishState(action)                           // StateFlow for UI
  }
```

### Detector Gate (FR-3.4)
```kotlin
var detectorGateActive = false
var detectorGateJob: Job? = null

fun activateDetectorGate() {
    detectorGateActive = true
    detectorGateJob?.cancel()
    detectorGateJob = serviceScope.launch {
        delay(5000)
        detectorGateActive = false
    }
}
```

Call `activateDetectorGate()` immediately when masking starts.

### handleAction()
```kotlin
when (action) {
    is PolicyAction.Ignore -> { /* no-op */ }
    is PolicyAction.StartMasking -> {
        audioPlayer.startMasking(action.sound, action.volume)
        activateDetectorGate()
        isMaskingActive = true
        lowScoreDurationMs = 0
        updateNotification("Masking started — ${action.sound.displayName}")
    }
    is PolicyAction.SetVolume -> audioPlayer.setVolume(action.volume)
    is PolicyAction.StopMasking -> {
        audioPlayer.stopMasking(immediate = false)
        isMaskingActive = false
    }
    is PolicyAction.SafetyAlert -> {
        audioPlayer.stopMasking(immediate = true)
        isMaskingActive = false
        activateDetectorGate()
        broadcastSafetyAlert(action.event)           // triggers full-screen intent
        sessionLogger.logSafetyEvent(action.event)
    }
}
```

### Safety Alert Broadcast
Use `USE_FULL_SCREEN_INTENT` notification to wake the screen:
```kotlin
val fullScreenIntent = Intent(this, ActiveModeActivity::class.java)
    .putExtra("SAFETY_EVENT", action.event.name)
val fullScreenPendingIntent = PendingIntent.getActivity(...)

NotificationCompat.Builder(this, SAFETY_CHANNEL_ID)
    .setFullScreenIntent(fullScreenPendingIntent, true)
    .setPriority(NotificationCompat.PRIORITY_HIGH)
    ...
```

### Service Interruption Logging (FR-7.4)
On `onDestroy()`:
```kotlin
if (wasStartedByUser && !stoppedByUser) {
    sessionLogger.logInterruption(
        startTime = serviceStartTime,
        endTime = System.currentTimeMillis(),
        reason = "Service destroyed by system"
    )
}
```

On next `onCreate()`, check for incomplete session and surface to UI via StateFlow.

### StateFlow for UI
Expose a shared `StateFlow<ServiceState>` via a singleton repository:
```kotlin
sealed class ServiceState {
    object Idle : ServiceState()
    object Monitoring : ServiceState()
    data class MaskingActive(val sound: MaskingSound, val volume: Float) : ServiceState()
    data class SafetyAlertActive(val event: EventClass) : ServiceState()
    data class Interrupted(val at: Long) : ServiceState()
}
```

ViewModels observe this StateFlow. They do not control the pipeline directly.

### Threading
```
Pipeline coroutine:  Dispatchers.IO
Policy evaluation:   Dispatchers.Default (launch child coroutine)
AudioPlayer fades:   Dispatchers.Default (managed by AudioPlayer)
StateFlow updates:   Dispatchers.Main (use withContext)
WakeLock:            Main thread only (acquire/release in onCreate/onDestroy)
```

Never put policy evaluation on the same dispatcher as audio capture.

## SessionLogger

### Storage
Flat JSON file per session: `{filesDir}/sessions/session_{timestamp}.json`
No Room DB. No SharedPreferences.

### 7-Day Retention
On each new session start, delete all session files older than 7 days.

### Session JSON Structure
```json
{
  "sessionId": "uuid",
  "startTime": 1234567890,
  "endTime": 1234567890,
  "events": [
    {
      "timestampMs": 1234567890,
      "eventClass": "GARBAGE_COLLECTION",
      "confidence": 0.94,
      "disturbanceScore": 88,
      "action": "MASKED",
      "maskingSound": "BROWN_NOISE",
      "maskingVolume": 0.6,
      "durationMs": 140000
    }
  ],
  "interruptions": [
    {
      "startTime": 1234567890,
      "endTime": 1234567890,
      "reason": "Service destroyed by system"
    }
  ],
  "totalMaskingDurationMs": 1080000,
  "safetyEvents": []
}
```

### Public API
```kotlin
fun startSession()
fun endSession()
fun logEvent(event: DetectedEvent, result: DisturbanceResult, action: PolicyAction)
fun logSafetyEvent(eventClass: EventClass)
fun logInterruption(startTime: Long, endTime: Long, reason: String)
suspend fun getRecentSessions(limit: Int = 7): List<SessionSummary>
suspend fun getSessionDetails(sessionId: String): SessionDetail?
```

All file I/O runs on `Dispatchers.IO`. Never block the main thread.

## Unit Tests Required

SessionLogger:
- Session file created on `startSession()`
- Event logged correctly with all fields
- Session files older than 7 days deleted on new session start
- `getRecentSessions()` returns sessions sorted newest first
- Interruption logged when endTime > startTime

Use JUnit4. Use a temporary directory for file I/O in tests — not the real device.

## After Every File
Run `./gradlew assembleDebug` and fix all errors.
Run `./gradlew test` after SessionLogger and fix all failures.
For SleepProtectionService: manually verify on device that:
- Foreground notification appears on start
- Service survives screen off for 10 minutes
- Interruption logged on force-stop
