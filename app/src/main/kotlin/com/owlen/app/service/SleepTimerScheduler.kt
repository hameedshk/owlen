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

    fun scheduleNext(schedule: SleepTimerSchedule) {
        if (!schedule.enabled) {
            cancel()
            return
        }

        val now = System.currentTimeMillis()
        val triggerTimeMs = Companion.computeNextTriggerTime(schedule, now)

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
            val startCal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, schedule.startHour)
                set(Calendar.MINUTE, schedule.startMinute)
                set(Calendar.SECOND, 0)
            }
            val endCal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, schedule.endHour)
                set(Calendar.MINUTE, schedule.endMinute)
                set(Calendar.SECOND, 0)
            }

            // Handle midnight-crossing window
            if (schedule.endHour < schedule.startHour) {
                endCal.add(Calendar.DAY_OF_MONTH, 1)
            }

            // If now is inside [start, end), start immediately
            if (isInsideWindow(now, startCal, endCal)) {
                return nowMs
            }

            // If now < today's start, schedule for today's start
            if (now.before(startCal)) {
                return startCal.timeInMillis
            }

            // Otherwise schedule for tomorrow's start
            startCal.add(Calendar.DAY_OF_MONTH, 1)
            return startCal.timeInMillis
        }

        private fun isInsideWindow(now: Calendar, start: Calendar, end: Calendar): Boolean {
            return !now.before(start) && now.before(end)
        }
    }
}
