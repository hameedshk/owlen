package com.owlen.app.domain.matcher

import com.owlen.app.domain.model.SoundPrototype
import kotlin.math.sqrt

/**
 * Matches a live YAMNet embedding against user-enrolled sound prototypes via
 * cosine similarity. Prototypes are stored L2-normalized, so after normalizing
 * the live embedding once, similarity is a plain dot product per prototype.
 */
class CustomSoundMatcher {

    data class CustomMatch(val prototype: SoundPrototype, val similarity: Float)

    fun match(embedding: FloatArray, prototypes: List<SoundPrototype>): CustomMatch? {
        if (prototypes.isEmpty()) return null

        val norm = l2Norm(embedding)
        if (norm == 0f) return null

        var best: CustomMatch? = null
        for (prototype in prototypes) {
            if (prototype.embedding.size != embedding.size) continue
            var dot = 0f
            for (i in embedding.indices) {
                dot += embedding[i] * prototype.embedding[i]
            }
            val similarity = dot / norm
            if (similarity >= prototype.threshold &&
                similarity > (best?.similarity ?: Float.NEGATIVE_INFINITY)
            ) {
                best = CustomMatch(prototype, similarity)
            }
        }
        return best
    }

    companion object {
        const val MIN_THRESHOLD = 0.75f
        const val MAX_THRESHOLD = 0.90f

        fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
            require(a.size == b.size) { "Vector sizes differ: ${a.size} vs ${b.size}" }
            var dot = 0f
            for (i in a.indices) dot += a[i] * b[i]
            val normProduct = l2Norm(a) * l2Norm(b)
            return if (normProduct == 0f) 0f else dot / normProduct
        }

        fun l2Norm(v: FloatArray): Float {
            var sum = 0f
            for (x in v) sum += x * x
            return sqrt(sum)
        }
    }
}
