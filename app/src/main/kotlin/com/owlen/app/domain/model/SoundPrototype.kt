package com.owlen.app.domain.model

import kotlinx.serialization.Serializable

/**
 * A user-enrolled custom disturbance: the mean YAMNet embedding of the
 * enrollment recordings, L2-normalized at build time so live matching can use
 * a plain dot product as cosine similarity.
 */
@Serializable
data class SoundPrototype(
    val id: String,
    val name: String,
    val embedding: FloatArray,
    val threshold: Float,
    val sampleCount: Int,
    val createdAtMs: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SoundPrototype) return false
        return id == other.id &&
            name == other.name &&
            embedding.contentEquals(other.embedding) &&
            threshold == other.threshold &&
            sampleCount == other.sampleCount &&
            createdAtMs == other.createdAtMs
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + embedding.contentHashCode()
        result = 31 * result + threshold.hashCode()
        result = 31 * result + sampleCount
        result = 31 * result + createdAtMs.hashCode()
        return result
    }
}
