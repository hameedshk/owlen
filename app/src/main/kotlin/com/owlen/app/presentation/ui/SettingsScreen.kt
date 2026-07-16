package com.owlen.app.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.Sensitivity
import com.owlen.app.presentation.ui.theme.GlassDialog
import com.owlen.app.presentation.ui.theme.GlassFillStrong
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.TextDisabled
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.glass

private enum class SettingsDialog {
    NONE, SOUND, VOLUME, SENSITIVITY, AUTO_STOP, SOUND_FLOOR
}

@Composable
fun SettingsScreen(
    sleepHour: Int = 22,
    wakeHour: Int = 7,
    preferredSound: MaskingSound = MaskingSound.BROWN_NOISE,
    maxVolumePct: Int = 70,
    sensitivity: Sensitivity = Sensitivity.MEDIUM,
    autoStopSeconds: Int = 60,
    rainBehaviourEnabled: Boolean = true,
    soundLevelFloorDb: Int = 50,
    batteryOptimisationAllowed: Boolean = true,
    appVersion: String = "1.0.0",
    onNavigateToSleepSchedule: () -> Unit = {},
    onSoundSelected: (MaskingSound) -> Unit = {},
    onMaxVolumeChanged: (Int) -> Unit = {},
    onSensitivitySelected: (Sensitivity) -> Unit = {},
    onAutoStopChanged: (Int) -> Unit = {},
    onRainBehaviourChanged: (Boolean) -> Unit = {},
    onSoundFloorChanged: (Int) -> Unit = {},
    onBatteryOptimisationClick: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {}
) {
    var openDialog by remember { mutableStateOf(SettingsDialog.NONE) }

    val sleepFormatted = run {
        val h = if (sleepHour % 12 == 0) 12 else sleepHour % 12
        val amPm = if (sleepHour < 12) "AM" else "PM"
        "$h:00 $amPm"
    }
    val wakeFormatted = run {
        val h = if (wakeHour % 12 == 0) 12 else wakeHour % 12
        val amPm = if (wakeHour < 12) "AM" else "PM"
        "$h:00 $amPm"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
        }

        // Settings list card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .glass(RoundedCornerShape(12.dp))
        ) {
            SettingsRow(
                label = "Sleep Schedule",
                value = "$sleepFormatted – $wakeFormatted",
                onClick = onNavigateToSleepSchedule
            )
            HorizontalDivider(color = GlassFillStrong, thickness = 1.dp)

            SettingsRow(
                label = "Protection Sound",
                value = preferredSound.displayName,
                onClick = { openDialog = SettingsDialog.SOUND }
            )
            HorizontalDivider(color = GlassFillStrong, thickness = 1.dp)

            SettingsRow(
                label = "Maximum Volume",
                value = "$maxVolumePct%",
                onClick = { openDialog = SettingsDialog.VOLUME }
            )
            HorizontalDivider(color = GlassFillStrong, thickness = 1.dp)

            SettingsRow(
                label = "Sensitivity",
                value = sensitivity.name.lowercase().replaceFirstChar { it.uppercase() },
                onClick = { openDialog = SettingsDialog.SENSITIVITY }
            )
            HorizontalDivider(color = GlassFillStrong, thickness = 1.dp)

            SettingsRow(
                label = "Auto Stop",
                value = "$autoStopSeconds sec",
                onClick = { openDialog = SettingsDialog.AUTO_STOP }
            )
            HorizontalDivider(color = GlassFillStrong, thickness = 1.dp)

            SettingsRow(
                label = "Sound Level Floor",
                value = "$soundLevelFloorDb dB",
                onClick = { openDialog = SettingsDialog.SOUND_FLOOR }
            )
            HorizontalDivider(color = GlassFillStrong, thickness = 1.dp)

            // Rain behaviour — inline toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Soothing Rain",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary
                    )
                    Text(
                        text = "Treat rain as soothing — never trigger masking",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
                Switch(
                    checked = rainBehaviourEnabled,
                    onCheckedChange = onRainBehaviourChanged,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Green,
                        checkedThumbColor = TextPrimary,
                        uncheckedTrackColor = TextDisabled,
                        uncheckedThumbColor = TextSecondary
                    )
                )
            }
            HorizontalDivider(color = GlassFillStrong, thickness = 1.dp)

            SettingsRow(
                label = "Battery Optimisation",
                value = if (batteryOptimisationAllowed) "Allowed" else "Restricted",
                onClick = onBatteryOptimisationClick
            )
            HorizontalDivider(color = GlassFillStrong, thickness = 1.dp)

            SettingsRow(
                label = "About Owlen",
                value = "v$appVersion",
                onClick = onNavigateToAbout
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    when (openDialog) {
        SettingsDialog.SOUND -> OptionPickerDialog(
            title = "Protection Sound",
            options = MaskingSound.values().map { it.displayName },
            selectedIndex = MaskingSound.values().indexOf(preferredSound),
            onSelect = { index ->
                onSoundSelected(MaskingSound.values()[index])
                openDialog = SettingsDialog.NONE
            },
            onDismiss = { openDialog = SettingsDialog.NONE }
        )

        SettingsDialog.SENSITIVITY -> OptionPickerDialog(
            title = "Sensitivity",
            options = Sensitivity.values().map {
                it.name.lowercase().replaceFirstChar { c -> c.uppercase() }
            },
            selectedIndex = Sensitivity.values().indexOf(sensitivity),
            onSelect = { index ->
                onSensitivitySelected(Sensitivity.values()[index])
                openDialog = SettingsDialog.NONE
            },
            onDismiss = { openDialog = SettingsDialog.NONE }
        )

        SettingsDialog.VOLUME -> SliderDialog(
            title = "Maximum Volume",
            initialValue = maxVolumePct.toFloat(),
            valueRange = 0f..100f,
            steps = 19,
            formatValue = { "${it.toInt()}%" },
            onConfirm = { value ->
                onMaxVolumeChanged(value.toInt())
                openDialog = SettingsDialog.NONE
            },
            onDismiss = { openDialog = SettingsDialog.NONE }
        )

        SettingsDialog.AUTO_STOP -> SliderDialog(
            title = "Auto Stop Duration",
            initialValue = autoStopSeconds.toFloat(),
            valueRange = 30f..120f,
            steps = 8,
            formatValue = { "${it.toInt()} seconds of quiet" },
            onConfirm = { value ->
                onAutoStopChanged(value.toInt())
                openDialog = SettingsDialog.NONE
            },
            onDismiss = { openDialog = SettingsDialog.NONE }
        )

        SettingsDialog.SOUND_FLOOR -> SliderDialog(
            title = "Sound Level Floor",
            initialValue = soundLevelFloorDb.toFloat(),
            valueRange = 30f..70f,
            steps = 7,
            formatValue = { "${it.toInt()} dB — adjust for your environment" },
            onConfirm = { value ->
                onSoundFloorChanged(value.toInt())
                openDialog = SettingsDialog.NONE
            },
            onDismiss = { openDialog = SettingsDialog.NONE }
        )

        SettingsDialog.NONE -> Unit
    }
}

@Composable
private fun OptionPickerDialog(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GlassDialog,
        titleContentColor = TextPrimary,
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
        text = {
            Column {
                options.forEachIndexed { index, option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .selectable(
                                selected = index == selectedIndex,
                                onClick = { onSelect(index) }
                            )
                            .padding(vertical = 12.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = index == selectedIndex,
                            onClick = { onSelect(index) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Primary,
                                unselectedColor = TextSecondary
                            )
                        )
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Close", color = Primary)
            }
        }
    )
}

@Composable
private fun SliderDialog(
    title: String,
    initialValue: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    formatValue: (Float) -> String,
    onConfirm: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    var sliderValue by remember { mutableFloatStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GlassDialog,
        titleContentColor = TextPrimary,
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
        text = {
            Column {
                Text(
                    text = formatValue(sliderValue),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    valueRange = valueRange,
                    steps = steps,
                    colors = SliderDefaults.colors(
                        thumbColor = Primary,
                        activeTrackColor = Primary,
                        inactiveTrackColor = TextDisabled
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(sliderValue) }) {
                Text(text = "Save", color = Primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel", color = TextSecondary)
            }
        }
    )
}

@Composable
private fun SettingsRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
