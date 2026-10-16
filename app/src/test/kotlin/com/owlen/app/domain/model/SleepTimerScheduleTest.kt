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
}
