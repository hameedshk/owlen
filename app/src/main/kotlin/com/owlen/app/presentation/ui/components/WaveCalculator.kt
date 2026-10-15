package com.owlen.app.presentation.ui.components

import androidx.compose.ui.geometry.Offset
import kotlin.math.sin

/**
 * Utility for calculating wave and hill paths for canvas drawing.
 * Extracted for reusability and testability of math logic.
 */
object WaveCalculator {
    /**
     * Calculates sine wave points for ocean scene.
     * Wave formula: y = baseY + (band * bandHeight) + amplitude * sin((x * frequency + phaseShift) * PI/180)
     *
     * @param width Canvas width
     * @param baseY Starting Y position (horizon line)
     * @param band Band index (0-based, determines phase and amplitude)
     * @param amplitude Vertical wave height in pixels
     * @param frequency Wave oscillation rate (0.01f = standard, lower = sparser)
     * @param phaseShift Offset per band (30f = stacked illusion)
     * @param bandHeight Vertical spacing between bands
     * @param step Horizontal step size for point sampling (smaller = smoother but slower)
     */
    fun calculateWavePoints(
        width: Float,
        baseY: Float,
        band: Int,
        amplitude: Float,
        frequency: Float,
        phaseShift: Float,
        bandHeight: Float,
        step: Float = 5f
    ): List<Offset> {
        val points = mutableListOf<Offset>()
        var x = 0f
        while (x <= width) {
            // sin() expects radians, convert from degrees
            val y = baseY + (band * bandHeight) + amplitude * sin((x * frequency + phaseShift) * Math.PI / 180).toFloat()
            points.add(Offset(x, y))
            x += step
        }
        return points
    }

    /**
     * Calculates rolling hill points for brown noise scene.
     * Hill formula: y = baseY + offset + amplitude * sin((x / waveLength) * PI)
     *
     * @param width Canvas width
     * @param baseY Starting Y position
     * @param hillIndex Hill layer (0-based, determines offset and amplitude)
     * @param baseAmplitude Base vertical height
     * @param amplitudeStep Height increase per hill layer
     * @param baseOffset Base vertical offset
     * @param offsetPerHill Vertical spacing between hills
     * @param step Horizontal step size for point sampling
     */
    fun calculateHillPoints(
        width: Float,
        baseY: Float,
        hillIndex: Int,
        baseAmplitude: Float,
        amplitudeStep: Float,
        baseOffset: Float = 0f,
        offsetPerHill: Float = 15f,
        step: Float = 10f
    ): List<Offset> {
        val points = mutableListOf<Offset>()
        val offset = baseOffset + (hillIndex * offsetPerHill)
        // Wave length decreases per layer for perspective effect
        // First hill: width/2, second: width/3, third: width/4, etc.
        val waveLength = width / (2 + hillIndex)
        val amplitude = baseAmplitude + hillIndex * amplitudeStep

        var x = 0f
        while (x <= width) {
            // sin() expects radians
            val y = baseY + offset + amplitude * sin((x / waveLength) * Math.PI).toFloat()
            points.add(Offset(x, y))
            x += step
        }
        return points
    }
}
