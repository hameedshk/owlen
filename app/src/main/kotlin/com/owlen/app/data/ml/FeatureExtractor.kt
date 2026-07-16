package com.owlen.app.data.ml

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Runs the bundled YAMNet classification model (CPU only) on a ~0.975s PCM
 * window and returns the raw scores for all 521 AudioSet classes.
 *
 * The bundled yamnet.tflite is the TF Hub classification variant: input is a
 * float32 waveform of 15600 samples at 16kHz, output is [521] scores in 0..1.
 */
class FeatureExtractor(context: Context) : AutoCloseable {
    companion object {
        const val INPUT_SAMPLES = 15600
        const val NUM_CLASSES = 521
    }

    private val interpreter: Interpreter
    private val inputSamples: Int
    private val numClasses: Int
    private val inputBuffer: ByteBuffer
    private val outputBuffer: ByteBuffer

    init {
        try {
            val modelBytes = context.assets.open("yamnet.tflite").use { it.readBytes() }
            val byteBuffer = ByteBuffer.allocateDirect(modelBytes.size).apply {
                order(ByteOrder.nativeOrder())
                put(modelBytes)
                rewind()
            }
            val options = Interpreter.Options().setNumThreads(2)
            interpreter = Interpreter(byteBuffer, options)
        } catch (e: Exception) {
            throw IllegalStateException("yamnet.tflite not found in assets", e)
        }

        // Read the real tensor shapes so we don't depend on a specific model layout
        inputSamples = interpreter.getInputTensor(0).shape().fold(1) { acc, dim -> acc * maxOf(dim, 1) }
        numClasses = interpreter.getOutputTensor(0).shape().fold(1) { acc, dim -> acc * maxOf(dim, 1) }

        inputBuffer = ByteBuffer.allocateDirect(inputSamples * 4).order(ByteOrder.nativeOrder())
        outputBuffer = ByteBuffer.allocateDirect(numClasses * 4).order(ByteOrder.nativeOrder())
    }

    suspend fun extract(pcmWindow: ShortArray): FloatArray {
        if (pcmWindow.size != inputSamples) {
            throw IllegalArgumentException(
                "PCM window must be $inputSamples samples, got ${pcmWindow.size}"
            )
        }

        return withContext(Dispatchers.IO) {
            synchronized(interpreter) {
                inputBuffer.rewind()
                for (sample in pcmWindow) {
                    inputBuffer.putFloat((sample / 32768.0f).coerceIn(-1.0f, 1.0f))
                }
                inputBuffer.rewind()
                outputBuffer.rewind()

                interpreter.run(inputBuffer, outputBuffer)

                outputBuffer.rewind()
                FloatArray(numClasses) { outputBuffer.float }
            }
        }
    }

    override fun close() {
        interpreter.close()
    }
}
