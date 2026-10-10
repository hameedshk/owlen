package com.owlen.app.service

import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.SleepTimerSchedule
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SleepTimerSchedulerTest {

    private fun createCalendar(hour: Int, minute: Int, dayOffset: Int = 0): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            add(Calendar.DAY_OF_MONTH, dayOffset)
        }.timeInMillis
    }

    private fun getHourMinuteFromMs(ms: Long): Pair<Int, Int> {
        val cal = Calendar.getInstance().apply { timeInMillis = ms }
        return Pair(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
    }

    @Test
    fun `computeNextTriggerTime today-future schedules for today start`() {
        val now = createCalendar(5, 30)  // 5:30 AM
        val schedule = SleepTimerSchedule(
            enabled = true,
            startHour = 7,
            startMinute = 0,
            endHour = 9,
            endMinute = 0,
            sound = MaskingSound.BROWN_NOISE
        )

        val trigger = SleepTimerScheduler.computeNextTriggerTime(schedule, now)
        val (hour, minute) = getHourMinuteFromMs(trigger)

        assertEquals(7, hour)
        assertEquals(0, minute)
    }

    @Test
    fun `computeNextTriggerTime in-window starts immediately`() {
        val now = createCalendar(6, 30)  // 6:30 AM
        val schedule = SleepTimerSchedule(
            enabled = true,
            startHour = 6,
            startMinute = 0,
            endHour = 8,
            endMinute = 0,
            sound = MaskingSound.RAIN
        )

        val trigger = SleepTimerScheduler.computeNextTriggerTime(schedule, now)

        assertEquals(now, trigger)
    }

    @Test
    fun `computeNextTriggerTime today-passed schedules for tomorrow start`() {
        val now = createCalendar(10, 0)  // 10:00 AM
        val schedule = SleepTimerSchedule(
            enabled = true,
            startHour = 7,
            startMinute = 0,
            endHour = 9,
            endMinute = 0,
            sound = MaskingSound.PINK_NOISE
        )

        val trigger = SleepTimerScheduler.computeNextTriggerTime(schedule, now)
        val (hour, _) = getHourMinuteFromMs(trigger)

        assertEquals(7, hour)
        // Verify it's tomorrow by checking the day offset
        val nowCal = Calendar.getInstance().apply { timeInMillis = now }
        val triggerCal = Calendar.getInstance().apply { timeInMillis = trigger }
        assertEquals(1, triggerCal.get(Calendar.DAY_OF_MONTH) - nowCal.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `computeNextTriggerTime midnight-crossing window handles wraparound`() {
        // 11 PM - 1 AM schedule, queried at 11:30 PM
        val now = createCalendar(23, 30)
        val schedule = SleepTimerSchedule(
            enabled = true,
            startHour = 23,
            startMinute = 0,
            endHour = 1,
            endMinute = 0,
            sound = MaskingSound.WHITE_NOISE
        )

        val trigger = SleepTimerScheduler.computeNextTriggerTime(schedule, now)

        assertEquals(now, trigger)  // Should start immediately (in window)
    }

    @Test
    fun `computeNextTriggerTime midnight-crossing after window schedules for tomorrow`() {
        // 11 PM - 1 AM schedule, queried at 2 AM (past window)
        val now = createCalendar(2, 0)
        val schedule = SleepTimerSchedule(
            enabled = true,
            startHour = 23,
            startMinute = 0,
            endHour = 1,
            endMinute = 0,
            sound = MaskingSound.BROWN_NOISE
        )

        val trigger = SleepTimerScheduler.computeNextTriggerTime(schedule, now)
        val (hour, minute) = getHourMinuteFromMs(trigger)

        assertEquals(23, hour)
        assertEquals(0, minute)
        // Verify it's tomorrow
        val nowCal = Calendar.getInstance().apply { timeInMillis = now }
        val triggerCal = Calendar.getInstance().apply { timeInMillis = trigger }
        assertEquals(1, triggerCal.get(Calendar.DAY_OF_MONTH) - nowCal.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `computeNextTriggerTime at exact boundary includes current time`() {
        val now = createCalendar(6, 0)  // Exactly at start time
        val schedule = SleepTimerSchedule(
            enabled = true,
            startHour = 6,
            startMinute = 0,
            endHour = 8,
            endMinute = 0,
            sound = MaskingSound.RAIN
        )

        val trigger = SleepTimerScheduler.computeNextTriggerTime(schedule, now)

        assertEquals(now, trigger)  // Should start immediately
    }

    @Test
    fun `computeNextTriggerTime at end boundary excludes current time`() {
        val now = createCalendar(8, 0)  // Exactly at end time (exclusive boundary)
        val schedule = SleepTimerSchedule(
            enabled = true,
            startHour = 6,
            startMinute = 0,
            endHour = 8,
            endMinute = 0,
            sound = MaskingSound.BROWN_NOISE
        )

        val trigger = SleepTimerScheduler.computeNextTriggerTime(schedule, now)

        val (hour, _) = getHourMinuteFromMs(trigger)
        assertEquals(6, hour)
        // Should be tomorrow since we've passed today's window
        val nowCal = Calendar.getInstance().apply { timeInMillis = now }
        val triggerCal = Calendar.getInstance().apply { timeInMillis = trigger }
        assertEquals(1, triggerCal.get(Calendar.DAY_OF_MONTH) - nowCal.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `computeNextTriggerTime respects minute precision`() {
        val now = createCalendar(5, 30)
        val schedule = SleepTimerSchedule(
            enabled = true,
            startHour = 6,
            startMinute = 45,
            endHour = 8,
            endMinute = 30,
            sound = MaskingSound.PINK_NOISE
        )

        val trigger = SleepTimerScheduler.computeNextTriggerTime(schedule, now)
        val (hour, minute) = getHourMinuteFromMs(trigger)

        assertEquals(6, hour)
        assertEquals(45, minute)
    }
}
