package com.owlen.app.domain.timer

import com.owlen.app.domain.model.MaskingSound
import java.util.Locale

/**
 * Pure function to generate a human-readable summary sentence for a sleep schedule.
 * Example: "Rain will start quietly at 6:00 AM, reach 50% volume by 6:10 AM, and stop at 8:00 AM"
 */
fun buildScheduleSummary(
    sound: MaskingSound,
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
    fadeInMinutes: Int,
    maxVolume: Float
): String {
    val startTime = formatTime(startHour, startMinute)
    val maxReachedTime = formatTime(
        startHour + (startMinute + fadeInMinutes) / 60,
        (startMinute + fadeInMinutes) % 60
    )
    val endTime = formatTime(endHour, endMinute)
    val volumePercent = (maxVolume * 100).toInt()

    return "${sound.displayName} will start quietly at $startTime, reach $volumePercent% volume by $maxReachedTime, and stop at $endTime"
}

private fun formatTime(hour: Int, minute: Int): String {
    val normalizedHour = hour % 24
    val amPm = if (normalizedHour < 12) "AM" else "PM"
    val displayHour = if (normalizedHour == 0) 12 else if (normalizedHour > 12) normalizedHour - 12 else normalizedHour
    return String.format(Locale.US, "%d:%02d %s", displayHour, minute, amPm)
}
