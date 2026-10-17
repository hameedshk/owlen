package com.owlen.app.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material.icons.rounded.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.SleepTimerSchedule
import com.owlen.app.domain.model.windowDurationMinutes
import com.owlen.app.domain.timer.buildScheduleSummary
import com.owlen.app.presentation.ui.components.OptionPickerDialog
import com.owlen.app.presentation.ui.components.OwlenButton
import com.owlen.app.presentation.ui.components.OwlenCard
import com.owlen.app.presentation.ui.components.OwlenInfoCard
import com.owlen.app.presentation.ui.components.OwlenRow
import com.owlen.app.presentation.ui.components.OwlenRowDivider
import com.owlen.app.presentation.ui.components.OwlenScreenHeader
import com.owlen.app.presentation.ui.components.OwlenSwitchRow
import com.owlen.app.presentation.ui.components.RangePickerDialog
import com.owlen.app.presentation.ui.components.SliderDialog
import com.owlen.app.presentation.ui.components.SoundHeroCard
import com.owlen.app.presentation.ui.components.TimePickerDialog
import com.owlen.app.presentation.ui.theme.FadeSlideIn
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.Warning as WarningColor
import com.owlen.app.presentation.util.formatDuration
import com.owlen.app.presentation.util.formatTime12h

private enum class ScheduleDialog { NONE, SOUND, START_TIME, END_TIME, FADE_IN, MAX_VOLUME }

/**
 * Nightly sleep schedule: Owlen eases a masking sound in at a set time and stops
 * it at wake time, with no monitoring involved. Stateless — the draft lives here;
 * persistence and alarm scheduling happen in [onSave].
 */
