package com.owlen.app.data.audio

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NoiseGeneratorTest {
    companion object {
        private const val EXPECTED_BUFFER_SIZE = 441000
    }

    @Test
    fun generateWhiteNoise_returnsCorrectSize() {
        val buffer = NoiseGenerator.generateWhiteNoise()
        assertEquals(EXPECTED_BUFFER_SIZE, buffer.size)
    }

    @Test
    fun generateWhiteNoise_valuesWithinShortRange() {
        val buffer = NoiseGenerator.generateWhiteNoise()
        for (sample in buffer) {
            assertTrue(sample >= Short.MIN_VALUE && sample <= Short.MAX_VALUE)
        }
    }

    @Test
    fun generateWhiteNoise_bufferNotAllZeros() {
        val buffer = NoiseGenerator.generateWhiteNoise()
        val hasNonZero = buffer.any { it != 0.toShort() }
        assertTrue(hasNonZero)
    }

    @Test
    fun generatePinkNoise_returnsCorrectSize() {
        val buffer = NoiseGenerator.generatePinkNoise()
        assertEquals(EXPECTED_BUFFER_SIZE, buffer.size)
    }

    @Test
    fun generatePinkNoise_valuesWithinShortRange() {
        val buffer = NoiseGenerator.generatePinkNoise()
        for (sample in buffer) {
            assertTrue(sample >= Short.MIN_VALUE && sample <= Short.MAX_VALUE)
        }
    }

    @Test
    fun generatePinkNoise_bufferNotAllZeros() {
        val buffer = NoiseGenerator.generatePinkNoise()
        val hasNonZero = buffer.any { it != 0.toShort() }
        assertTrue(hasNonZero)
    }

    @Test
    fun generateBrownNoise_returnsCorrectSize() {
        val buffer = NoiseGenerator.generateBrownNoise()
        assertEquals(EXPECTED_BUFFER_SIZE, buffer.size)
    }

    @Test
    fun generateBrownNoise_valuesWithinShortRange() {
        val buffer = NoiseGenerator.generateBrownNoise()
        for (sample in buffer) {
            assertTrue(sample >= Short.MIN_VALUE && sample <= Short.MAX_VALUE)
        }
    }

    @Test
    fun generateBrownNoise_bufferNotAllZeros() {
        val buffer = NoiseGenerator.generateBrownNoise()
        val hasNonZero = buffer.any { it != 0.toShort() }
        assertTrue(hasNonZero)
    }

    @Test
    fun generateFan_returnsCorrectSize() {
        val buffer = NoiseGenerator.generateFan()
        assertEquals(EXPECTED_BUFFER_SIZE, buffer.size)
    }

    @Test
    fun generateFan_bufferNotAllZeros() {
        val buffer = NoiseGenerator.generateFan()
        val hasNonZero = buffer.any { it != 0.toShort() }
        assertTrue(hasNonZero)
    }

    @Test
    fun generateFan_hasLessHighFrequencyEnergyThanWhiteNoise() {
        // Fan is low-passed, so its mean sample-to-sample jump (a proxy for
        // high-frequency content) should be far below white noise's
        val fanDelta = meanAbsDelta(NoiseGenerator.generateFan())
        val whiteDelta = meanAbsDelta(NoiseGenerator.generateWhiteNoise())
        assertTrue(fanDelta < whiteDelta / 2)
    }

    @Test
    fun generateRain_returnsCorrectSize() {
        val buffer = NoiseGenerator.generateRain()
        assertEquals(EXPECTED_BUFFER_SIZE, buffer.size)
    }

    @Test
    fun generateRain_bufferNotAllZeros() {
        val buffer = NoiseGenerator.generateRain()
        val hasNonZero = buffer.any { it != 0.toShort() }
        assertTrue(hasNonZero)
    }

    @Test
    fun generateOceanWaves_returnsCorrectSize() {
        val buffer = NoiseGenerator.generateOceanWaves()
        assertEquals(EXPECTED_BUFFER_SIZE, buffer.size)
    }

    @Test
    fun generateOceanWaves_bufferNotAllZeros() {
        val buffer = NoiseGenerator.generateOceanWaves()
        val hasNonZero = buffer.any { it != 0.toShort() }
        assertTrue(hasNonZero)
    }

    @Test
    fun generateOceanWaves_troughsAtLoopBoundaryAndCrestsMidBuffer() {
        val buffer = NoiseGenerator.generateOceanWaves()
        val windowSize = 11025  // 250 ms at 44.1 kHz

        val startRms = rms(buffer, 0, windowSize)
        val endRms = rms(buffer, buffer.size - windowSize, buffer.size)
        val crestRms = rms(buffer, (buffer.size - windowSize) / 2, (buffer.size + windowSize) / 2)

        // The wave crest (mid-buffer) must be clearly louder than the trough
        // at both ends, so the loop point sits in the quiet part of the wave
        assertTrue(crestRms > startRms * 2)
        assertTrue(crestRms > endRms * 2)
    }

    private fun rms(buffer: ShortArray, from: Int, to: Int): Double {
        var sumSquares = 0.0
        for (i in from until to) {
            val sample = buffer[i].toDouble()
            sumSquares += sample * sample
        }
        return kotlin.math.sqrt(sumSquares / (to - from))
    }

    private fun meanAbsDelta(buffer: ShortArray): Double {
        var sum = 0.0
        for (i in 1 until buffer.size) {
            sum += kotlin.math.abs(buffer[i] - buffer[i - 1]).toDouble()
        }
        return sum / (buffer.size - 1)
    }
}
