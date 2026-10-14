package com.owlen.app.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.BatteryFull
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.Sensitivity
import com.owlen.app.domain.model.SleepTimerSchedule
import com.owlen.app.domain.model.SoundPrototype
import com.owlen.app.presentation.ui.components.OptionPickerDialog
import com.owlen.app.presentation.ui.components.OwlenButtonVariant
import com.owlen.app.presentation.ui.components.OwlenCard
import com.owlen.app.presentation.ui.components.OwlenDialog
import com.owlen.app.presentation.ui.components.OwlenRow
import com.owlen.app.presentation.ui.components.OwlenRowDivider
import com.owlen.app.presentation.ui.components.OwlenScreenHeader
import com.owlen.app.presentation.ui.components.OwlenSwitchRow
import com.owlen.app.presentation.ui.components.SectionHeader
import com.owlen.app.presentation.ui.components.SliderDialog
import com.owlen.app.presentation.ui.theme.FadeSlideIn
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.util.formatHour12h
import com.owlen.app.presentation.util.formatTime12h

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
    schedule: SleepTimerSchedule = SleepTimerSchedule(),
    customPrototypes: List<SoundPrototype> = emptyList(),
    onNavigateToEnrollment: () -> Unit = {},
    onDeletePrototype: (String) -> Unit = {},
    onNavigateToSleepSchedule: () -> Unit = {},
    onNavigateToSleepWindow: () -> Unit = {},
    onNavigateToSleepTimer: () -> Unit = {},
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

    val scheduleValue = if (schedule.enabled) {
        "${formatTime12h(schedule.startHour, schedule.startMinute)} – " +
            formatTime12h(schedule.endHour, schedule.endMinute)
    } else {
        "Off"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        OwlenScreenHeader(title = "Settings", overline = "Preferences")

        // Playback — what Owlen plays on purpose, with no monitoring involved
        FadeSlideIn {
            Column {
                SettingsSection("Playback")
                OwlenCard(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    contentPadding = 0.dp
                ) {
                    SettingsNavRow(
                        label = "Sleep Schedule",
                        value = scheduleValue,
                        icon = Icons.Rounded.Bedtime,
                        onClick = onNavigateToSleepSchedule
                    )
                    OwlenRowDivider()
                    SettingsNavRow(
                        label = "Sleep Timer",
                        value = "Play now",
                        icon = Icons.Rounded.Timer,
                        onClick = onNavigateToSleepTimer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Monitoring window — when reactive protection is allowed to arm
        FadeSlideIn(delayMillis = 80) {
            Column {
                SettingsSection("Sleep Window")
                OwlenCard(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    contentPadding = 0.dp
                ) {
                    SettingsNavRow(
                        label = "Monitoring Window",
                        value = "${formatHour12h(sleepHour)} – ${formatHour12h(wakeHour)}",
                        icon = Icons.Rounded.WbTwilight,
                        onClick = onNavigateToSleepWindow
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Protection — how Owlen reacts to disturbances
        FadeSlideIn(delayMillis = 150) {
            Column {
                SettingsSection("Protection")
                OwlenCard(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    contentPadding = 0.dp
                ) {
                    SettingsNavRow(
                        label = "Protection Sound",
                        value = preferredSound.displayName,
                        icon = Icons.Rounded.MusicNote,
                        onClick = { openDialog = SettingsDialog.SOUND }
                    )
                    OwlenRowDivider()
                    SettingsNavRow(
                        label = "Maximum Volume",
                        value = "$maxVolumePct%",
                        icon = Icons.AutoMirrored.Rounded.VolumeUp,
                        onClick = { openDialog = SettingsDialog.VOLUME }
                    )
                    OwlenRowDivider()
                    SettingsNavRow(
                        label = "Sensitivity",
                        value = sensitivity.name.lowercase().replaceFirstChar { it.uppercase() },
                        icon = Icons.Rounded.Tune,
                        onClick = { openDialog = SettingsDialog.SENSITIVITY }
                    )
                    OwlenRowDivider()
                    SettingsNavRow(
                        label = "Auto Stop",
                        value = "$autoStopSeconds sec",
                        icon = Icons.Rounded.Timer,
                        onClick = { openDialog = SettingsDialog.AUTO_STOP }
                    )
                    OwlenRowDivider()
                    SettingsNavRow(
                        label = "Sound Level Floor",
                        value = "$soundLevelFloorDb dB",
                        icon = Icons.Rounded.GraphicEq,
                        onClick = { openDialog = SettingsDialog.SOUND_FLOOR }
                    )
                    OwlenRowDivider()
                    OwlenSwitchRow(
                        title = "Soothing Rain",
                        subtitle = "Treat rain as soothing — never trigger masking",
                        checked = rainBehaviourEnabled,
                        onCheckedChange = onRainBehaviourChanged,
                        leading = { SettingsRowIcon(Icons.Rounded.WaterDrop) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Custom disturbances — user-enrolled sounds that trigger protection
        FadeSlideIn(delayMillis = 220) {
            Column {
                SettingsSection("Custom Disturbances")
                OwlenCard(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    contentPadding = 0.dp
                ) {
                    customPrototypes.forEach { prototype ->
                        OwlenRow(
                            title = prototype.name,
                            subtitle = "${prototype.sampleCount} recordings",
                            trailing = {
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
                        )
                        OwlenRowDivider()
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

        FadeSlideIn(delayMillis = 290) {
            Column {
                SettingsSection("System")
                OwlenCard(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    contentPadding = 0.dp
                ) {
                    SettingsNavRow(
                        label = "Battery Optimisation",
                        value = if (batteryOptimisationAllowed) "Allowed" else "Restricted",
                        icon = Icons.Rounded.BatteryFull,
                        onClick = onBatteryOptimisationClick
                    )
                    OwlenRowDivider()
                    SettingsNavRow(
                        label = "About Owlen",
                        value = "v$appVersion",
                        icon = Icons.Rounded.Info,
                        onClick = onNavigateToAbout
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    prototypePendingDelete?.let { prototype ->
        OwlenDialog(
            title = "Delete \"${prototype.name}\"?",
            onDismiss = { prototypePendingDelete = null },
            confirmText = "Delete",
            onConfirm = {
                onDeletePrototype(prototype.id)
                prototypePendingDelete = null
            },
            dismissText = "Cancel",
            confirmVariant = OwlenButtonVariant.Danger
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
            options = MaskingSound.entries.map { it.displayName },
            selectedIndex = MaskingSound.entries.indexOf(preferredSound),
            onSelect = { index ->
                onSoundSelected(MaskingSound.entries[index])
                openDialog = SettingsDialog.NONE
            },
            onDismiss = { openDialog = SettingsDialog.NONE }
        )

        SettingsDialog.SENSITIVITY -> OptionPickerDialog(
            title = "Sensitivity",
            options = Sensitivity.entries.map {
                it.name.lowercase().replaceFirstChar { c -> c.uppercase() }
            },
            selectedIndex = Sensitivity.entries.indexOf(sensitivity),
            onSelect = { index ->
                onSensitivitySelected(Sensitivity.entries[index])
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
private fun SettingsSection(text: String) {
    SectionHeader(
        text = text,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsNavRow(
    label: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    OwlenRow(
        title = label,
        leading = { SettingsRowIcon(icon) },
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

@Composable
private fun SettingsRowIcon(
    icon: ImageVector,
    tint: Color = TextPrimary
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(20.dp)
    )
}
