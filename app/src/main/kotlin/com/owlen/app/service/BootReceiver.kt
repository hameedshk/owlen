package com.owlen.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.owlen.app.di.AppModule
import dagger.hilt.EntryPoints
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED || context == null) return

        val entryPoint = EntryPoints.get(context, AppModule.SettingsEntryPoint::class.java)
        GlobalScope.launch {
            val schedule = entryPoint.settingsRepository().getSchedule()
            if (schedule.enabled) {
                val scheduler = SleepTimerScheduler(context)
                scheduler.scheduleNext(schedule)
            }
        }
    }
}
