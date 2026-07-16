package com.owlen.app.domain.scorer

import com.owlen.app.domain.model.DetectedEvent
import com.owlen.app.domain.model.EventClass
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.Sensitivity
import com.owlen.app.domain.model.SleepSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DisturbanceScorerTest {
    private lateinit var scorer: DisturbanceScorer
    private lateinit var defaultSettings: SleepSettings

    @Before
    fun setup() {
        scorer = DisturbanceScorer()
        defaultSettings = SleepSettings.default()
    }

    @Test
    fun testBabyCryWithHighConfidenceReturnsSafetyBypass() {
        val event = DetectedEvent(
            eventClass = EventClass.BABY_CRY,
            confidence = 0.85f,
            isSafetyEvent = true,
            timestampMs = System.currentTimeMillis()
        )
        val result = scorer.score(event, 75f, 23, defaultSettings)
        assertTrue(result.isSafetyBypass)
        assertEquals(100, result.score)
    }

    @Test
    fun testBabyCryWithLowConfidenceDoesNotReturnSafetyBypass() {
        val event = DetectedEvent(
            eventClass = EventClass.BABY_CRY,
            confidence = 0.65f,
            isSafetyEvent = true,
            timestampMs = System.currentTimeMillis()
        )
        val result = scorer.score(event, 75f, 23, defaultSettings)
        assertFalse(result.isSafetyBypass)
    }

    @Test
    fun testSmokeAlarmWithHighConfidenceReturnsSafetyBypass() {
        val event = DetectedEvent(
            eventClass = EventClass.SMOKE_ALARM,
            confidence = 0.75f,
            isSafetyEvent = true,
            timestampMs = System.currentTimeMillis()
        )
        val result = scorer.score(event, 80f, 23, defaultSettings)
        assertTrue(result.isSafetyBypass)
    }

    @Test
    fun testRainWithRainBehaviourEnabledReturnsZeroScore() {
        val event = DetectedEvent(
            eventClass = EventClass.RAIN,
            confidence = 0.9f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        val settings = defaultSettings.copy(rainBehaviourEnabled = true)
        val result = scorer.score(event, 70f, 23, settings)
        assertEquals(0, result.score)
    }

    @Test
    fun testRainWithRainBehaviourDisabledUsesFormula() {
        val event = DetectedEvent(
            eventClass = EventClass.RAIN,
            confidence = 0.9f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        val settings = defaultSettings.copy(rainBehaviourEnabled = false)
        val result = scorer.score(event, 70f, 23, settings)
        assertTrue(result.score > 0)
    }

    @Test
    fun testScoreClampedToZeroWhenDBSPLBelowFloor() {
        val event = DetectedEvent(
            eventClass = EventClass.DOG_BARKING,
            confidence = 0.8f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        val settings = defaultSettings.copy(soundLevelFloor = 80f)
        val result = scorer.score(event, 40f, 23, settings)
        assertEquals(0, result.normSoundLevel)
    }

    @Test
    fun testScoreClampedTo100AtMaximumInput() {
        val event = DetectedEvent(
            eventClass = EventClass.HUMAN_SHOUTING,
            confidence = 0.9f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        val result = scorer.score(event, 100f, 23, defaultSettings)
        // Score = (90 × 0.5) + (100 × 0.3) + (100 × 0.2) = 45 + 30 + 20 = 95
        assertEquals(95, result.score)
    }

    @Test
    fun testTimeWeightIs100WithinSleepWindow() {
        val event = DetectedEvent(
            eventClass = EventClass.DOG_BARKING,
            confidence = 0.7f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        // 23:00 is within 22:00-06:00 sleep window
        val result = scorer.score(event, 70f, 23, defaultSettings)
        assertEquals(100, result.timeWeight)
    }

    @Test
    fun testTimeWeightIs30OutsideSleepWindow() {
        val event = DetectedEvent(
            eventClass = EventClass.DOG_BARKING,
            confidence = 0.7f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        // 12:00 is outside 22:00-06:00 sleep window
        val result = scorer.score(event, 70f, 12, defaultSettings)
        assertEquals(30, result.timeWeight)
    }

    @Test
    fun testAllEventWeightsAppliedCorrectly() {
        val testCases = mapOf(
            EventClass.GARBAGE_COLLECTION to 80,
            EventClass.HUMAN_SHOUTING to 90,
            EventClass.MOTORCYCLE to 75,
            EventClass.DOG_BARKING to 60,
            EventClass.CONSTRUCTION to 85,
            EventClass.RAIN to 20,
            EventClass.THUNDER to 50,
            EventClass.UNKNOWN to 40
        )

        testCases.forEach { (eventClass, expectedWeight) ->
            val event = DetectedEvent(
                eventClass = eventClass,
                confidence = 0.7f,
                isSafetyEvent = false,
                timestampMs = System.currentTimeMillis()
            )
            val result = scorer.score(event, 70f, 23, defaultSettings)
            assertEquals("Event $eventClass should have weight $expectedWeight", expectedWeight, result.eventWeight)
        }
    }
}
