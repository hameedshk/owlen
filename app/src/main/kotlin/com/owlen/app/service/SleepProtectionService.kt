package com.owlen.app.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import com.owlen.app.data.audio.AudioCapture
import com.owlen.app.data.audio.AudioPlayer
import com.owlen.app.data.log.SessionLogger
import com.owlen.app.data.ml.EventDetector
import com.owlen.app.data.ml.FeatureExtractor
import com.owlen.app.data.ml.RmsCalculator
import com.owlen.app.data.settings.SettingsRepository
import com.owlen.app.domain.model.DetectedEvent
import com.owlen.app.domain.model.DisturbanceResult
import com.owlen.app.domain.model.PolicyAction
import com.owlen.app.domain.model.SleepSettings
import com.owlen.app.domain.policy.PolicyEngine
import com.owlen.app.domain.scorer.DisturbanceScorer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class SleepProtectionService : Service() {

    @Inject
    lateinit var audioCapture: AudioCapture

    @Inject
    lateinit var audioPlayer: AudioPlayer

    @Inject
    lateinit var featureExtractor: FeatureExtractor

    @Inject
    lateinit var eventDetector: EventDetector

    @Inject
    lateinit var disturbanceScorer: DisturbanceScorer

    @Inject
    lateinit var policyEngine: PolicyEngine

    @Inject
    lateinit var sessionLogger: SessionLogger

    @Inject
    lateinit var serviceRepository: ServiceRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private lateinit var notificationHelper: NotificationHelper

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var pipelineJob: Job? = null
    private lateinit var wakeLock: PowerManager.WakeLock

    // State tracked across inference cycles
    @Volatile
    private var currentSettings: SleepSettings = SleepSettings.default()
    private var isMaskingActive = false
    private var lowScoreStartTimeMs = 0L
    private var maskingStartTimeMs = 0L

    @Volatile
    private var detectorGateActive = false
    private var detectorGateJob: Job? = null
    private var serviceStartedByUser = false
    private var serviceStartTimeMs = 0L
    private var stoppedByUser = false

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannels()
        acquireWakeLock()
        startForeground(NOTIFICATION_ID, notificationHelper.createServiceNotification(this))
        serviceScope.launch {
            // If a previous session file was never closed, the system killed us
            // mid-night (START_STICKY restart). Surface it and fold it into this session.
            val interrupted = sessionLogger.findInterruptedSession()
            sessionLogger.startSession()
            if (interrupted != null) {
                sessionLogger.closeSession(interrupted.sessionId, interrupted.lastActivityMs)
                sessionLogger.logInterruption(
                    startTime = interrupted.lastActivityMs,
                    endTime = System.currentTimeMillis(),
                    reason = "Service restarted by system"
                )
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.notify(
                    INTERRUPTION_NOTIFICATION_ID,
                    notificationHelper.createInterruptionNotification(
                        this@SleepProtectionService, interrupted.lastActivityMs
                    )
                )
            }
        }
        serviceScope.launch {
            settingsRepository.settings.collect { currentSettings = it }
        }
        serviceRepository.updateSessionStartTime(System.currentTimeMillis())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stoppedByUser = true
            stopSelf()
            return START_NOT_STICKY
        }
        startPipeline()
        return START_STICKY
    }

    override fun onDestroy() {
        logInterruptionIfUnexpected()
        pipelineJob?.cancel()
        audioCapture.stop()
        // Immediate silence; keep singletons (interpreter, player) alive for the next session
        audioPlayer.close()
        runBlocking { sessionLogger.endSession() }
        releaseWakeLock()
        // Preserve the Interrupted state set by logInterruptionIfUnexpected so
        // the UI can surface it; only a clean user stop resets to Idle
        if (stoppedByUser || !serviceStartedByUser) {
            serviceRepository.updateState(ServiceState.Idle)
        }
        serviceRepository.updateSessionStartTime(null)
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "owlen:SleepProtectionService"
        ).apply {
            acquire(9 * 60 * 60 * 1000L)  // 9 hours for overnight session
        }
    }

    private fun releaseWakeLock() {
        if (::wakeLock.isInitialized && wakeLock.isHeld) {
            wakeLock.release()
        }
    }

    private fun startPipeline() {
        if (pipelineJob?.isActive == true) return
        serviceStartedByUser = true
        serviceStartTimeMs = System.currentTimeMillis()
        stoppedByUser = false
        audioCapture.start(serviceScope)
        serviceRepository.updateState(ServiceState.Monitoring)

        pipelineJob = serviceScope.launch {
            audioCapture.audioFlow.collect { pcmWindow ->
                // Detector gate — suppress while masking ramps up to prevent feedback loop
                if (detectorGateActive) return@collect

                val settings = currentSettings

                // Run inference on IO dispatcher
                val scores = withContext(Dispatchers.IO) {
                    featureExtractor.extract(pcmWindow)
                }
                val event = eventDetector.detect(scores)

                // Calculate sound level
                val dBSPL = RmsCalculator.calculate(pcmWindow)

                // Score and policy on Default dispatcher
                val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val result = withContext(Dispatchers.Default) {
                    disturbanceScorer.score(event, dBSPL, currentHour, settings)
                }

                // Track low score duration for auto-stop
                val now = System.currentTimeMillis()
                if (result.score < 20) {
                    if (lowScoreStartTimeMs == 0L) lowScoreStartTimeMs = now
                } else {
                    lowScoreStartTimeMs = 0L
                }
                val lowScoreDurationMs = if (lowScoreStartTimeMs > 0L)
                    now - lowScoreStartTimeMs else 0L

                val action = withContext(Dispatchers.Default) {
                    policyEngine.evaluate(
                        result, event, now, settings,
                        isMaskingActive, lowScoreDurationMs
                    )
                }

                serviceRepository.updateLastDetection(event, result)
                handleAction(action, event, result, settings)
            }
        }
    }

    private fun handleAction(
        action: PolicyAction,
        event: DetectedEvent,
        result: DisturbanceResult,
        settings: SleepSettings
    ) {
        serviceScope.launch {
            when (action) {
                is PolicyAction.Ignore -> { /* no-op */ }

                is PolicyAction.StartMasking -> {
                    audioPlayer.startMasking(action.sound, action.volume, settings.maxVolume)
                    activateDetectorGate()
                    isMaskingActive = true
                    maskingStartTimeMs = System.currentTimeMillis()
                    lowScoreStartTimeMs = 0L
                    serviceRepository.updateState(
                        ServiceState.MaskingActive(action.sound, action.volume)
                    )
                    updateNotification(
                        notificationHelper.createMaskingNotification(
                            this@SleepProtectionService, action.sound
                        )
                    )
                    sessionLogger.logEvent(event, result, action)
                }

                is PolicyAction.SetVolume -> {
                    audioPlayer.setVolume(action.volume, settings.maxVolume)
                    serviceRepository.updateState(
                        ServiceState.MaskingActive(settings.preferredSound, action.volume)
                    )
                    sessionLogger.logEvent(event, result, action)
                }

                is PolicyAction.StopMasking -> {
                    val duration = System.currentTimeMillis() - maskingStartTimeMs
                    audioPlayer.stopMasking(immediate = false)
                    if (!audioPlayer.isPlaying()) {
                        isMaskingActive = false
                        serviceRepository.updateState(ServiceState.Monitoring)
                        updateNotification(
                            notificationHelper.createServiceNotification(
                                this@SleepProtectionService
                            )
                        )
                        sessionLogger.logEvent(event, result, action, maskingDurationMs = duration)
                    }
                }

                is PolicyAction.SafetyAlert -> {
                    audioPlayer.stopMasking(immediate = true)
                    isMaskingActive = false
                    activateDetectorGate()
                    sessionLogger.logSafetyEvent(action.event)
                    sessionLogger.logEvent(event, result, action)
                    serviceRepository.updateState(ServiceState.SafetyAlertActive(action.event))
                    showSafetyAlert(action.event)
                }
            }
        }
    }

    private fun activateDetectorGate() {
        detectorGateActive = true
        detectorGateJob?.cancel()
        detectorGateJob = serviceScope.launch {
            delay(5_000L)
            detectorGateActive = false
        }
    }

    private fun showSafetyAlert(event: com.owlen.app.domain.model.EventClass) {
        val notification = notificationHelper.createSafetyNotification(this, event)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(SAFETY_NOTIFICATION_ID, notification)
    }

    private fun updateNotification(notification: Notification) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun logInterruptionIfUnexpected() {
        if (serviceStartedByUser && !stoppedByUser) {
            runBlocking {
                sessionLogger.logInterruption(
                    startTime = serviceStartTimeMs,
                    endTime = System.currentTimeMillis(),
                    reason = "Service destroyed by system"
                )
            }
            serviceRepository.updateState(
                ServiceState.Interrupted(
                    interruptedAt = serviceStartTimeMs,
                    resumedAt = System.currentTimeMillis()
                )
            )
        }
    }

    companion object {
        const val NOTIFICATION_ID = 1001
        const val SAFETY_NOTIFICATION_ID = 1002
        const val INTERRUPTION_NOTIFICATION_ID = 1003
        const val ACTION_STOP = "com.owlen.app.action.STOP_PROTECTION"

        fun start(context: Context) {
            val intent = Intent(context, SleepProtectionService::class.java)
            context.startForegroundService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, SleepProtectionService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
}
