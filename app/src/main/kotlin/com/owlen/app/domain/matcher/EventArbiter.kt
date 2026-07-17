package com.owlen.app.domain.matcher

import com.owlen.app.domain.model.DetectedEvent
import com.owlen.app.domain.model.EventClass

/**
 * Decides which event a window ultimately represents when the class detector
 * and the custom-sound matcher disagree.
 *
 * Safety events (Smoke Alarm, Baby Cry) always win unconditionally — a custom
 * match must never suppress or delay a safety alert. Otherwise an enrolled
 * match wins over generic classes because enrollment is explicit user intent
 * and the similarity gate is already conservative.
 */
class EventArbiter {

    fun arbitrate(
        detected: DetectedEvent,
        match: CustomSoundMatcher.CustomMatch?
    ): DetectedEvent {
        if (detected.isSafetyEvent) return detected
        if (match == null) return detected
        return DetectedEvent(
            eventClass = EventClass.CUSTOM,
            confidence = match.similarity.coerceIn(0f, 1f),
            isSafetyEvent = false,
            timestampMs = detected.timestampMs,
            customLabel = match.prototype.name
        )
    }
}
