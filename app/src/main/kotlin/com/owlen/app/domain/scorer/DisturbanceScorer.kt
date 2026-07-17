package com.owlen.app.domain.scorer

import com.owlen.app.domain.model.DetectedEvent
import com.owlen.app.domain.model.DisturbanceResult
import com.owlen.app.domain.model.EventClass
import com.owlen.app.domain.model.SleepSettings
import kotlin.math.max
import kotlin.math.min

class DisturbanceScorer {
    fun score(
        event: DetectedEvent,
        dBSPL: Float,
        currentHour: Int,
        settings: SleepSettings
    ): DisturbanceResult {
        val eventWeight = getEventWeight(event.eventClass)
        val isSafetyBypass = event.eventClass.isSafetyEvent && event.confidence >= 0.70f

        // Rain special case: if enabled, return 0 score
        if (event.eventClass == EventClass.RAIN && settings.rainBehaviourEnabled) {
            return DisturbanceResult(
                score = 0,
                eventWeight = eventWeight,
                normSoundLevel = 0,
                timeWeight = 100,
                isSafetyBypass = false
            )
        }

        // Safety bypass: return immediately without scoring
        if (isSafetyBypass) {
            return DisturbanceResult(
                score = 100,
                eventWeight = eventWeight,
                normSoundLevel = 100,
                timeWeight = 100,
                isSafetyBypass = true
            )
        }

        // Calculate normalized sound level
        val normSoundLevel = calculateNormSoundLevel(dBSPL, settings.soundLevelFloor)

        // Calculate time weight
        val timeWeight = if (isWithinSleepWindow(currentHour, settings)) 100 else 30

        // Calculate final score: (EventWeight × 0.5) + (NormSoundLevel × 0.3) + (TimeWeight × 0.2)
        val score = (eventWeight * 0.5f + normSoundLevel * 0.3f + timeWeight * 0.2f).toInt()
        val clampedScore = min(100, max(0, score))

        return DisturbanceResult(
            score = clampedScore,
            eventWeight = eventWeight,
            normSoundLevel = normSoundLevel,
            timeWeight = timeWeight,
            isSafetyBypass = false
        )
    }

    private fun getEventWeight(event: EventClass): Int = when (event) {
        EventClass.GARBAGE_COLLECTION -> 80
        EventClass.HUMAN_SHOUTING -> 90
        EventClass.MOTORCYCLE -> 75
        EventClass.DOG_BARKING -> 60
        EventClass.CONSTRUCTION -> 85
        EventClass.RAIN -> 20
        EventClass.THUNDER -> 50
        EventClass.CUSTOM -> 75 // User-enrolled — high personal salience, never force-triggers
        EventClass.UNKNOWN -> 40
        EventClass.BABY_CRY -> 100 // Safety event
        EventClass.SMOKE_ALARM -> 100 // Safety event
    }

    private fun calculateNormSoundLevel(dBSPL: Float, floor: Float): Int {
        val normalized = (dBSPL - floor) / (90f - floor) * 100f
        return min(100, max(0, normalized.toInt()))
    }

    private fun isWithinSleepWindow(currentHour: Int, settings: SleepSettings): Boolean {
        return if (settings.sleepWindowStartHour < settings.sleepWindowEndHour) {
            // Normal case: e.g., 9am to 5pm
            currentHour in settings.sleepWindowStartHour until settings.sleepWindowEndHour
        } else {
            // Wraparound case: e.g., 10pm to 6am
            currentHour >= settings.sleepWindowStartHour || currentHour < settings.sleepWindowEndHour
        }
    }
}
