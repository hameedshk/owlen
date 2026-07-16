package com.owlen.app.domain.model

data class SleepSettings(
    val sleepWindowStartHour: Int,
    val sleepWindowEndHour: Int,
    val wakeTimeHour: Int,
    val wakeTimeMinute: Int,
    val sensitivity: Sensitivity,
    val maxVolume: Float,
    val preferredSound: MaskingSound,
    val autoStopDurationMs: Long,
    val rainBehaviourEnabled: Boolean,
    val soundLevelFloor: Float
) {
    companion object {
        fun default() = SleepSettings(
            sleepWindowStartHour = 22,
            sleepWindowEndHour = 6,
            wakeTimeHour = 6,
            wakeTimeMinute = 0,
            sensitivity = Sensitivity.MEDIUM,
            maxVolume = 0.7f,
            preferredSound = MaskingSound.BROWN_NOISE,
            autoStopDurationMs = 60_000L,
            rainBehaviourEnabled = true,
            soundLevelFloor = 50f
        )
    }
}
