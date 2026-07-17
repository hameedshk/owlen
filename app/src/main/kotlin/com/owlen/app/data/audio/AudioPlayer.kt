package com.owlen.app.data.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.owlen.app.domain.model.MaskingSound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.Closeable

class AudioPlayer : Closeable {
    companion object {
        const val PLAYBACK_SAMPLE_RATE = 44100
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        const val AUDIO_USAGE = AudioAttributes.USAGE_MEDIA
        const val CONTENT_TYPE = AudioAttributes.CONTENT_TYPE_MUSIC
        const val FADE_STEPS = 100
        const val FADE_DURATION_MS = 2000L
        const val FADE_STEP_DELAY_MS = 20L
        const val MINIMUM_MASKING_DURATION_MS = 30_000L
    }

    private val noiseBuffers = mutableMapOf<MaskingSound, ShortArray>()

    private var audioTrack: AudioTrack? = null
    private var startMaskingTimeMs: Long = 0L
    private var currentVolume: Float = 0f

    // Buffers are synthesized on first use (on Dispatchers.Default via
    // startMasking) rather than eagerly, so construction stays cheap
    private fun bufferFor(sound: MaskingSound): ShortArray =
        noiseBuffers.getOrPut(sound) {
            when (sound) {
                MaskingSound.BROWN_NOISE -> NoiseGenerator.generateBrownNoise()
                MaskingSound.PINK_NOISE -> NoiseGenerator.generatePinkNoise()
                MaskingSound.WHITE_NOISE -> NoiseGenerator.generateWhiteNoise()
                MaskingSound.FAN -> NoiseGenerator.generateFan()
                MaskingSound.RAIN -> NoiseGenerator.generateRain()
                MaskingSound.OCEAN_WAVES -> NoiseGenerator.generateOceanWaves()
            }
        }

    suspend fun startMasking(sound: MaskingSound, volume: Float, maxVolume: Float) {
        withContext(Dispatchers.Default) {
            val cappedVolume = volume.coerceAtMost(maxVolume)
            val noiseBuffer = bufferFor(sound)

            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AUDIO_USAGE)
                        .setContentType(CONTENT_TYPE)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setSampleRate(PLAYBACK_SAMPLE_RATE)
                        .setChannelMask(CHANNEL_CONFIG)
                        .setEncoding(AUDIO_FORMAT)
                        .build()
                )
                .setBufferSizeInBytes(noiseBuffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack?.write(noiseBuffer, 0, noiseBuffer.size)
            audioTrack?.setLoopPoints(0, noiseBuffer.size, -1)
            audioTrack?.play()

            startMaskingTimeMs = System.currentTimeMillis()

            // Fade in from 0 to target volume
            for (step in 0..FADE_STEPS) {
                val volumeAtStep = (step.toFloat() / FADE_STEPS) * cappedVolume
                audioTrack?.setVolume(volumeAtStep)
                delay(FADE_STEP_DELAY_MS)
            }
            audioTrack?.setVolume(cappedVolume)
            currentVolume = cappedVolume
        }
    }

    suspend fun stopMasking(immediate: Boolean = false) {
        withContext(Dispatchers.Default) {
            if (immediate) {
                audioTrack?.stop()
                audioTrack?.release()
                audioTrack = null
                currentVolume = 0f
            } else {
                val elapsedMs = System.currentTimeMillis() - startMaskingTimeMs
                if (elapsedMs < MINIMUM_MASKING_DURATION_MS) {
                    return@withContext  // Do not stop
                }

                // Fade out from current volume to 0
                for (step in 0..FADE_STEPS) {
                    val volumeAtStep = currentVolume * (1f - step.toFloat() / FADE_STEPS)
                    audioTrack?.setVolume(volumeAtStep)
                    delay(FADE_STEP_DELAY_MS)
                }

                audioTrack?.stop()
                audioTrack?.release()
                audioTrack = null
                currentVolume = 0f
            }
        }
    }

    suspend fun setVolume(volume: Float, maxVolume: Float) {
        withContext(Dispatchers.Default) {
            val cappedVolume = volume.coerceAtMost(maxVolume)
            audioTrack?.setVolume(cappedVolume)
            currentVolume = cappedVolume
        }
    }

    fun isPlaying(): Boolean {
        return audioTrack != null && audioTrack?.playState == AudioTrack.PLAYSTATE_PLAYING
    }

    override fun close() {
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
        currentVolume = 0f
    }
}
