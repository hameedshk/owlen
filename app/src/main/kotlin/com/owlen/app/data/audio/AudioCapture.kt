package com.owlen.app.data.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.Closeable

class AudioCapture : Closeable {
    companion object {
        const val SAMPLE_RATE = 16000
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        const val FRAME_SIZE = 780          // 48.75ms per frame at 16kHz
        const val WINDOW_FRAMES = 20        // 20 frames = 0.975s window
        const val WINDOW_SIZE = 15600       // FRAME_SIZE × WINDOW_FRAMES — YAMNet input size
    }

    // Hot flow: a single AudioRecord feeds all collectors. DROP_OLDEST keeps the
    // pipeline real-time if inference falls behind capture.
    private val _audioFlow = MutableSharedFlow<ShortArray>(
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val audioFlow: SharedFlow<ShortArray> = _audioFlow

    private var recordingJob: Job? = null

    fun start(scope: CoroutineScope) {
        if (recordingJob?.isActive == true) return
        recordingJob = scope.launch(Dispatchers.IO) {
            captureLoop()
        }
    }

    private suspend fun CoroutineScope.captureLoop() {
        val minBuffer = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
        if (minBuffer == AudioRecord.ERROR || minBuffer == AudioRecord.ERROR_BAD_VALUE) {
            throw IllegalStateException("AudioRecord buffer size error")
        }

        val actualBuffer = maxOf(FRAME_SIZE * 4, minBuffer)
        val record = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            actualBuffer
        )

        if (record.state != AudioRecord.STATE_INITIALIZED) {
            record.release()
            throw IllegalStateException("AudioRecord failed to initialise")
        }

        try {
            record.startRecording()

            val frameBuffer = ShortArray(FRAME_SIZE)
            val windowBuffer = ShortArray(WINDOW_SIZE)
            var windowIndex = 0

            while (isActive) {
                val read = record.read(frameBuffer, 0, FRAME_SIZE)
                if (read == FRAME_SIZE) {
                    frameBuffer.copyInto(windowBuffer, windowIndex * FRAME_SIZE)
                    windowIndex++
                    if (windowIndex >= WINDOW_FRAMES) {
                        _audioFlow.emit(windowBuffer.copyOf())
                        windowIndex = 0
                    }
                }
            }
        } finally {
            try {
                record.stop()
            } catch (_: IllegalStateException) {
                // Already stopped
            }
            record.release()
        }
    }

    fun stop() {
        recordingJob?.cancel()
        recordingJob = null
    }

    fun isRecording(): Boolean {
        return recordingJob?.isActive == true
    }

    override fun close() {
        stop()
    }
}
