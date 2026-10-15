package com.owlen.app.domain.timer

object SleepTimerCalculator {
    const val NEAR_SILENT_VOLUME = 0.02f

    /**
     * Where a ramp begins. Normally the near-silent floor, but a target below that
     * floor (the volume slider reaches 0%) has to cap it — otherwise the ramp would
     * open louder than the volume the user actually asked for.
     */
    fun rampFloor(targetVolume: Float): Float = minOf(NEAR_SILENT_VOLUME, targetVolume)

    /**
     * Timer fade-out: hold at target volume, then ramp down to silence over the final fadeOutMs.
     * Used for "play now" timer mode.
     */
    fun fadeOutVolume(elapsedMs: Long, durationMs: Long, fadeOutMs: Long, targetVolume: Float): Float {
        if (durationMs <= 0L) return 0f
        val remaining = durationMs - elapsedMs
        if (remaining <= 0L) return 0f
        if (fadeOutMs <= 0L || remaining >= fadeOutMs) return targetVolume
        val floor = rampFloor(targetVolume)
        val t = remaining.toFloat() / fadeOutMs.toFloat()
        return floor + (targetVolume - floor) * t
    }

    /**
     * Schedule fade-in: start near-silent, ramp up to target over fadeInMs, then hold.
     * Used for scheduled playback mode.
     */
    fun fadeInVolume(elapsedMs: Long, fadeInMs: Long, targetVolume: Float): Float {
        if (fadeInMs <= 0L) return targetVolume
        val floor = rampFloor(targetVolume)
        val t = (elapsedMs.toFloat() / fadeInMs.toFloat()).coerceIn(0f, 1f)
        return floor + (targetVolume - floor) * t
    }

    /**
     * Legacy fade-in: ramp from near-silent to target over duration. Deprecated.
     * Kept for backward compatibility until service refactoring in Phase 6.
     */
    fun volumeAt(elapsedMs: Long, durationMs: Long, targetVolume: Float): Float {
        if (durationMs <= 0L) return targetVolume
        val floor = rampFloor(targetVolume)
        val t = (elapsedMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
        return floor + (targetVolume - floor) * t
    }
}
