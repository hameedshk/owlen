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
}
