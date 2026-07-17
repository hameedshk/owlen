package com.owlen.app.domain.matcher

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PrototypeBuilderTest {

    private lateinit var builder: PrototypeBuilder

    @Before
    fun setup() {
        builder = PrototypeBuilder()
    }

    @Test
    fun testIdenticalEmbeddingsBuildNormalizedMeanPrototype() {
        val embeddings = listOf(
            floatArrayOf(3f, 4f),
            floatArrayOf(3f, 4f),
            floatArrayOf(3f, 4f)
        )
        val result = builder.build("id", "Door", embeddings, 123L)
        assertTrue(result is PrototypeBuilder.BuildResult.Success)
        val prototype = (result as PrototypeBuilder.BuildResult.Success).prototype

        // Mean (3,4) normalized → (0.6, 0.8)
        assertEquals(0.6f, prototype.embedding[0], 0.0001f)
        assertEquals(0.8f, prototype.embedding[1], 0.0001f)
        assertEquals(1f, CustomSoundMatcher.l2Norm(prototype.embedding), 0.0001f)
        assertEquals("id", prototype.id)
        assertEquals("Door", prototype.name)
        assertEquals(3, prototype.sampleCount)
        assertEquals(123L, prototype.createdAtMs)
    }

    @Test
    fun testIdenticalEmbeddingsGetMaxThreshold() {
        val embeddings = List(3) { floatArrayOf(1f, 2f, 3f) }
        val result = builder.build("id", "x", embeddings, 0L)
        val prototype = (result as PrototypeBuilder.BuildResult.Success).prototype
        // minPairwise = 1.0 → 1.0 - 0.05 = 0.95, clamped to 0.90
        assertEquals(CustomSoundMatcher.MAX_THRESHOLD, prototype.threshold, 0.0001f)
    }

    @Test
    fun testLooselyConsistentEmbeddingsGetMinThreshold() {
        // Pairwise similarity ~0.707 (45°) — above 0.60 consistency floor,
        // and 0.707 - 0.05 = 0.657 clamps up to MIN_THRESHOLD
        val embeddings = listOf(
            floatArrayOf(1f, 0f),
            floatArrayOf(1f, 1f)
        )
        val result = builder.build("id", "x", embeddings, 0L)
        val prototype = (result as PrototypeBuilder.BuildResult.Success).prototype
        assertEquals(CustomSoundMatcher.MIN_THRESHOLD, prototype.threshold, 0.0001f)
    }

    @Test
    fun testThresholdDerivedFromMinPairwiseSimilarity() {
        // cos = 0.868 between (1,0) and (0.868, 0.497): inside the clamp range
        val embeddings = listOf(
            floatArrayOf(1f, 0f),
            floatArrayOf(0.868f, 0.4967f)
        )
        val result = builder.build("id", "x", embeddings, 0L)
        val prototype = (result as PrototypeBuilder.BuildResult.Success).prototype
        assertEquals(0.868f - 0.05f, prototype.threshold, 0.001f)
    }

    @Test
    fun testInconsistentEmbeddingsAreRejected() {
        // Orthogonal takes: similarity 0 < 0.60
        val embeddings = listOf(
            floatArrayOf(1f, 0f),
            floatArrayOf(0f, 1f),
            floatArrayOf(1f, 0f)
        )
        val result = builder.build("id", "x", embeddings, 0L)
        assertTrue(result is PrototypeBuilder.BuildResult.TooInconsistent)
        val inconsistent = result as PrototypeBuilder.BuildResult.TooInconsistent
        assertEquals(0f, inconsistent.minPairwiseSimilarity, 0.0001f)
    }

    @Test
    fun testMinPairwiseIsTheWorstPair() {
        // Two identical + one orthogonal: min pairwise is 0 → rejected
        val embeddings = listOf(
            floatArrayOf(1f, 0f),
            floatArrayOf(1f, 0f),
            floatArrayOf(0f, 1f)
        )
        val result = builder.build("id", "x", embeddings, 0L)
        assertTrue(result is PrototypeBuilder.BuildResult.TooInconsistent)
    }

    @Test
    fun testSingleEmbeddingBuildsWithMaxThreshold() {
        val result = builder.build("id", "x", listOf(floatArrayOf(1f, 1f)), 0L)
        val prototype = (result as PrototypeBuilder.BuildResult.Success).prototype
        assertEquals(1, prototype.sampleCount)
        assertEquals(CustomSoundMatcher.MAX_THRESHOLD, prototype.threshold, 0.0001f)
    }

    @Test
    fun testSelectBestPicksHighestRmsWindow() {
        val quiet = floatArrayOf(1f, 0f)
        val loud = floatArrayOf(0f, 1f)
        val medium = floatArrayOf(1f, 1f)
        val selected = builder.selectBest(
            listOf(30f to quiet, 72f to loud, 55f to medium)
        )
        assertTrue(selected.contentEquals(loud))
    }
}
