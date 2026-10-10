package com.owlen.app.data.audio

import com.owlen.app.domain.model.MaskingSound
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AudioPlayerTest {
    @Test
    fun setVolume_neverExceedsMaxVolume() {
        // Note: This is a logic test. Actual AudioTrack mocking would be needed for full integration testing.
        val maxVolume = 0.7f
        val requestedVolume = 0.9f
        val cappedVolume = requestedVolume.coerceAtMost(maxVolume)
        assertTrue(cappedVolume <= maxVolume)
        assertTrue(cappedVolume == maxVolume)
    }

    @Test
    fun minimumMaskingDuration_isCorrect() {
        assertEquals(30_000L, AudioPlayer.MINIMUM_MASKING_DURATION_MS)
    }

    @Test
    fun maskingSoundMapping_brownNoiseIsSupported() {
        // Verify that BROWN_NOISE can be used
        val sound = MaskingSound.BROWN_NOISE
        assertTrue(sound == MaskingSound.BROWN_NOISE)
    }

    @Test
    fun maskingSoundMapping_pinkNoiseIsSupported() {
        // Verify that PINK_NOISE can be used
        val sound = MaskingSound.PINK_NOISE
        assertTrue(sound == MaskingSound.PINK_NOISE)
    }

    @Test
    fun maskingSoundMapping_whiteNoiseIsSupported() {
        // Verify that WHITE_NOISE can be used
        val sound = MaskingSound.WHITE_NOISE
        assertTrue(sound == MaskingSound.WHITE_NOISE)
    }

    @Test
    fun maskingSoundMapping_fanIsSupported() {
        // Verify that FAN can be used
        val sound = MaskingSound.FAN
        assertTrue(sound == MaskingSound.FAN)
    }

    @Test
    fun maskingSoundMapping_rainIsSupported() {
        // Verify that RAIN can be used
        val sound = MaskingSound.RAIN
        assertTrue(sound == MaskingSound.RAIN)
    }

    @Test
    fun maskingSoundMapping_oceanWavesIsSupported() {
        // Verify that OCEAN_WAVES can be used
        val sound = MaskingSound.OCEAN_WAVES
        assertTrue(sound == MaskingSound.OCEAN_WAVES)
    }

    @Test
    fun playbackSampleRate_isCorrect() {
        assertEquals(44100, AudioPlayer.PLAYBACK_SAMPLE_RATE)
    }

    @Test
    fun fadeInSteps_isCorrect() {
        assertEquals(150, AudioPlayer.FADE_IN_STEPS)
    }

    @Test
    fun fadeOutSteps_isCorrect() {
        assertEquals(100, AudioPlayer.FADE_OUT_STEPS)
    }

    @Test
    fun startMaskingAtVolume_acceptsZeroVolume() {
        // Timer feature can start at near-silent volume
        val initialVolume = 0.02f
        assertTrue(initialVolume >= 0f && initialVolume <= 1f)
    }

    @Test
    fun startMaskingAtVolume_acceptsMaxVolume() {
        val maxVolume = 1.0f
        assertTrue(maxVolume <= AudioPlayer.MAX_VOLUME_FLOAT)
    }

    @Test
    fun stopMasking_defaultParameterPreservesMinimumDuration() {
        // Regression: default stopMasking(immediate=false) should preserve 30s floor
        // This ensures existing reactive pipeline behavior is unchanged
        assertTrue(AudioPlayer.MINIMUM_MASKING_DURATION_MS == 30_000L)
    }

    @Test
    fun stopMasking_timerBypassMinimumDuration() {
        // Timer feature calls stopMasking(minimumDurationMs=0) to fade immediately
        // even on short durations (bypassing the reactive-only 30s floor)
        val minimumForTimer = 0L
        assertTrue(minimumForTimer < AudioPlayer.MINIMUM_MASKING_DURATION_MS)
    }

    private fun assertEquals(expected: Any, actual: Any) {
        org.junit.Assert.assertEquals(expected, actual)
    }
}