@Composable
fun SleepScheduleScreen(
    schedule: SleepTimerSchedule,
    hasExactAlarmPermission: Boolean = true,
    onSave: (SleepTimerSchedule) -> Unit = {},
    onRequestExactAlarmPermission: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    // Re-seeded whenever the persisted schedule changes, including after a save
    var draft by remember(schedule) { mutableStateOf(schedule) }
    var openDialog by remember { mutableStateOf(ScheduleDialog.NONE) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        OwlenScreenHeader(
            title = "Sleep Schedule",
            overline = "Nightly",
            onNavigateBack = onNavigateBack
        )

        if (!hasExactAlarmPermission) {
            OwlenInfoCard(
                title = "Alarms may run late",
                text = "Without exact alarm access Android can delay the schedule by " +
                    "several minutes. Grant it so playback starts on time.",
                icon = Icons.Rounded.Warning,
                tint = WarningColor,
                actionText = "Allow exact alarms",
                onAction = onRequestExactAlarmPermission,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        FadeSlideIn {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                OwlenCard(contentPadding = 0.dp) {
                    OwlenSwitchRow(
                        title = "Nightly schedule",
                        subtitle = if (draft.enabled) {
                            "Owlen will start playback on its own"
                        } else {
                            "Off — nothing plays automatically"
                        },
                        checked = draft.enabled,
                        onCheckedChange = { draft = draft.copy(enabled = it) },
                        leading = { ScheduleRowIcon(Icons.Rounded.Bedtime) }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                SoundHeroCard(
                    sound = draft.sound,
                    label = "Schedule sound",
                    onClick = { openDialog = ScheduleDialog.SOUND }
                )

                Spacer(modifier = Modifier.height(20.dp))

                OwlenCard(contentPadding = 0.dp) {
                    OwlenRow(
                        title = "Starts at",
                        value = formatTime12h(draft.startHour, draft.startMinute),
                        leading = { ScheduleRowIcon(Icons.Rounded.Bedtime) },
                        onClick = { openDialog = ScheduleDialog.START_TIME }
                    )
                    OwlenRowDivider()
                    OwlenRow(
                        title = "Stops at",
                        value = formatTime12h(draft.endHour, draft.endMinute),
                        leading = { ScheduleRowIcon(Icons.Rounded.WbTwilight) },
                        onClick = { openDialog = ScheduleDialog.END_TIME }
                    )
                    OwlenRowDivider()
                    OwlenRow(
                        title = "Fade in over",
                        value = if (draft.fadeInMinutes == 0) {
                            "Off"
                        } else {
                            formatDuration(draft.fadeInMinutes)
                        },
                        leading = { ScheduleRowIcon(Icons.Rounded.GraphicEq) },
                        onClick = { openDialog = ScheduleDialog.FADE_IN }
                    )
                    OwlenRowDivider()
                    OwlenRow(
                        title = "Maximum volume",
                        value = "${(draft.maxVolume * 100).toInt()}%",
                        leading = { ScheduleRowIcon(Icons.AutoMirrored.Rounded.VolumeUp) },
                        onClick = { openDialog = ScheduleDialog.MAX_VOLUME }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        FadeSlideIn(delayMillis = 100) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                OwlenInfoCard(
                    text = buildScheduleSummary(
                        sound = draft.sound,
                        startHour = draft.startHour,
                        startMinute = draft.startMinute,
                        endHour = draft.endHour,
                        endMinute = draft.endMinute,
                        fadeInMinutes = draft.fadeInMinutes,
                        maxVolume = draft.maxVolume
                    )
                )

                Spacer(modifier = Modifier.height(28.dp))

                OwlenButton(
                    text = "Save Schedule",
                    onClick = { onSave(draft) },
                    enabled = draft != schedule,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    when (openDialog) {
        ScheduleDialog.SOUND -> OptionPickerDialog(
            title = "Schedule Sound",
            options = MaskingSound.entries.map { it.displayName },
            selectedIndex = MaskingSound.entries.indexOf(draft.sound),
            onSelect = { index ->
                draft = draft.copy(sound = MaskingSound.entries[index])
                openDialog = ScheduleDialog.NONE
            },
            onDismiss = { openDialog = ScheduleDialog.NONE }
        )

        ScheduleDialog.START_TIME -> TimePickerDialog(
            title = "Start Time",
            hour = draft.startHour,
            minute = draft.startMinute,
            onConfirm = { hour, minute ->
                // Fade-in can never outlast the window it plays in
                val updated = draft.copy(startHour = hour, startMinute = minute)
                draft = updated.copy(
                    fadeInMinutes = updated.fadeInMinutes.coerceAtMost(updated.windowDurationMinutes)
                )
                openDialog = ScheduleDialog.NONE
            },
            onDismiss = { openDialog = ScheduleDialog.NONE }
        )

        ScheduleDialog.END_TIME -> TimePickerDialog(
            title = "Stop Time",
            hour = draft.endHour,
            minute = draft.endMinute,
            onConfirm = { hour, minute ->
                val updated = draft.copy(endHour = hour, endMinute = minute)
                draft = updated.copy(
                    fadeInMinutes = updated.fadeInMinutes.coerceAtMost(updated.windowDurationMinutes)
                )
                openDialog = ScheduleDialog.NONE
            },
            onDismiss = { openDialog = ScheduleDialog.NONE }
        )

        ScheduleDialog.FADE_IN -> RangePickerDialog(
            title = "Fade In Over",
            range = 0..draft.windowDurationMinutes.coerceAtMost(60),
            value = draft.fadeInMinutes,
            unitLabel = "minutes",
            onConfirm = { minutes ->
                draft = draft.copy(fadeInMinutes = minutes)
                openDialog = ScheduleDialog.NONE
            },
            onDismiss = { openDialog = ScheduleDialog.NONE }
        )

        ScheduleDialog.MAX_VOLUME -> SliderDialog(
            title = "Maximum Volume",
            initialValue = draft.maxVolume * 100f,
            valueRange = 10f..100f,
            steps = 17,
            formatValue = { "${it.toInt()}%" },
            onConfirm = { percent ->
                draft = draft.copy(maxVolume = percent.toInt() / 100f)
                openDialog = ScheduleDialog.NONE
            },
            onDismiss = { openDialog = ScheduleDialog.NONE }
        )

        ScheduleDialog.NONE -> Unit
    }
}

@Composable
private fun ScheduleRowIcon(
    icon: ImageVector,
    tint: Color = TextSecondary
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(20.dp)
    )
}
