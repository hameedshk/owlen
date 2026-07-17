package com.owlen.app.data.log

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class EventLogCompatTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun testOldEventLogWithoutCustomLabelStillParses() {
        // Session files written before the custom-disturbance feature
        val old = """{"timestampMs":1,"eventClass":"DOG_BARKING","confidence":0.8,""" +
            """"disturbanceScore":55,"action":"MASKED"}"""
        val log = json.decodeFromString<EventLog>(old)
        assertEquals("DOG_BARKING", log.eventClass)
        assertNull(log.customLabel)
    }

    @Test
    fun testCustomEventLogRoundTripsLabel(): Unit {
        val log = EventLog(
            timestampMs = 1L,
            eventClass = "CUSTOM",
            confidence = 0.9f,
            disturbanceScore = 80,
            action = "MASKED",
            customLabel = "Bedroom door"
        )
        val decoded = json.decodeFromString<EventLog>(json.encodeToString(EventLog.serializer(), log))
        assertEquals("Bedroom door", decoded.customLabel)
        assertEquals("CUSTOM", decoded.eventClass)
    }
}
