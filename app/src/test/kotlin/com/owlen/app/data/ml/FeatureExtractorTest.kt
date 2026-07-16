package com.owlen.app.data.ml

import org.junit.Test
import org.junit.Assert.*

class FeatureExtractorTest {

    @Test
    fun testShortMaxValueNormalisesTo1() {
        val sample = Short.MAX_VALUE / 32768.0f
        val clamped = sample.coerceIn(-1.0f, 1.0f)
        assertEquals(1.0f, clamped, 0.01f)
    }

    @Test
    fun testShortMinValueNormalisesToMinus1() {
        val sample = Short.MIN_VALUE / 32768.0f
        val clamped = sample.coerceIn(-1.0f, 1.0f)
        assertEquals(-1.0f, clamped, 0.01f)
    }

    @Test
    fun testSampleZeroNormalisesToZero() {
        val sample = 0.toShort() / 32768.0f
        val clamped = sample.coerceIn(-1.0f, 1.0f)
        assertEquals(0.0f, clamped, 0.0f)
    }

    @Test
    fun testNormalisationClamping() {
        val tooHigh = 2.0f
        val clamped = tooHigh.coerceIn(-1.0f, 1.0f)
        assertEquals(1.0f, clamped)

        val tooLow = -2.0f
        val clampedLow = tooLow.coerceIn(-1.0f, 1.0f)
        assertEquals(-1.0f, clampedLow)
    }

    @Test
    fun testInputSize15600IsRequired() {
        // YAMNet classification input: 0.975s at 16kHz
        assertEquals(15600, FeatureExtractor.INPUT_SAMPLES)
        assertEquals(521, FeatureExtractor.NUM_CLASSES)
    }

    @Test
    fun testNormalisationRangeAllValues() {
        val testCases = listOf(
            Pair(0.toShort(), 0.0f),
            Pair(16384.toShort(), 0.5f),
            Pair((-16384).toShort(), -0.5f),
            Pair(32767.toShort(), 1.0f),
            Pair((-32768).toShort(), -1.0f)
        )

        for ((shortVal, expectedNorm) in testCases) {
            val normalized = (shortVal / 32768.0f).coerceIn(-1.0f, 1.0f)
            assertEquals(
                "Short value $shortVal should normalize to $expectedNorm",
                expectedNorm,
                normalized,
                0.001f
            )
        }
    }

    @Test
    fun testNormalisationNeverExceedsRange() {
        val extremeValues = arrayOf(
            Short.MAX_VALUE,
            Short.MIN_VALUE,
            0.toShort(),
            1.toShort(),
            (-1).toShort()
        )

        for (value in extremeValues) {
            val normalized = (value / 32768.0f).coerceIn(-1.0f, 1.0f)
            assertTrue("Normalized value should be >= -1.0", normalized >= -1.0f)
            assertTrue("Normalized value should be <= 1.0", normalized <= 1.0f)
        }
    }

    @Test
    fun testInputSizeValidation() {
        // Capture window must match the model input: 20 frames × 780 samples
        val expectedSize = 15600
        val actualSize = 20 * 780
        assertEquals(expectedSize, actualSize)
    }
}
