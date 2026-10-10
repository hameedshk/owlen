package com.owlen.app.domain.timer

class SleepTimerCalculator {
    companion object {
        const val NEAR_SILENT_VOLUME = 0.02f
    }

    fun volumeAt(elapsedMs: Long, durationMs: Long, targetVolume: Float): Float {
        if (durationMs <= 0L) return targetVolume
        val t = (elapsedMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
        return NEAR_SILENT_VOLUME + (targetVolume - NEAR_SILENT_VOLUME) * t
    }
}
