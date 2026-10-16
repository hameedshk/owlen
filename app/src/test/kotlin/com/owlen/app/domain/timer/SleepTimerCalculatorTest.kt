package com.owlen.app.domain.timer

import org.junit.Test
import kotlin.test.assertEquals

class SleepTimerCalculatorTest {

    private val calculator = SleepTimerCalculator

    // --- fadeOutVolume: timer mode, holds at target then ramps down to silence ---

    @Test
    fun `fadeOutVolume holds at target while outside the fade window`() {
        val result = calculator.fadeOutVolume(
            elapsedMs = 0L, durationMs = 60_000L, fadeOutMs = 10_000L, targetVolume = 0.8f
        )
        assertEquals(0.8f, result, 0.0001f)
    }

    @Test
    fun `fadeOutVolume at the fade window boundary still returns target`() {
        // remaining == fadeOutMs is still the last instant of the hold phase
        val result = calculator.fadeOutVolume(
            elapsedMs = 50_000L, durationMs = 60_000L, fadeOutMs = 10_000L, targetVolume = 0.8f
        )
        assertEquals(0.8f, result, 0.0001f)
    }

    @Test
    fun `fadeOutVolume midway through the fade window is halfway to the floor`() {
        val target = 0.8f
        val result = calculator.fadeOutVolume(
            elapsedMs = 55_000L, durationMs = 60_000L, fadeOutMs = 10_000L, targetVolume = target
        )
        val floor = SleepTimerCalculator.rampFloor(target)
        val expected = floor + (target - floor) * 0.5f
        assertEquals(expected, result, 0.0001f)
    }

    @Test
    fun `fadeOutVolume reaches silence exactly at duration end`() {
        val result = calculator.fadeOutVolume(
            elapsedMs = 60_000L, durationMs = 60_000L, fadeOutMs = 10_000L, targetVolume = 0.8f
        )
        assertEquals(0f, result, 0.0001f)
    }

    @Test
    fun `fadeOutVolume past duration stays silent`() {
        val result = calculator.fadeOutVolume(
            elapsedMs = 90_000L, durationMs = 60_000L, fadeOutMs = 10_000L, targetVolume = 0.8f
        )
        assertEquals(0f, result, 0.0001f)
    }

    @Test
    fun `fadeOutVolume with zero duration is silent`() {
        val result = calculator.fadeOutVolume(
            elapsedMs = 0L, durationMs = 0L, fadeOutMs = 10_000L, targetVolume = 0.8f
        )
        assertEquals(0f, result, 0.0001f)
    }

    @Test
    fun `fadeOutVolume with no fade window holds target for the whole duration`() {
        val target = 0.8f
        val atStart = calculator.fadeOutVolume(0L, 60_000L, fadeOutMs = 0L, targetVolume = target)
        val atMid = calculator.fadeOutVolume(30_000L, 60_000L, fadeOutMs = 0L, targetVolume = target)
        assertEquals(target, atStart, 0.0001f)
        assertEquals(target, atMid, 0.0001f)
    }

    @Test
    fun `fadeOutVolume starts ramping immediately when fadeOutMs equals durationMs`() {
        // Confirmed live on-device: Duration=5m, Fade out=5m clamps to one continuous
        // ramp with no hold phase, so volume is already below target one tick after start.
        val target = 0.7f
        val atStart = calculator.fadeOutVolume(0L, 300_000L, fadeOutMs = 300_000L, targetVolume = target)
        val oneSecondIn = calculator.fadeOutVolume(1_000L, 300_000L, fadeOutMs = 300_000L, targetVolume = target)
        assertEquals(target, atStart, 0.0001f)
        assert(oneSecondIn < target) { "Expected ramp to have started, got $oneSecondIn" }
    }

    // --- fadeInVolume: schedule mode, starts near-silent and ramps up to target ---

    @Test
    fun `fadeInVolume starts at the near-silent floor`() {
        val result = calculator.fadeInVolume(elapsedMs = 0L, fadeInMs = 600_000L, targetVolume = 0.8f)
        assertEquals(SleepTimerCalculator.NEAR_SILENT_VOLUME, result, 0.0001f)
    }

    @Test
    fun `fadeInVolume reaches target exactly at fadeInMs`() {
        val target = 0.8f
        val result = calculator.fadeInVolume(elapsedMs = 600_000L, fadeInMs = 600_000L, targetVolume = target)
        assertEquals(target, result, 0.0001f)
    }

    @Test
    fun `fadeInVolume midpoint is halfway between floor and target`() {
        val target = 0.8f
        val result = calculator.fadeInVolume(elapsedMs = 300_000L, fadeInMs = 600_000L, targetVolume = target)
        val floor = SleepTimerCalculator.rampFloor(target)
        val expected = floor + (target - floor) * 0.5f
        assertEquals(expected, result, 0.0001f)
    }

    @Test
    fun `fadeInVolume clamps to target after fadeInMs has elapsed`() {
        val target = 0.8f
        val result = calculator.fadeInVolume(elapsedMs = 900_000L, fadeInMs = 600_000L, targetVolume = target)
        assertEquals(target, result, 0.0001f)
    }

    @Test
    fun `fadeInVolume with no fade window jumps straight to target`() {
        val target = 0.8f
        val result = calculator.fadeInVolume(elapsedMs = 0L, fadeInMs = 0L, targetVolume = target)
        assertEquals(target, result, 0.0001f)
    }

    @Test
    fun `fadeInVolume below near-silent target clamps the floor to target`() {
        val target = 0.01f // below NEAR_SILENT_VOLUME
        val atStart = calculator.fadeInVolume(elapsedMs = 0L, fadeInMs = 600_000L, targetVolume = target)
        assertEquals(target, atStart, 0.0001f)
    }

    @Test
    fun `fadeInVolume is monotonically non-decreasing across the ramp`() {
        val target = 0.9f
        var previous = calculator.fadeInVolume(0L, 600_000L, target)
        for (elapsedMs in listOf(100_000L, 300_000L, 450_000L, 600_000L)) {
            val volume = calculator.fadeInVolume(elapsedMs, 600_000L, target)
            assert(volume >= previous - 0.0001f) { "Ramp decreased at $elapsedMs: $previous -> $volume" }
            previous = volume
        }
    }
}
