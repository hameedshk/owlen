package com.owlen.app.domain.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SleepTimerScheduleTest {

    @Test
    fun `default window spans night hours, not morning hours`() {
        // Regression guard: this previously defaulted to 6 AM-8 AM, so a user who
        // enabled the schedule without touching the time pickers got playback in
        // the morning instead of overnight.
        val schedule = SleepTimerSchedule()

        assertEquals(22, schedule.startHour, "default start should be 10 PM")
        assertEquals(6, schedule.endHour, "default end should be 6 AM")
        assertTrue(
            schedule.startHour > schedule.endHour,
            "default window must cross midnight (start hour > end hour) to count as a nightly schedule"
        )
    }

    @Test
    fun `window duration for a same-day schedule is a plain subtraction`() {
        val schedule = SleepTimerSchedule(startHour = 6, startMinute = 0, endHour = 8, endMinute = 30)
        assertEquals(150, schedule.windowDurationMinutes)
    }

    @Test
    fun `window duration wraps past midnight when end is before start`() {
        val schedule = SleepTimerSchedule(startHour = 22, startMinute = 0, endHour = 6, endMinute = 0)
        assertEquals(8 * 60, schedule.windowDurationMinutes)
    }

    @Test
    fun `window duration wraps a full day when start and end are equal`() {
        val schedule = SleepTimerSchedule(startHour = 22, startMinute = 15, endHour = 22, endMinute = 15)
        assertEquals(24 * 60, schedule.windowDurationMinutes)
    }

    @Test
    fun `window duration handles a wrap with non-zero minutes`() {
        val schedule = SleepTimerSchedule(startHour = 23, startMinute = 30, endHour = 0, endMinute = 15)
        assertEquals(45, schedule.windowDurationMinutes)
    }
}
