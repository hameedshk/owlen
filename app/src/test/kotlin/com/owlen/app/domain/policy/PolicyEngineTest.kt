package com.owlen.app.domain.policy

import com.owlen.app.domain.model.DetectedEvent
import com.owlen.app.domain.model.DisturbanceResult
import com.owlen.app.domain.model.EventClass
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.PolicyAction
import com.owlen.app.domain.model.Sensitivity
import com.owlen.app.domain.model.SleepSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PolicyEngineTest {
    private lateinit var engine: PolicyEngine
    private lateinit var defaultSettings: SleepSettings

    @Before
    fun setup() {
        engine = PolicyEngine()
        defaultSettings = SleepSettings.default()
    }

    @Test
    fun testSafetyBypassAlwaysReturnsAlert() {
        val event = DetectedEvent(
            eventClass = EventClass.BABY_CRY,
            confidence = 0.9f,
            isSafetyEvent = true,
            timestampMs = System.currentTimeMillis()
        )
        val result = DisturbanceResult(score = 50, eventWeight = 100, normSoundLevel = 50, timeWeight = 100, isSafetyBypass = true)
        val action = engine.evaluate(result, event, System.currentTimeMillis(), defaultSettings, false, 0)

        assertTrue(action is PolicyAction.SafetyAlert)
        assertEquals(EventClass.BABY_CRY, (action as PolicyAction.SafetyAlert).event)
    }

    @Test
    fun testWakeTimeWithin90SecondsStopsActiveMasking() {
        val currentTimeMs = 6 * 60 * 60 * 1000L + 30 * 1000L // 06:00:30
        val event = DetectedEvent(
            eventClass = EventClass.DOG_BARKING,
            confidence = 0.7f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        val result = DisturbanceResult(score = 85, eventWeight = 60, normSoundLevel = 80, timeWeight = 100, isSafetyBypass = false)

        // Masking active: wind down for wake-up
        val activeAction = engine.evaluate(result, event, currentTimeMs, defaultSettings, true, 0)
        assertEquals(PolicyAction.StopMasking, activeAction)

        // Not masking: never start new masking this close to wake time
        val idleAction = engine.evaluate(result, event, currentTimeMs, defaultSettings, false, 0)
        assertEquals(PolicyAction.Ignore, idleAction)
    }

    @Test
    fun testAutoStopReturnsStopMaskingWhenConditionsMet() {
        val event = DetectedEvent(
            eventClass = EventClass.DOG_BARKING,
            confidence = 0.7f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        val result = DisturbanceResult(score = 15, eventWeight = 60, normSoundLevel = 20, timeWeight = 100, isSafetyBypass = false)
        val settings = defaultSettings.copy(autoStopDurationMs = 30_000L)
        val action = engine.evaluate(result, event, System.currentTimeMillis(), settings, true, 35_000L)

        assertEquals(PolicyAction.StopMasking, action)
    }

    @Test
    fun testHighSensitivityTriggersAtLowerScores() {
        val event = DetectedEvent(
            eventClass = EventClass.DOG_BARKING,
            confidence = 0.7f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        val settings = defaultSettings.copy(sensitivity = Sensitivity.HIGH)
        val result = DisturbanceResult(score = 70, eventWeight = 60, normSoundLevel = 70, timeWeight = 100, isSafetyBypass = false)
        val action = engine.evaluate(result, event, System.currentTimeMillis(), settings, false, 0)

        // With HIGH (-15 offset), 70 > (80 - 15) = 65, so should start masking
        assertTrue(action is PolicyAction.StartMasking)
    }

    @Test
    fun testLowSensitivityRequiresHigherScores() {
        val event = DetectedEvent(
            eventClass = EventClass.DOG_BARKING,
            confidence = 0.7f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        val settings = defaultSettings.copy(sensitivity = Sensitivity.LOW)
        val result = DisturbanceResult(score = 40, eventWeight = 60, normSoundLevel = 40, timeWeight = 100, isSafetyBypass = false)
        val action = engine.evaluate(result, event, System.currentTimeMillis(), settings, false, 0)

        // With LOW (+15 offset), 40 is not > (30 + 15) = 45, so should ignore
        assertEquals(PolicyAction.Ignore, action)
    }

    @Test
    fun testScoreAt80ThresholdTriggersMaxVolume() {
        val event = DetectedEvent(
            eventClass = EventClass.HUMAN_SHOUTING,
            confidence = 0.8f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        val result = DisturbanceResult(score = 81, eventWeight = 90, normSoundLevel = 80, timeWeight = 100, isSafetyBypass = false)
        val action = engine.evaluate(result, event, System.currentTimeMillis(), defaultSettings, false, 0)

        assertTrue(action is PolicyAction.StartMasking)
        assertEquals(defaultSettings.maxVolume, (action as PolicyAction.StartMasking).volume, 0.001f)
    }

    @Test
    fun testScoreAt60ThresholdTriggers60Percent() {
        val event = DetectedEvent(
            eventClass = EventClass.CONSTRUCTION,
            confidence = 0.8f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        val result = DisturbanceResult(score = 61, eventWeight = 85, normSoundLevel = 60, timeWeight = 100, isSafetyBypass = false)
        val action = engine.evaluate(result, event, System.currentTimeMillis(), defaultSettings, false, 0)

        assertTrue(action is PolicyAction.StartMasking)
        assertEquals(defaultSettings.maxVolume * 0.6f, (action as PolicyAction.StartMasking).volume, 0.001f)
    }

    @Test
    fun testScoreAt30ThresholdTriggers35Percent() {
        val event = DetectedEvent(
            eventClass = EventClass.RAIN,
            confidence = 0.7f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        val result = DisturbanceResult(score = 31, eventWeight = 20, normSoundLevel = 30, timeWeight = 100, isSafetyBypass = false)
        val action = engine.evaluate(result, event, System.currentTimeMillis(), defaultSettings, false, 0)

        assertTrue(action is PolicyAction.StartMasking)
        assertEquals(defaultSettings.maxVolume * 0.35f, (action as PolicyAction.StartMasking).volume, 0.001f)
    }

    @Test
    fun testBelowThresholdsReturnsIgnore() {
        val event = DetectedEvent(
            eventClass = EventClass.UNKNOWN,
            confidence = 0.5f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        val result = DisturbanceResult(score = 25, eventWeight = 40, normSoundLevel = 25, timeWeight = 100, isSafetyBypass = false)
        val action = engine.evaluate(result, event, System.currentTimeMillis(), defaultSettings, false, 0)

        assertEquals(PolicyAction.Ignore, action)
    }

    @Test
    fun testActiveMaskingAdjustsVolumeInsteadOfRestarting() {
        val event = DetectedEvent(
            eventClass = EventClass.HUMAN_SHOUTING,
            confidence = 0.8f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        val result = DisturbanceResult(score = 85, eventWeight = 90, normSoundLevel = 80, timeWeight = 100, isSafetyBypass = false)
        val action = engine.evaluate(result, event, System.currentTimeMillis(), defaultSettings, true, 0)

        assertTrue(action is PolicyAction.SetVolume)
        assertEquals(defaultSettings.maxVolume, (action as PolicyAction.SetVolume).volume, 0.001f)
    }

    @Test
    fun testRetriggerWorksAfterAutoStop() {
        val event = DetectedEvent(
            eventClass = EventClass.MOTORCYCLE,
            confidence = 0.8f,
            isSafetyEvent = false,
            timestampMs = System.currentTimeMillis()
        )
        // First, auto-stop activates
        val lowScoreResult = DisturbanceResult(score = 15, eventWeight = 75, normSoundLevel = 15, timeWeight = 100, isSafetyBypass = false)
        val stopAction = engine.evaluate(lowScoreResult, event, System.currentTimeMillis(), defaultSettings, true, 65_000L)
        assertEquals(PolicyAction.StopMasking, stopAction)

        // Then, a new high-score event comes in
        val highScoreResult = DisturbanceResult(score = 90, eventWeight = 75, normSoundLevel = 90, timeWeight = 100, isSafetyBypass = false)
        val retriggerAction = engine.evaluate(highScoreResult, event, System.currentTimeMillis(), defaultSettings, false, 0)
        assertTrue(retriggerAction is PolicyAction.StartMasking)
    }
}
