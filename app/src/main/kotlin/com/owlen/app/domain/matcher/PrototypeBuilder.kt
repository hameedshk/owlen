package com.owlen.app.domain.matcher

import com.owlen.app.domain.model.SoundPrototype

/**
 * Builds a [SoundPrototype] from the embeddings of the enrollment takes.
 *
 * The takes must be self-consistent (the user made the same sound each time);
 * otherwise the prototype would match too broadly. The match threshold is
 * derived from how tightly the takes agree, clamped to a conservative range so
 * night-time false positives stay rare.
 */
class PrototypeBuilder {

    sealed interface BuildResult {
        data class Success(val prototype: SoundPrototype) : BuildResult
        data class TooInconsistent(val minPairwiseSimilarity: Float) : BuildResult
    }

    fun build(
        id: String,
        name: String,
        embeddings: List<FloatArray>,
        nowMs: Long
    ): BuildResult {
        require(embeddings.isNotEmpty()) { "At least one enrollment embedding required" }

        val minPairwise = minPairwiseSimilarity(embeddings)
        if (minPairwise < MIN_CONSISTENCY) {
            return BuildResult.TooInconsistent(minPairwise)
        }

        val size = embeddings.first().size
        val mean = FloatArray(size)
        for (embedding in embeddings) {
            for (i in 0 until size) mean[i] += embedding[i]
        }
        val count = embeddings.size.toFloat()
        for (i in 0 until size) mean[i] = mean[i] / count

        val norm = CustomSoundMatcher.l2Norm(mean)
        if (norm > 0f) {
            for (i in 0 until size) mean[i] /= norm
        }

        val threshold = (minPairwise - THRESHOLD_MARGIN)
            .coerceIn(CustomSoundMatcher.MIN_THRESHOLD, CustomSoundMatcher.MAX_THRESHOLD)

        return BuildResult.Success(
            SoundPrototype(
                id = id,
                name = name,
                embedding = mean,
                threshold = threshold,
                sampleCount = embeddings.size,
                createdAtMs = nowMs
            )
        )
    }

    /**
     * Picks the enrollment window most likely to contain the target sound:
     * the one with the highest RMS level.
     */
    fun selectBest(candidates: List<Pair<Float, FloatArray>>): FloatArray {
        require(candidates.isNotEmpty()) { "No candidate windows" }
        return candidates.maxByOrNull { it.first }!!.second
    }

    private fun minPairwiseSimilarity(embeddings: List<FloatArray>): Float {
        if (embeddings.size < 2) return 1f
        var min = Float.MAX_VALUE
        for (i in embeddings.indices) {
            for (j in i + 1 until embeddings.size) {
                val sim = CustomSoundMatcher.cosineSimilarity(embeddings[i], embeddings[j])
                if (sim < min) min = sim
            }
        }
        return min
    }

    companion object {
        const val MIN_CONSISTENCY = 0.60f
        const val THRESHOLD_MARGIN = 0.05f
    }
}
