package com.owlen.app.domain.model

data class DetectedEvent(
    val eventClass: EventClass,
    val confidence: Float,
    val isSafetyEvent: Boolean,
    val timestampMs: Long
)
