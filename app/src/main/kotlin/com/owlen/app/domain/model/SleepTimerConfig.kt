package com.owlen.app.domain.model

/**
 * Configuration for the sleep timer (play now mode): sound, duration, fade-out, max volume.
 * Separate from SleepTimerSchedule which is for scheduled nightly playback.
 */
data class SleepTimerConfig(
    val sound: MaskingSound = MaskingSound.RAIN,
    val durationMinutes: Int = 30,
    val fadeOutMinutes: Int = 10,
    val maxVolume: Float = 0.7f
)
