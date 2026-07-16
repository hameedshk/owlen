package com.owlen.app.data.ml

import com.owlen.app.domain.model.DetectedEvent
import com.owlen.app.domain.model.EventClass
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class EventDetectorTest {

    private val detector = EventDetector()

    private fun scoresWith(vararg indexToScore: Pair<Int, Float>): FloatArray {
        val scores = FloatArray(EventDetector.NUM_CLASSES)
        for ((index, score) in indexToScore) {
            scores[index] = score
        }
        return scores
    }

    @Test
    fun smokeAlarmScoreDetectsSmokeAlarm() = runBlocking {
        // Index 393 = "Smoke detector, smoke alarm" in yamnet_label_list.txt
        val event = detector.detect(scoresWith(393 to 0.8f))
        assertEquals(EventClass.SMOKE_ALARM, event.eventClass)
        assertTrue(event.isSafetyEvent)
        assertEquals(1.0f, event.confidence, 0.001f)
    }

    @Test
    fun fireAlarmAlsoMapsToSmokeAlarm() = runBlocking {
        // Index 394 = "Fire alarm"
        val event = detector.detect(scoresWith(394 to 0.6f))
        assertEquals(EventClass.SMOKE_ALARM, event.eventClass)
        assertTrue(event.isSafetyEvent)
    }

    @Test
    fun babyCryScoreDetectsBabyCry() = runBlocking {
        // Index 20 = "Baby cry, infant cry"
        val event = detector.detect(scoresWith(20 to 0.5f))
        assertEquals(EventClass.BABY_CRY, event.eventClass)
        assertTrue(event.isSafetyEvent)
        assertEquals(1.0f, event.confidence, 0.001f)
    }

    @Test
    fun strongSafetyScoreClearsScorersBypassThreshold() = runBlocking {
        // A raw YAMNet score of 0.35 must produce confidence >= 0.70,
        // the DisturbanceScorer's safety-bypass threshold
        val event = detector.detect(scoresWith(393 to 0.35f))
        assertEquals(EventClass.SMOKE_ALARM, event.eventClass)
        assertTrue(event.confidence >= 0.70f)
    }

    @Test
    fun dogBarkDetectsDogBarking() = runBlocking {
        // Index 70 = "Bark"
        val event = detector.detect(scoresWith(70 to 0.4f))
        assertEquals(EventClass.DOG_BARKING, event.eventClass)
        assertFalse(event.isSafetyEvent)
        assertEquals(0.8f, event.confidence, 0.001f)
    }

    @Test
    fun thunderDetectsThunder() = runBlocking {
        // Index 281 = "Thunder"
        val event = detector.detect(scoresWith(281 to 0.3f))
        assertEquals(EventClass.THUNDER, event.eventClass)
    }

    @Test
    fun rainDetectsRain() = runBlocking {
        // Index 283 = "Rain"
        val event = detector.detect(scoresWith(283 to 0.3f))
        assertEquals(EventClass.RAIN, event.eventClass)
    }

    @Test
    fun highestScoringClassWins() = runBlocking {
        val event = detector.detect(
            scoresWith(70 to 0.3f, 393 to 0.5f, 283 to 0.2f)
        )
        assertEquals(EventClass.SMOKE_ALARM, event.eventClass)
    }

    @Test
    fun allLowScoresReturnUnknown() = runBlocking {
        // Everything below the detection floor
        val event = detector.detect(scoresWith(70 to 0.05f, 393 to 0.04f))
        assertEquals(EventClass.UNKNOWN, event.eventClass)
        assertFalse(event.isSafetyEvent)
    }

    @Test
    fun silenceReturnsUnknown() = runBlocking {
        val event = detector.detect(FloatArray(EventDetector.NUM_CLASSES))
        assertEquals(EventClass.UNKNOWN, event.eventClass)
    }

    @Test(expected = IllegalArgumentException::class)
    fun wrongScoreArraySizeThrows(): Unit = runBlocking {
        detector.detect(FloatArray(100))
        Unit
    }

    @Test
    fun babyCrySetsSafetyEventFlag() {
        assertTrue(EventClass.BABY_CRY.isSafetyEvent)
    }

    @Test
    fun smokeAlarmSetsSafetyEventFlag() {
        assertTrue(EventClass.SMOKE_ALARM.isSafetyEvent)
    }

    @Test
    fun allOtherClassesAreNotSafetyEvents() {
        val nonSafetyClasses = listOf(
            EventClass.GARBAGE_COLLECTION,
            EventClass.HUMAN_SHOUTING,
            EventClass.MOTORCYCLE,
            EventClass.DOG_BARKING,
            EventClass.CONSTRUCTION,
            EventClass.RAIN,
            EventClass.THUNDER,
            EventClass.UNKNOWN
        )

        for (eventClass in nonSafetyClasses) {
            assertFalse(
                "${eventClass.name} should not be a safety event",
                eventClass.isSafetyEvent
            )
        }
    }

    @Test
    fun onlyTwoSafetyEventsExist() {
        assertEquals(2, EventClass.values().count { it.isSafetyEvent })
    }

    @Test
    fun detectedEventContainsIsSafetyEventFromClass() {
        val babyCryEvent = DetectedEvent(
            eventClass = EventClass.BABY_CRY,
            confidence = 0.95f,
            isSafetyEvent = EventClass.BABY_CRY.isSafetyEvent,
            timestampMs = System.currentTimeMillis()
        )
        assertTrue(babyCryEvent.isSafetyEvent)
    }
}
