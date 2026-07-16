package com.owlen.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.owlen.app.domain.model.EventClass
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.presentation.MainActivity

class NotificationHelper(private val context: Context) {

    companion object {
        const val PROTECTION_CHANNEL_ID = "owlen_protection"
        const val SAFETY_CHANNEL_ID = "owlen_safety"
        const val PROTECTION_CHANNEL_NAME = "Sleep Protection"
        const val SAFETY_CHANNEL_NAME = "Safety Alerts"
    }

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(NotificationManager::class.java)

            // Protection channel - low importance, silent
            val protectionChannel = NotificationChannel(
                PROTECTION_CHANNEL_ID,
                PROTECTION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                enableLights(false)
                enableVibration(false)
                setSound(null, null)
            }
            notificationManager.createNotificationChannel(protectionChannel)

            // Safety channel - high importance, with sound
            val safetyChannel = NotificationChannel(
                SAFETY_CHANNEL_ID,
                SAFETY_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(safetyChannel)
        }
    }

    fun createServiceNotification(context: Context): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, PROTECTION_CHANNEL_ID)
            .setContentTitle("Sleep Protection is active")
            .setContentText("Tap to view.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    fun createMaskingNotification(context: Context, sound: MaskingSound): Notification {
        return NotificationCompat.Builder(context, PROTECTION_CHANNEL_ID)
            .setContentTitle("Masking started")
            .setContentText(sound.displayName)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    fun createSafetyNotification(context: Context, event: EventClass): Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("SAFETY_EVENT", event.name)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, SAFETY_CHANNEL_ID)
            .setContentTitle("Alert: ${event.displayName} detected")
            .setContentText("Masking stopped.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()
    }

    fun createInterruptionNotification(context: Context, interruptedAt: Long): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            2,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val timeString = formatTime(interruptedAt)
        return NotificationCompat.Builder(context, PROTECTION_CHANNEL_ID)
            .setContentTitle("Sleep Protection was interrupted")
            .setContentText("Interrupted at $timeString. Tap to review.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun formatTime(timeMs: Long): String {
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = timeMs
        val hour = cal.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = cal.get(java.util.Calendar.MINUTE)
        return String.format("%02d:%02d", hour, minute)
    }
}
