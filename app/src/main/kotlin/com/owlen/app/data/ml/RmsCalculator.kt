package com.owlen.app.data.ml

import kotlin.math.log10
import kotlin.math.sqrt

object RmsCalculator {
    // Typical smartphone MEMS mic clips (0 dBFS) around 90 dB SPL. The scorer
    // works on an estimated SPL scale (soundLevelFloor ~50, ceiling 90).
    const val DBFS_TO_DBSPL_OFFSET = 90f

    fun calculate(pcmWindow: ShortArray): Float {
        var sum = 0.0
        for (sample in pcmWindow) {
            val normalised = sample / 32768.0
            sum += normalised * normalised
        }
        val rms = sqrt(sum / pcmWindow.size)
        val dBFS = 20 * log10(rms + 1e-9)
        return (dBFS.toFloat() + DBFS_TO_DBSPL_OFFSET).coerceAtLeast(0f)
    }
}
