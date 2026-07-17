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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.Sensitivity
import com.owlen.app.domain.model.SoundPrototype
import com.owlen.app.presentation.ui.components.NeoPopButtonVariant
import com.owlen.app.presentation.ui.components.NeoPopDialog
import com.owlen.app.presentation.ui.components.NeoPopRadio
import com.owlen.app.presentation.ui.components.NeoPopRow
import com.owlen.app.presentation.ui.components.NeoPopRowDivider
import com.owlen.app.presentation.ui.components.SectionHeader
import com.owlen.app.presentation.ui.components.neoPopSliderColors
import com.owlen.app.presentation.ui.theme.FadeSlideIn
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.TextDisabled
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.neoPopCard

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
    customPrototypes: List<SoundPrototype> = emptyList(),
    onNavigateToEnrollment: () -> Unit = {},
    onDeletePrototype: (String) -> Unit = {},
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
    var prototypePendingDelete by remember { mutableStateOf<SoundPrototype?>(null) }

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "PREFERENCES",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
        }

        // Settings list card
        FadeSlideIn {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .neoPopCard()
        ) {
            SettingsRow(
                label = "Sleep Schedule",
                value = "$sleepFormatted – $wakeFormatted",
                onClick = onNavigateToSleepSchedule
            )
            NeoPopRowDivider()

            SettingsRow(
                label = "Protection Sound",
                value = preferredSound.displayName,
                onClick = { openDialog = SettingsDialog.SOUND }
            )
            NeoPopRowDivider()

            SettingsRow(
                label = "Maximum Volume",
                value = "$maxVolumePct%",
                onClick = { openDialog = SettingsDialog.VOLUME }
            )
            NeoPopRowDivider()

            SettingsRow(
                label = "Sensitivity",
                value = sensitivity.name.lowercase().replaceFirstChar { it.uppercase() },
                onClick = { openDialog = SettingsDialog.SENSITIVITY }
            )
            NeoPopRowDivider()

            SettingsRow(
                label = "Auto Stop",
                value = "$autoStopSeconds sec",
                onClick = { openDialog = SettingsDialog.AUTO_STOP }
            )
            NeoPopRowDivider()

            SettingsRow(
                label = "Sound Level Floor",
                value = "$soundLevelFloorDb dB",
                onClick = { openDialog = SettingsDialog.SOUND_FLOOR }
            )
            NeoPopRowDivider()

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
            NeoPopRowDivider()

            SettingsRow(
                label = "Battery Optimisation",
                value = if (batteryOptimisationAllowed) "Allowed" else "Restricted",
                onClick = onBatteryOptimisationClick
            )
            NeoPopRowDivider()

            SettingsRow(
                label = "About Owlen",
                value = "v$appVersion",
                onClick = onNavigateToAbout
            )
        }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Custom disturbances — user-enrolled sounds that trigger protection
        FadeSlideIn(delayMillis = 150) {
        Column {
        SectionHeader(
            text = "Custom Disturbances",
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .neoPopCard()
        ) {
            customPrototypes.forEach { prototype ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = prototype.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary
                        )
                        Text(
                            text = "${prototype.sampleCount} recordings",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                    IconButton(
                        onClick = { prototypePendingDelete = prototype },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete ${prototype.name}",
                            tint = TextSecondary
                        )
                    }
                }
                NeoPopRowDivider()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToEnrollment)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Add custom sound",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Primary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    prototypePendingDelete?.let { prototype ->
        NeoPopDialog(
            title = "Delete \"${prototype.name}\"?",
            onDismiss = { prototypePendingDelete = null },
            confirmText = "Delete",
            onConfirm = {
                onDeletePrototype(prototype.id)
                prototypePendingDelete = null
            },
            dismissText = "Cancel",
            confirmVariant = NeoPopButtonVariant.Danger
        ) {
            Text(
                text = "Owlen will stop reacting to this sound.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
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
    NeoPopDialog(
        title = title,
        onDismiss = onDismiss,
        dismissText = "Close"
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedIndex
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(selected = selected, onClick = { onSelect(index) })
                    .padding(vertical = 12.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NeoPopRadio(selected = selected)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selected) Primary else TextPrimary
                )
            }
        }
    }
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

    NeoPopDialog(
        title = title,
        onDismiss = onDismiss,
        confirmText = "Save",
        onConfirm = { onConfirm(sliderValue) },
        dismissText = "Cancel"
    ) {
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
            colors = neoPopSliderColors()
        )
    }
}

@Composable
private fun SettingsRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    NeoPopRow(
        title = label,
        onClick = onClick,
        trailing = {
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
    )
}
