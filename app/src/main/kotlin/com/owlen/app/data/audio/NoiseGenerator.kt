package com.owlen.app.data.audio

import kotlin.math.PI
import kotlin.math.cos
import kotlin.random.Random

object NoiseGenerator {
    private const val SAMPLE_RATE = 44100
    private const val DURATION_SECONDS = 10
    private const val BUFFER_SIZE = SAMPLE_RATE * DURATION_SECONDS  // 441,000 samples

    fun generateWhiteNoise(): ShortArray {
        val buffer = ShortArray(BUFFER_SIZE)
        for (i in 0 until BUFFER_SIZE) {
            val sample = (Random.nextFloat() * 2f - 1f) * Short.MAX_VALUE
            buffer[i] = sample.toInt().toShort()
        }
        return buffer
    }

    fun generatePinkNoise(): ShortArray {
        val buffer = ShortArray(BUFFER_SIZE)
        val numGenerators = 16
        val generators = FloatArray(numGenerators)
        val random = Random(System.currentTimeMillis())

        for (i in 0 until BUFFER_SIZE) {
            // Update generators at different rates
            if (i % 1 == 0) generators[0] = random.nextFloat() * 2f - 1f
            if (i % 2 == 0) generators[1] = random.nextFloat() * 2f - 1f
            if (i % 4 == 0) generators[2] = random.nextFloat() * 2f - 1f
            if (i % 8 == 0) generators[3] = random.nextFloat() * 2f - 1f
            if (i % 16 == 0) generators[4] = random.nextFloat() * 2f - 1f
            if (i % 32 == 0) generators[5] = random.nextFloat() * 2f - 1f
            if (i % 64 == 0) generators[6] = random.nextFloat() * 2f - 1f
            if (i % 128 == 0) generators[7] = random.nextFloat() * 2f - 1f
            if (i % 256 == 0) generators[8] = random.nextFloat() * 2f - 1f
            if (i % 512 == 0) generators[9] = random.nextFloat() * 2f - 1f
            if (i % 1024 == 0) generators[10] = random.nextFloat() * 2f - 1f
            if (i % 2048 == 0) generators[11] = random.nextFloat() * 2f - 1f
            if (i % 4096 == 0) generators[12] = random.nextFloat() * 2f - 1f
            if (i % 8192 == 0) generators[13] = random.nextFloat() * 2f - 1f
            if (i % 16384 == 0) generators[14] = random.nextFloat() * 2f - 1f
            if (i % 32768 == 0) generators[15] = random.nextFloat() * 2f - 1f

            val sum = generators.sum()
            val normalized = sum / numGenerators
            val clamped = normalized.coerceIn(-1f, 1f)
            buffer[i] = (clamped * Short.MAX_VALUE).toInt().toShort()
        }
        return buffer
    }

    fun generateBrownNoise(): ShortArray {
        val buffer = ShortArray(BUFFER_SIZE)
        var runningSum = 0f
        val random = Random(System.currentTimeMillis())

        for (i in 0 until BUFFER_SIZE) {
            val delta = random.nextFloat() * 0.02f - 0.01f
            runningSum += delta
            runningSum = runningSum.coerceIn(-1f, 1f)
            buffer[i] = (runningSum * Short.MAX_VALUE).toInt().toShort()
        }
        return buffer
    }
}
