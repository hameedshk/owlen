package com.owlen.app.domain.policy

import com.owlen.app.domain.model.DetectedEvent
import com.owlen.app.domain.model.DisturbanceResult
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.PolicyAction
import com.owlen.app.domain.model.SleepSettings

class PolicyEngine {
    fun evaluate(
        result: DisturbanceResult,
        event: DetectedEvent,
        currentTimeMs: Long,
        settings: SleepSettings,
        isMaskingActive: Boolean,
        lowScoreDurationMs: Long
    ): PolicyAction {
        // 1. Safety bypass always wins
        if (result.isSafetyBypass) {
            return PolicyAction.SafetyAlert(event.eventClass)
        }

        // 2. Wake time check: if within 90 seconds of wake time, wind down
        if (isNearWakeTime(currentTimeMs, settings)) {
            return if (isMaskingActive) PolicyAction.StopMasking else PolicyAction.Ignore
        }

        // 3. Auto stop: if masking active, score < 20, and low score duration exceeded
        if (isMaskingActive && result.score < 20 && lowScoreDurationMs >= settings.autoStopDurationMs) {
            return PolicyAction.StopMasking
        }

        // 4. Score thresholds with sensitivity offset
        val offset = settings.sensitivity.thresholdOffset
        val sound = settings.preferredSound
        val maxVolume = settings.maxVolume

        val targetVolume = when {
            result.score > (80 + offset) -> maxVolume
            result.score > (60 + offset) -> maxVolume * 0.6f
            result.score > (30 + offset) -> maxVolume * 0.35f
            else -> return PolicyAction.Ignore
        }

        // Already masking: adjust level instead of restarting the track
        return if (isMaskingActive) {
            PolicyAction.SetVolume(targetVolume)
        } else {
            PolicyAction.StartMasking(sound, targetVolume)
        }
    }

    private fun isNearWakeTime(currentTimeMs: Long, settings: SleepSettings): Boolean {
        val wakeTimeMs = settings.wakeTimeHour * 60 * 60 * 1000L + settings.wakeTimeMinute * 60 * 1000L
        val msOfDay = currentTimeMs % (24L * 60 * 60 * 1000)
        val timeDiffMs = kotlin.math.abs(msOfDay - wakeTimeMs)
        val wrapAroundDiff = 24L * 60 * 60 * 1000 - timeDiffMs

        return kotlin.math.min(timeDiffMs, wrapAroundDiff) <= 90_000
    }
}
