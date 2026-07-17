package com.owlen.app.domain.matcher

import com.owlen.app.domain.model.SoundPrototype
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import kotlin.math.sqrt

class CustomSoundMatcherTest {

    private lateinit var matcher: CustomSoundMatcher

    @Before
    fun setup() {
        matcher = CustomSoundMatcher()
    }

    private fun prototype(
        embedding: FloatArray,
        threshold: Float = 0.75f,
        id: String = "id",
        name: String = "Door"
    ) = SoundPrototype(
        id = id,
        name = name,
        embedding = normalized(embedding),
        threshold = threshold,
        sampleCount = 3,
        createdAtMs = 0L
    )

    private fun normalized(v: FloatArray): FloatArray {
        val norm = CustomSoundMatcher.l2Norm(v)
        return if (norm == 0f) v else FloatArray(v.size) { v[it] / norm }
    }

    @Test
    fun testCosineSimilarityOfIdenticalVectorsIsOne() {
        val v = floatArrayOf(1f, 2f, 3f)
        assertEquals(1f, CustomSoundMatcher.cosineSimilarity(v, v), 0.0001f)
    }

    @Test
    fun testCosineSimilarityOfOrthogonalVectorsIsZero() {
        val a = floatArrayOf(1f, 0f)
        val b = floatArrayOf(0f, 1f)
        assertEquals(0f, CustomSoundMatcher.cosineSimilarity(a, b), 0.0001f)
    }

    @Test
    fun testCosineSimilarityOfOppositeVectorsIsMinusOne() {
        val a = floatArrayOf(1f, 2f)
        val b = floatArrayOf(-1f, -2f)
        assertEquals(-1f, CustomSoundMatcher.cosineSimilarity(a, b), 0.0001f)
    }

    @Test
    fun testCosineSimilarityOfZeroVectorIsZero() {
        val a = floatArrayOf(0f, 0f)
        val b = floatArrayOf(1f, 2f)
        assertEquals(0f, CustomSoundMatcher.cosineSimilarity(a, b), 0.0001f)
    }

    @Test
    fun testMatchWithEmptyPrototypeListReturnsNull() {
        assertNull(matcher.match(floatArrayOf(1f, 0f), emptyList()))
    }

    @Test
    fun testUnnormalizedLiveEmbeddingStillMatches() {
        val proto = prototype(floatArrayOf(1f, 1f, 0f))
        // Same direction, much larger magnitude — cosine is scale-invariant
        val live = floatArrayOf(50f, 50f, 0f)
        val match = matcher.match(live, listOf(proto))
        assertNotNull(match)
        assertEquals(1f, match!!.similarity, 0.0001f)
    }

    @Test
    fun testSimilarityAtThresholdMatches() {
        val proto = prototype(floatArrayOf(1f, 0f), threshold = 0.8f)
        // cos(angle) = 0.8 exactly: (0.8, 0.6) against (1, 0)
        val live = floatArrayOf(0.8f, 0.6f)
        val match = matcher.match(live, listOf(proto))
        assertNotNull(match)
        assertEquals(0.8f, match!!.similarity, 0.001f)
    }

    @Test
    fun testSimilarityBelowThresholdDoesNotMatch() {
        val proto = prototype(floatArrayOf(1f, 0f), threshold = 0.9f)
        val live = floatArrayOf(0.8f, 0.6f) // similarity 0.8 < 0.9
        assertNull(matcher.match(live, listOf(proto)))
    }

    @Test
    fun testBestOfMultiplePrototypesWins() {
        val close = prototype(floatArrayOf(1f, 0.1f), id = "close", name = "Close")
        val far = prototype(floatArrayOf(1f, 1f), id = "far", name = "Far")
        val live = floatArrayOf(1f, 0f)
        val match = matcher.match(live, listOf(far, close))
        assertNotNull(match)
        assertEquals("close", match!!.prototype.id)
    }

    @Test
    fun testMismatchedEmbeddingSizeIsSkipped() {
        val proto = prototype(floatArrayOf(1f, 0f, 0f))
        assertNull(matcher.match(floatArrayOf(1f, 0f), listOf(proto)))
    }

    @Test
    fun testZeroLiveEmbeddingReturnsNull() {
        val proto = prototype(floatArrayOf(1f, 0f))
        assertNull(matcher.match(floatArrayOf(0f, 0f), listOf(proto)))
    }

    @Test
    fun testL2Norm() {
        assertEquals(5f, CustomSoundMatcher.l2Norm(floatArrayOf(3f, 4f)), 0.0001f)
        assertEquals(sqrt(2f), CustomSoundMatcher.l2Norm(floatArrayOf(1f, 1f)), 0.0001f)
    }
}
