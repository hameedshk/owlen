package com.owlen.app.presentation.util

/**
 * 12-hour clock formatting shared by Settings, Sleep Timer, and Sleep Schedule.
 * Hours are 24-hour inputs (0..23); output is e.g. "6:05 AM", "11:00 PM".
 */
fun formatTime12h(hour: Int, minute: Int): String {
    val normalized = normalizeHour(hour)
    return "${to12Hour(normalized)}:${minute.toString().padStart(2, '0')} ${amPmLabel(normalized)}"
}

/** Hour-only variant: "6 AM", "11 PM". */
fun formatHour12h(hour: Int): String {
    val normalized = normalizeHour(hour)
    return "${to12Hour(normalized)} ${amPmLabel(normalized)}"
}

/** Converts a 12-hour picker selection back to a 24-hour value. */
fun to24Hour(hour12: Int, isAm: Boolean): Int {
    val base = hour12 % 12
    return if (isAm) base else base + 12
}

/** The 1..12 hour a picker should show for a 24-hour value. */
fun to12Hour(hour24: Int): Int {
    val base = normalizeHour(hour24) % 12
    return if (base == 0) 12 else base
}

/** True when a 24-hour value falls in the AM half of the day. */
fun isAm(hour24: Int): Boolean = normalizeHour(hour24) < 12

/** Duration as "1h 30m" / "45m", used for timer and schedule summaries. */
fun formatDuration(totalMinutes: Int): String {
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}

private fun normalizeHour(hour: Int): Int = ((hour % 24) + 24) % 24

private fun amPmLabel(normalizedHour: Int): String = if (normalizedHour < 12) "AM" else "PM"
