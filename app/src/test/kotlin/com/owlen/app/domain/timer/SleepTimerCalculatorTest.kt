package com.owlen.app.domain.timer

import org.junit.Test
import kotlin.test.assertEquals

class SleepTimerCalculatorTest {

    private val calculator = SleepTimerCalculator()

    @Test
    fun `volumeAt elapsed 0 returns near-silent volume`() {
        val result = calculator.volumeAt(elapsedMs = 0L, durationMs = 60_000L, targetVolume = 0.8f)
        assertEquals(SleepTimerCalculator.NEAR_SILENT_VOLUME, result, 0.0001f)
    }

    @Test
    fun `volumeAt elapsed equals duration returns target volume`() {
        val targetVolume = 0.75f
        val result = calculator.volumeAt(elapsedMs = 60_000L, durationMs = 60_000L, targetVolume = targetVolume)
        assertEquals(targetVolume, result, 0.0001f)
    }

    @Test
    fun `volumeAt midpoint returns linear interpolation`() {
        val targetVolume = 0.8f
        val result = calculator.volumeAt(elapsedMs = 30_000L, durationMs = 60_000L, targetVolume = targetVolume)
        val expected = SleepTimerCalculator.NEAR_SILENT_VOLUME +
                (targetVolume - SleepTimerCalculator.NEAR_SILENT_VOLUME) * 0.5f
        assertEquals(expected, result, 0.0001f)
    }

    @Test
    fun `volumeAt elapsed greater than duration clamps to target`() {
        val targetVolume = 0.7f
        val result = calculator.volumeAt(elapsedMs = 120_000L, durationMs = 60_000L, targetVolume = targetVolume)
        assertEquals(targetVolume, result, 0.0001f)
    }

    @Test
    fun `volumeAt zero duration returns target volume`() {
        val targetVolume = 0.6f
        val result = calculator.volumeAt(elapsedMs = 0L, durationMs = 0L, targetVolume = targetVolume)
        assertEquals(targetVolume, result, 0.0001f)
    }

    @Test
    fun `volumeAt negative duration returns target volume`() {
        val targetVolume = 0.5f
        val result = calculator.volumeAt(elapsedMs = 0L, durationMs = -1000L, targetVolume = targetVolume)
        assertEquals(targetVolume, result, 0.0001f)
    }

    @Test
    fun `ramp is monotonically non-decreasing over duration`() {
        val targetVolume = 0.9f
        var previousVolume = calculator.volumeAt(0L, 60_000L, targetVolume)

        for (elapsedMs in listOf(5_000L, 15_000L, 30_000L, 45_000L, 60_000L)) {
            val volume = calculator.volumeAt(elapsedMs, 60_000L, targetVolume)
            assert(volume >= previousVolume - 0.0001f) { "Ramp decreased at $elapsedMs: $previousVolume -> $volume" }
            previousVolume = volume
        }
    }

    @Test
    fun `degenerate target volume below near-silent clamps start`() {
        val targetVolume = 0.01f  // Below NEAR_SILENT_VOLUME
        val atStart = calculator.volumeAt(0L, 60_000L, targetVolume)
        val atMid = calculator.volumeAt(30_000L, 60_000L, targetVolume)
        val atEnd = calculator.volumeAt(60_000L, 60_000L, targetVolume)

        // Start should clamp to target, and ramp should be monotonic
        assert(atStart <= targetVolume + 0.0001f)
        assert(atMid >= atStart - 0.0001f)
        assert(atEnd >= atMid - 0.0001f)
    }
}
