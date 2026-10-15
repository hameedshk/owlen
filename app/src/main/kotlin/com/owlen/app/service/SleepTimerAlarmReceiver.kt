package com.owlen.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.di.AppModule
import dagger.hilt.EntryPoints
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar

class SleepTimerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val soundName = intent.getStringExtra("sound") ?: return
        val endHour = intent.getIntExtra("end_hour", 8)
        val endMinute = intent.getIntExtra("end_minute", 0)
        val fadeInMinutes = intent.getIntExtra("fade_in_minutes", 10)
        val maxVolume = intent.getFloatExtra("max_volume", 0.5f)

        val sound = try {
            MaskingSound.valueOf(soundName)
        } catch (e: IllegalArgumentException) {
            return
        }

        // onReceive hands us a ReceiverRestrictedContext, which is not a Hilt
        // component holder and cannot bind a service. Everything below must run
        // against the application context.
        val appContext = context.applicationContext

        val goAsync = goAsync()
        GlobalScope.launch {
            try {
                val now = System.currentTimeMillis()
                val nowCal = Calendar.getInstance()
                val endCal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, endHour)
                    set(Calendar.MINUTE, endMinute)
                    set(Calendar.SECOND, 0)
                }

                // Handle midnight-crossing: if end time is in the past today, move to tomorrow
                if (nowCal.after(endCal)) {
                    endCal.add(Calendar.DAY_OF_MONTH, 1)
                }

                val durationMs = endCal.timeInMillis - now
                if (durationMs > 0) {
                    SleepProtectionService.startTimer(
                        appContext,
                        sound,
                        durationMs,
                        maxVolume,
                        SleepProtectionService.PlaybackMode.SCHEDULE,
                        fadeInMinutes * 60 * 1000L
                    )
                }

                // Schedule the next day's alarm. We are inside the window we just
                // opened, so the search has to start after it closes — otherwise
                // "next start" resolves to now and the alarm re-fires in a loop.
                val entryPoint =
                    EntryPoints.get(appContext, AppModule.SettingsEntryPoint::class.java)
                val schedule = entryPoint.settingsRepository().getSchedule()
                if (schedule.enabled) {
                    val scheduler = SleepTimerScheduler(appContext)
                    scheduler.scheduleNext(schedule, endCal.timeInMillis + 60_000L)
                }
            } finally {
                goAsync.finish()
            }
        }
    }
}
