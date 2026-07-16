package com.owlen.app.data.audio

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class AudioCaptureTest {
    @Test
    fun windowSize_calculatedCorrectly() {
        val windowSize = AudioCapture.FRAME_SIZE * AudioCapture.WINDOW_FRAMES
        assertEquals(15600, windowSize)
        assertEquals(AudioCapture.WINDOW_SIZE, windowSize)
    }

    @Test
    fun sampleRate_isCorrect() {
        assertEquals(16000, AudioCapture.SAMPLE_RATE)
    }

    @Test
    fun isRecording_returnsFalseBeforeStart() {
        val capture = AudioCapture()
        assertFalse(capture.isRecording())
        capture.close()
    }

    @Test
    fun frameSize_isCorrect() {
        assertEquals(780, AudioCapture.FRAME_SIZE)
    }

    @Test
    fun windowFrames_isCorrect() {
        assertEquals(20, AudioCapture.WINDOW_FRAMES)
    }

    @Test
    fun windowDuration_isYamnetInputLength() {
        // 15600 samples at 16kHz = 0.975s, the YAMNet classification input length
        val durationMs = AudioCapture.WINDOW_SIZE * 1000L / AudioCapture.SAMPLE_RATE
        assertEquals(975L, durationMs)
    }
}
