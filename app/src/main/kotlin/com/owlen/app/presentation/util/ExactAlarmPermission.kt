package com.owlen.app.presentation.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

/**
 * Opens the system screen that grants exact-alarm access. Android 12+ lets users
 * revoke SCHEDULE_EXACT_ALARM at any time, which silently downgrades the nightly
 * sleep-schedule alarm to an inexact one.
 *
 * The *check* lives on [com.owlen.app.service.SleepTimerScheduler.hasExactAlarmPermission];
 * this object only handles the user-facing request.
 */
object ExactAlarmPermission {

    fun request(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = Uri.parse("package:${context.packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Some OEMs strip the per-app screen; fall back to app details
            context.startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.parse("package:${context.packageName}"))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }
}
