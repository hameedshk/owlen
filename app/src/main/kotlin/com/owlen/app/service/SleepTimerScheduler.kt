package com.owlen.app.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.owlen.app.domain.model.SleepTimerSchedule
import java.util.Calendar

class SleepTimerScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val requestCode = 9001

    /**
     * Arms the next start alarm.
     *
     * [fromMs] is the instant the search starts from. It defaults to now, which
     * makes an enable-while-inside-the-window start immediately. The alarm
     * receiver must instead pass an instant past the window it just opened —
     * re-arming from "now" would resolve to now again and re-fire in a loop.
     */
    fun scheduleNext(schedule: SleepTimerSchedule, fromMs: Long = System.currentTimeMillis()) {
        if (!schedule.enabled) {
            cancel()
            return
        }

        val triggerTimeMs = Companion.computeNextTriggerTime(schedule, fromMs)

        val intent = Intent(context, SleepTimerAlarmReceiver::class.java).apply {
            putExtra("sound", schedule.sound.name)
            putExtra("end_hour", schedule.endHour)
            putExtra("end_minute", schedule.endMinute)
            putExtra("fade_in_minutes", schedule.fadeInMinutes)
            putExtra("max_volume", schedule.maxVolume)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeMs, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeMs, pendingIntent)
        }
    }

    fun cancel() {
        val intent = Intent(context, SleepTimerAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun hasExactAlarmPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    companion object {
        fun computeNextTriggerTime(schedule: SleepTimerSchedule, nowMs: Long): Long {
            val now = Calendar.getInstance().apply { timeInMillis = nowMs }
            val wrapsMidnight = wrapsMidnight(schedule)

            val startCal = dayOf(nowMs, schedule.startHour, schedule.startMinute)
            val endCal = dayOf(nowMs, schedule.endHour, schedule.endMinute).apply {
                if (wrapsMidnight) add(Calendar.DAY_OF_MONTH, 1)
            }

            // A window that crosses midnight may have opened yesterday and still be
            // running: between midnight and the end time, today's start is hours away
            // yet playback is already due.
            if (wrapsMidnight) {
                val yesterdayStart = (startCal.clone() as Calendar)
                    .apply { add(Calendar.DAY_OF_MONTH, -1) }
                val yesterdayEnd = (endCal.clone() as Calendar)
                    .apply { add(Calendar.DAY_OF_MONTH, -1) }
                if (isInsideWindow(now, yesterdayStart, yesterdayEnd)) {
                    return nowMs
                }
            }

            // Inside today's window → start immediately
            if (isInsideWindow(now, startCal, endCal)) {
                return nowMs
            }

            // Today's start still ahead → wait for it
            if (now.before(startCal)) {
                return startCal.timeInMillis
            }

            // Today's start already passed → tomorrow
            startCal.add(Calendar.DAY_OF_MONTH, 1)
            return startCal.timeInMillis
        }

        /** True when the end time is at or before the start time, e.g. 23:30 → 06:15. */
        private fun wrapsMidnight(schedule: SleepTimerSchedule): Boolean {
            val start = schedule.startHour * 60 + schedule.startMinute
            val end = schedule.endHour * 60 + schedule.endMinute
            return end <= start
        }

        private fun dayOf(baseMs: Long, hour: Int, minute: Int): Calendar =
            Calendar.getInstance().apply {
                timeInMillis = baseMs
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

        private fun isInsideWindow(now: Calendar, start: Calendar, end: Calendar): Boolean {
            return !now.before(start) && now.before(end)
        }
    }
}
