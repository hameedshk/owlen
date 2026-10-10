package com.owlen.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.owlen.app.domain.model.MaskingSound
import java.util.Calendar

class SleepTimerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val soundName = intent.getStringExtra("sound") ?: return
        val endHour = intent.getIntExtra("end_hour", 8)
        val endMinute = intent.getIntExtra("end_minute", 0)

        val sound = try {
            MaskingSound.valueOf(soundName)
        } catch (e: IllegalArgumentException) {
            return
        }

        val now = System.currentTimeMillis()
        val endCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, endHour)
            set(Calendar.MINUTE, endMinute)
            set(Calendar.SECOND, 0)
        }

        val durationMs = if (now < endCal.timeInMillis) {
            endCal.timeInMillis - now
        } else {
            0L
        }

        if (durationMs > 0) {
            SleepProtectionService.startTimer(context, sound, durationMs, 0.8f)
        }
    }
}
