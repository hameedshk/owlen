package com.owlen.app.domain.matcher

import com.owlen.app.domain.model.DetectedEvent
import com.owlen.app.domain.model.EventClass
import com.owlen.app.domain.model.SoundPrototype
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

class EventArbiterTest {

    private lateinit var arbiter: EventArbiter

    private val match = CustomSoundMatcher.CustomMatch(
        prototype = SoundPrototype(
            id = "id",
            name = "Bedroom door",
            embedding = floatArrayOf(1f, 0f),
            threshold = 0.8f,
            sampleCount = 3,
            createdAtMs = 0L
        ),
        similarity = 0.92f
    )

    @Before
    fun setup() {
        arbiter = EventArbiter()
    }

    private fun event(eventClass: EventClass, confidence: Float = 0.9f) = DetectedEvent(
        eventClass = eventClass,
        confidence = confidence,
        isSafetyEvent = eventClass.isSafetyEvent,
        timestampMs = 42L
    )

    @Test
    fun testSmokeAlarmWinsOverCustomMatch() {
        val detected = event(EventClass.SMOKE_ALARM)
        val result = arbiter.arbitrate(detected, match)
        assertSame(detected, result)
    }

    @Test
    fun testBabyCryWinsOverCustomMatch() {
        val detected = event(EventClass.BABY_CRY)
        val result = arbiter.arbitrate(detected, match)
        assertSame(detected, result)
    }

    @Test
    fun testCustomMatchWinsOverNonSafetyClass() {
        val detected = event(EventClass.DOG_BARKING)
        val result = arbiter.arbitrate(detected, match)
        assertEquals(EventClass.CUSTOM, result.eventClass)
        assertEquals("Bedroom door", result.customLabel)
        assertEquals(0.92f, result.confidence, 0.0001f)
        assertFalse(result.isSafetyEvent)
        assertEquals(42L, result.timestampMs)
    }

    @Test
    fun testCustomMatchWinsOverUnknown() {
        val detected = event(EventClass.UNKNOWN, confidence = 0.1f)
        val result = arbiter.arbitrate(detected, match)
        assertEquals(EventClass.CUSTOM, result.eventClass)
    }

    @Test
    fun testNoMatchPassesDetectedEventThrough() {
        val detected = event(EventClass.MOTORCYCLE)
        val result = arbiter.arbitrate(detected, null)
        assertSame(detected, result)
    }

    @Test
    fun testConfidenceIsClampedToOne() {
        val overconfident = match.copy(similarity = 1.3f)
        val result = arbiter.arbitrate(event(EventClass.UNKNOWN), overconfident)
        assertEquals(1f, result.confidence, 0.0001f)
    }
}
