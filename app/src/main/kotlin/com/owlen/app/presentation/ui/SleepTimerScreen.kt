package com.owlen.app.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.SleepTimerSchedule
import com.owlen.app.presentation.sleeptimer.SleepTimerViewModel
import com.owlen.app.presentation.ui.components.OptionPickerDialog
import com.owlen.app.presentation.ui.components.RangePickerDialog
import com.owlen.app.presentation.ui.components.SliderDialog
import com.owlen.app.service.ServiceState
import java.util.Calendar

@Composable
fun SleepTimerScreen(viewModel: SleepTimerViewModel = hiltViewModel()) {
    val serviceState by viewModel.serviceState.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val isProtectionRunning by viewModel.isProtectionRunning.collectAsState()
    val schedule by viewModel.schedule.collectAsState()

    val now = Calendar.getInstance()
    var scheduledHour by remember { mutableStateOf(now.get(Calendar.HOUR_OF_DAY)) }
    var scheduledMinute by remember { mutableStateOf(now.get(Calendar.MINUTE)) }

    var selectedSound by remember { mutableStateOf(MaskingSound.RAIN) }
    var selectedDurationMinutes by remember { mutableStateOf(30f) }

    var showSoundPicker by remember { mutableStateOf(false) }
    var showDurationPicker by remember { mutableStateOf(false) }
    var showScheduleHourPicker by remember { mutableStateOf(false) }
    var showScheduleMinutePicker by remember { mutableStateOf(false) }

    val isTimerActive = serviceState is ServiceState.TimedPlaybackActive
    val timerState = serviceState as? ServiceState.TimedPlaybackActive

    val soundOptions = MaskingSound.entries.map { it.displayName }
    val soundIndex = MaskingSound.entries.indexOf(selectedSound)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Sleep Timer", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))

        // Schedule section
        Text("Schedule Alarm", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { showScheduleHourPicker = true }) {
            Text("Hour: $scheduledHour")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { showScheduleMinutePicker = true }) {
            Text("Minute: ${String.format("%02d", scheduledMinute)}")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            val updatedSchedule = schedule.copy(
                startHour = scheduledHour,
                startMinute = scheduledMinute,
                enabled = true
            )
            viewModel.saveSchedule(updatedSchedule)
        }) {
            Text("Enable Schedule")
        }

        if (schedule.enabled) {
            Text("✓ Scheduled for ${String.format("%02d:%02d", schedule.startHour, schedule.startMinute)}",
                style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Immediate timer section
        Text("Start Now", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { showSoundPicker = true }) {
            Text("Sound: ${selectedSound.displayName}")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { showDurationPicker = true }) {
            Text("Duration: ${selectedDurationMinutes.toInt()} minutes")
        }

        if (timerState != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Running: ${timerState.remainingMs / 1000}s remaining")
            Text("Volume: ${(timerState.volume * 100).toInt()}%")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isTimerActive && !isProtectionRunning) {
            Button(onClick = {
                viewModel.startTimer(selectedSound, selectedDurationMinutes.toLong() * 60_000L, settings.maxVolume)
            }) {
                Text("Start Timer")
            }
        } else if (isTimerActive) {
            Button(onClick = { viewModel.stopTimer() }) {
                Text("Stop Timer")
            }
        } else {
            Text("Sleep Protection is running. Stop it to use the timer.")
        }
    }

    if (showScheduleHourPicker) {
        RangePickerDialog(
            title = "Select Hour",
            range = 0..23,
            value = scheduledHour,
            formatWithLeadingZero = true,
            onValueChange = { scheduledHour = it },
            onDismiss = { showScheduleHourPicker = false }
        )
    }

    if (showScheduleMinutePicker) {
        RangePickerDialog(
            title = "Select Minute",
            range = 0..59,
            value = scheduledMinute,
            formatWithLeadingZero = true,
            onValueChange = { scheduledMinute = it },
            onDismiss = { showScheduleMinutePicker = false }
        )
    }

    if (showSoundPicker) {
        OptionPickerDialog(
            title = "Select Sound",
            options = soundOptions,
            selectedIndex = soundIndex,
            onSelect = { index ->
                selectedSound = MaskingSound.entries[index]
                showSoundPicker = false
            },
            onDismiss = { showSoundPicker = false }
        )
    }

    if (showDurationPicker) {
        SliderDialog(
            title = "Duration",
            initialValue = selectedDurationMinutes,
            valueRange = 5f..120f,
            steps = 115,
            formatValue = { "${it.toInt()} min" },
            onConfirm = { value ->
                selectedDurationMinutes = value
                showDurationPicker = false
            },
            onDismiss = { showDurationPicker = false }
        )
    }
}
