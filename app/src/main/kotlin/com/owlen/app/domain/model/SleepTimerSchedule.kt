package com.owlen.app.domain.model

data class SleepTimerSchedule(
    val enabled: Boolean = false,
    val startHour: Int = 6,
    val startMinute: Int = 0,
    val endHour: Int = 8,
    val endMinute: Int = 0,
    val sound: MaskingSound = MaskingSound.RAIN,
    val fadeInMinutes: Int = 10,
    val maxVolume: Float = 0.5f
)
