package com.owlen.app.data.ml

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Scores and embedding produced by one YAMNet inference over a ~0.975s window.
 * The arrays are private copies — safe to hold across subsequent extractions.
 */
class ExtractionResult(val scores: FloatArray, val embedding: FloatArray)

/**
 * Runs the bundled YAMNet model (CPU only) on a ~0.975s PCM window.
 *
 * The bundled yamnet.tflite is the TF Hub multi-output variant: input is a
 * float32 waveform of 15600 samples at 16kHz; outputs are [1, 521] class
 * scores, a [1, 1024] embedding, and a log-mel spectrogram (unused). Output
 * indices are resolved by tensor shape at init rather than assumed by order.
 */
class FeatureExtractor(context: Context) : AutoCloseable {
    companion object {
        const val INPUT_SAMPLES = 15600
        const val NUM_CLASSES = 521
        const val EMBEDDING_SIZE = 1024
    }

    private val interpreter: Interpreter
    private val scoresOutputIndex: Int
    private val embeddingOutputIndex: Int
    private val inputBuffer: ByteBuffer
    private val scoresOutput = Array(1) { FloatArray(NUM_CLASSES) }
    private val embeddingOutput = Array(1) { FloatArray(EMBEDDING_SIZE) }

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

        // The canonical model has a fixed [15600] input; resize defensively in
        // case a dynamic-input export is bundled instead
        val inputElements = interpreter.getInputTensor(0).shape()
            .fold(1) { acc, dim -> acc * maxOf(dim, 1) }
        if (inputElements != INPUT_SAMPLES) {
            interpreter.resizeInput(0, intArrayOf(INPUT_SAMPLES))
            interpreter.allocateTensors()
        }

        // Resolve output tensors by shape, never by index order — a wrong index
        // here would silently feed embeddings into the class-score detector
        var scoresIndex = -1
        var embeddingIndex = -1
        for (i in 0 until interpreter.outputTensorCount) {
            when (interpreter.getOutputTensor(i).shape().last()) {
                NUM_CLASSES -> scoresIndex = i
                EMBEDDING_SIZE -> embeddingIndex = i
            }
        }
        check(scoresIndex >= 0) { "yamnet.tflite has no [.., $NUM_CLASSES] scores output" }
        check(embeddingIndex >= 0) { "yamnet.tflite has no [.., $EMBEDDING_SIZE] embedding output" }
        scoresOutputIndex = scoresIndex
        embeddingOutputIndex = embeddingIndex

        inputBuffer = ByteBuffer.allocateDirect(INPUT_SAMPLES * 4).order(ByteOrder.nativeOrder())
    }

    suspend fun extract(pcmWindow: ShortArray): ExtractionResult {
        if (pcmWindow.size != INPUT_SAMPLES) {
            throw IllegalArgumentException(
                "PCM window must be $INPUT_SAMPLES samples, got ${pcmWindow.size}"
            )
        }

        return withContext(Dispatchers.IO) {
            synchronized(interpreter) {
                inputBuffer.rewind()
                for (sample in pcmWindow) {
                    inputBuffer.putFloat((sample / 32768.0f).coerceIn(-1.0f, 1.0f))
                }
                inputBuffer.rewind()

                interpreter.runForMultipleInputsOutputs(
                    arrayOf(inputBuffer),
                    mapOf(
                        scoresOutputIndex to scoresOutput,
                        embeddingOutputIndex to embeddingOutput
                    )
                )

                // Copy out — the backing arrays are reused on the next inference
                ExtractionResult(scoresOutput[0].copyOf(), embeddingOutput[0].copyOf())
            }
        }
    }

    override fun close() {
        interpreter.close()
    }
}
