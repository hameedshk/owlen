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

        // onReceive hands us a ReceiverRestrictedContext, which is not a Hilt
        // component holder — resolve the application context before any lookup.
        val appContext = context.applicationContext

        val goAsync = goAsync()
        val entryPoint = EntryPoints.get(appContext, AppModule.SettingsEntryPoint::class.java)
        GlobalScope.launch {
            try {
                val schedule = entryPoint.settingsRepository().getSchedule()
                if (schedule.enabled) {
                    val scheduler = SleepTimerScheduler(appContext)
                    scheduler.scheduleNext(schedule)
                }
            } finally {
                goAsync.finish()
            }
        }
    }
}
