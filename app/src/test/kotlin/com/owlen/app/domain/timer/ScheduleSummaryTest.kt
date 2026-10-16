package com.owlen.app.domain.timer

import com.owlen.app.domain.model.MaskingSound
import org.junit.Test
import kotlin.test.assertEquals

class ScheduleSummaryTest {

    @Test
    fun `same-day schedule reads start, fade-in landing, and stop time`() {
        val summary = buildScheduleSummary(
            sound = MaskingSound.RAIN,
            startHour = 6, startMinute = 0,
            endHour = 8, endMinute = 0,
            fadeInMinutes = 10,
            maxVolume = 0.5f
        )
        assertEquals(
            "Rain will start quietly at 6:00 AM, reach 50% volume by 6:10 AM, and stop at 8:00 AM",
            summary
        )
    }

    @Test
    fun `fade-in landing time crosses midnight correctly`() {
        // Confirmed live on-device: 11:55 PM start + 10min fade-in lands at 12:05 AM, not 23:65 or 11:65 PM.
        val summary = buildScheduleSummary(
            sound = MaskingSound.RAIN,
            startHour = 23, startMinute = 55,
            endHour = 8, endMinute = 0,
            fadeInMinutes = 10,
            maxVolume = 0.8f
        )
        assertEquals(
            "Rain will start quietly at 11:55 PM, reach 80% volume by 12:05 AM, and stop at 8:00 AM",
            summary
        )
    }

    @Test
    fun `fade-in landing time that stays within the same hour just adds minutes`() {
        val summary = buildScheduleSummary(
            sound = MaskingSound.OCEAN_WAVES,
            startHour = 22, startMinute = 0,
            endHour = 6, endMinute = 0,
            fadeInMinutes = 15,
            maxVolume = 0.6f
        )
        assertEquals(
            "Ocean Waves will start quietly at 10:00 PM, reach 60% volume by 10:15 PM, and stop at 6:00 AM",
            summary
        )
    }

    @Test
    fun `zero fade-in makes the landing time equal the start time`() {
        val summary = buildScheduleSummary(
            sound = MaskingSound.FAN,
            startHour = 21, startMinute = 30,
            endHour = 5, endMinute = 0,
            fadeInMinutes = 0,
            maxVolume = 0.4f
        )
        assertEquals(
            "Fan will start quietly at 9:30 PM, reach 40% volume by 9:30 PM, and stop at 5:00 AM",
            summary
        )
    }

    @Test
    fun `midnight hour formats as 12 AM, not 0`() {
        val summary = buildScheduleSummary(
            sound = MaskingSound.WHITE_NOISE,
            startHour = 0, startMinute = 0,
            endHour = 7, endMinute = 0,
            fadeInMinutes = 5,
            maxVolume = 0.5f
        )
        assertEquals(
            "White Noise will start quietly at 12:00 AM, reach 50% volume by 12:05 AM, and stop at 7:00 AM",
            summary
        )
    }

    @Test
    fun `noon hour formats as 12 PM, not 0`() {
        val summary = buildScheduleSummary(
            sound = MaskingSound.PINK_NOISE,
            startHour = 12, startMinute = 0,
            endHour = 13, endMinute = 0,
            fadeInMinutes = 5,
            maxVolume = 0.5f
        )
        assertEquals(
            "Pink Noise will start quietly at 12:00 PM, reach 50% volume by 12:05 PM, and stop at 1:00 PM",
            summary
        )
    }

    @Test
    fun `volume percent truncates rather than rounds`() {
        val summary = buildScheduleSummary(
            sound = MaskingSound.RAIN,
            startHour = 6, startMinute = 0,
            endHour = 8, endMinute = 0,
            fadeInMinutes = 10,
            maxVolume = 0.759f
        )
        assert(summary.contains("75% volume")) { "Expected truncated 75%, got: $summary" }
    }
}
