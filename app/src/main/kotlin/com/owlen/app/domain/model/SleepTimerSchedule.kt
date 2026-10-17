package com.owlen.app.domain.model

data class SleepTimerSchedule(
    val enabled: Boolean = false,
    val startHour: Int = 22,
    val startMinute: Int = 0,
    val endHour: Int = 6,
    val endMinute: Int = 0,
    val sound: MaskingSound = MaskingSound.RAIN,
    val fadeInMinutes: Int = 10,
    val maxVolume: Float = 0.5f
)

/**
 * Minutes from [SleepTimerSchedule.startHour]/[SleepTimerSchedule.startMinute] to
 * [SleepTimerSchedule.endHour]/[SleepTimerSchedule.endMinute]. An end time at or before
 * the start time (e.g. 23:00 -> 06:00, or a start equal to the end) is treated as
 * crossing midnight and wraps to the next day, matching the scheduler's wrap convention.
 */
val SleepTimerSchedule.windowDurationMinutes: Int
    get() {
        val startTotal = startHour * 60 + startMinute
        val endTotal = endHour * 60 + endMinute
        val diff = endTotal - startTotal
        return if (diff <= 0) diff + 24 * 60 else diff
    }
