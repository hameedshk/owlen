package com.owlen.app.data.audio

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

object NoiseGenerator {
    private const val SAMPLE_RATE = 44100
    private const val DURATION_SECONDS = 10
    private const val BUFFER_SIZE = SAMPLE_RATE * DURATION_SECONDS  // 441,000 samples
    private const val CROSSFADE_SAMPLES = SAMPLE_RATE / 10  // 100 ms loop-boundary blend

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

    fun generateFan(): ShortArray {
        val extended = FloatArray(BUFFER_SIZE + CROSSFADE_SAMPLES)
        val random = Random(System.currentTimeMillis())
        var brown = 0f
        var lowPassed = 0f
        val lowPassAlpha = 0.15f
        val humAmplitude = 0.05f
        // 120 Hz and 0.5 Hz are whole numbers of cycles per 10 s buffer, so both loop seamlessly
        val humHz = 120.0
        val wobbleHz = 0.5
        val wobbleDepth = 0.10f

        for (i in extended.indices) {
            brown = (brown + random.nextFloat() * 0.02f - 0.01f).coerceIn(-1f, 1f)
            lowPassed += lowPassAlpha * (brown - lowPassed)
            val hum = humAmplitude * sin(2.0 * PI * humHz * i / SAMPLE_RATE).toFloat()
            val wobble = 1f + wobbleDepth * sin(2.0 * PI * wobbleHz * i / SAMPLE_RATE).toFloat()
            extended[i] = (lowPassed + hum) * wobble
        }
        return normalizeToPcm(makeLoopable(extended))
    }

    fun generateRain(): ShortArray {
        val extended = FloatArray(BUFFER_SIZE + CROSSFADE_SAMPLES)
        val random = Random(System.currentTimeMillis())
        var lowPass = 0f
        val highPassAlpha = 0.25f
        val hissLevel = 0.35f

        for (i in extended.indices) {
            val white = random.nextFloat() * 2f - 1f
            lowPass += highPassAlpha * (white - lowPass)
            extended[i] = (white - lowPass) * hissLevel
        }

        // Individual droplets: short decaying noise bursts scattered over the buffer
        val dropletsPerSecond = 8
        val dropletCount = dropletsPerSecond * (extended.size / SAMPLE_RATE)
        for (d in 0 until dropletCount) {
            val start = random.nextInt(extended.size)
            val durationSamples = (SAMPLE_RATE * (0.005f + random.nextFloat() * 0.010f)).toInt()
            val amplitude = 0.3f + random.nextFloat() * 0.5f
            for (j in 0 until durationSamples) {
                val index = start + j
                if (index >= extended.size) break
                val decay = exp(-5f * j / durationSamples)
                extended[index] += (random.nextFloat() * 2f - 1f) * amplitude * decay
            }
        }
        return normalizeToPcm(makeLoopable(extended))
    }

    fun generateOceanWaves(): ShortArray {
        val extended = FloatArray(BUFFER_SIZE + CROSSFADE_SAMPLES)
        val random = Random(System.currentTimeMillis())
        var brown = 0f

        for (i in extended.indices) {
            brown = (brown + random.nextFloat() * 0.02f - 0.01f).coerceIn(-1f, 1f)
            extended[i] = brown
        }
        val bed = makeLoopable(extended)

        // Exactly one raised-cosine wave cycle per buffer, so the envelope
        // returns to the trough at the loop point; trough stays above zero
        // so the sound never fully drops out between waves
        val trough = 0.25f
        for (i in bed.indices) {
            val envelope = trough + (1f - trough) * 0.5f * (1f - cos(2.0 * PI * i / bed.size).toFloat())
            bed[i] *= envelope
        }
        return normalizeToPcm(bed)
    }

    // Blends the first 100 ms with the samples that continue past the loop
    // point, so filtered/stateful signals wrap without an audible click
    private fun makeLoopable(extended: FloatArray): FloatArray {
        val looped = FloatArray(BUFFER_SIZE)
        extended.copyInto(looped, 0, 0, BUFFER_SIZE)
        for (i in 0 until CROSSFADE_SAMPLES) {
            val t = i.toFloat() / CROSSFADE_SAMPLES
            looped[i] = extended[i] * t + extended[BUFFER_SIZE + i] * (1f - t)
        }
        return looped
    }

    private fun normalizeToPcm(samples: FloatArray, peak: Float = 0.9f): ShortArray {
        var maxAbs = 0f
        for (sample in samples) {
            val magnitude = abs(sample)
            if (magnitude > maxAbs) maxAbs = magnitude
        }
        val scale = if (maxAbs > 0f) peak / maxAbs else 0f
        val buffer = ShortArray(samples.size)
        for (i in samples.indices) {
            buffer[i] = (samples[i] * scale * Short.MAX_VALUE).toInt().toShort()
        }
        return buffer
    }
}
