package com.owlen.app.data.ml

import kotlin.math.absoluteValue
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sqrt
import org.junit.Test
import org.junit.Assert.*

class RmsCalculatorTest {

    @Test
    fun testAllZeroInputClampsToZeroDbSpl() {
        val input = ShortArray(1000) { 0 }
        val result = RmsCalculator.calculate(input)

        // Silence is far below the SPL floor and must clamp to 0, never negative
        assertEquals(0f, result, 0.001f)
    }

    @Test
    fun testAllMaxShortValuesReturnsNearOffsetDbSpl() {
        val input = ShortArray(1000) { Short.MAX_VALUE }
        val result = RmsCalculator.calculate(input)

        // Full-scale signal is 0 dBFS, i.e. the SPL offset (~90 dB)
        assertTrue((result - RmsCalculator.DBFS_TO_DBSPL_OFFSET).absoluteValue < 1.0f)
    }

    @Test
    fun testKnownInputReturnsExpectedDbSpl() {
        val input = ShortArray(16) { (1000).toShort() }
        val result = RmsCalculator.calculate(input)

        val normalised = 1000.0 / 32768.0
        val sum = 16 * normalised.pow(2.0)
        val rms = sqrt(sum / 16)
        val expectedDbfs = (20 * log10(rms + 1e-9)).toFloat()
        val expected = expectedDbfs + RmsCalculator.DBFS_TO_DBSPL_OFFSET

        assertTrue((result - expected).absoluteValue <= 1.0f)
    }

    @Test
    fun testResultIsAlwaysFiniteAndNonNegative() {
        val inputs = listOf(
            ShortArray(1000) { 1 },
            ShortArray(1000) { 0 },
            ShortArray(1000) { Short.MIN_VALUE },
            ShortArray(1000) { Short.MAX_VALUE }
        )
        for (input in inputs) {
            val result = RmsCalculator.calculate(input)
            assertTrue(result.isFinite())
            assertTrue(result >= 0f)
        }
    }
}
