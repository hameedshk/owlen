package com.owlen.app.presentation.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.presentation.sleeptimer.SleepTimerViewModel
import com.owlen.app.presentation.ui.components.OptionPickerDialog
import com.owlen.app.presentation.ui.components.OwlenButton
import com.owlen.app.presentation.ui.components.OwlenButtonVariant
import com.owlen.app.presentation.ui.components.OwlenCard
import com.owlen.app.presentation.ui.components.OwlenInfoCard
import com.owlen.app.presentation.ui.components.OwlenRow
import com.owlen.app.presentation.ui.components.OwlenRowDivider
import com.owlen.app.presentation.ui.components.OwlenScreenHeader
import com.owlen.app.presentation.ui.components.RangePickerDialog
import com.owlen.app.presentation.ui.components.SoundHeroCard
import com.owlen.app.presentation.ui.theme.FadeSlideIn
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.Stroke
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.util.formatDuration
import com.owlen.app.service.ServiceState

private const val PREVIEW_DURATION_MS = 8_000L

private enum class TimerDialog { NONE, SOUND, DURATION, FADE_OUT }

@Composable
fun SleepTimerScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: SleepTimerViewModel = hiltViewModel()
) {
    val serviceState by viewModel.serviceState.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val config by viewModel.timerConfig.collectAsState()
    val isProtectionRunning by viewModel.isProtectionRunning.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val isPreviewPlaying by viewModel.isPreviewPlaying.collectAsState()
    android.util.Log.d(
        "OwlenDiag",
        "COMPOSE recompose isPreviewPlaying=$isPreviewPlaying isTimerRunning=$isTimerRunning " +
            "serviceState=$serviceState"
    )

    var openDialog by remember { mutableStateOf(TimerDialog.NONE) }

    val timerState = (serviceState as? ServiceState.TimedPlaybackActive)?.takeIf { isTimerRunning }
    val volumePct = (settings.maxVolume * 100).toInt()

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            OwlenScreenHeader(
                title = "Sleep Timer",
                overline = "Play now",
                onNavigateBack = onNavigateBack
            )

            FadeSlideIn {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SoundHeroCard(
                        sound = config.sound,
                        label = "Timer sound",
                        onClick = {
                            // Changing the sound mid-run would silently apply only to the
                            // *next* timer — the live playback keeps using the old config.
                            if (!isTimerRunning) openDialog = TimerDialog.SOUND
                        },
                        onLongPress = {
                            // Long-press to preview the sound
                            val previewEnabled = !isProtectionRunning && !isTimerRunning
                            if (previewEnabled && !isPreviewPlaying) {
                                viewModel.startPreview(
                                    sound = config.sound,
                                    durationMs = PREVIEW_DURATION_MS,
                                    volume = settings.maxVolume
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OwlenCard(contentPadding = 0.dp) {
                        OwlenRow(
                            title = "Duration",
                            value = formatDuration(config.durationMinutes),
                            leading = { RowIcon(Icons.Rounded.HourglassEmpty) },
                            onClick = if (isTimerRunning) null else { { openDialog = TimerDialog.DURATION } }
                        )
                        OwlenRowDivider()
                        OwlenRow(
                            title = "Fade out over",
                            value = if (config.fadeOutMinutes == 0) {
                                "Off"
                            } else {
                                formatDuration(config.fadeOutMinutes)
                            },
                            leading = { RowIcon(Icons.Rounded.GraphicEq) },
                            onClick = if (isTimerRunning) null else { { openDialog = TimerDialog.FADE_OUT } }
                        )
                        OwlenRowDivider()
                        OwlenRow(
                            title = "Volume",
                            subtitle = "Change under Settings → Maximum Volume",
                            value = "$volumePct%",
                            leading = { RowIcon(Icons.AutoMirrored.Rounded.VolumeUp) }
                        )
                    }

                    if (isTimerRunning) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Duration and fade-out are locked while the timer is running — " +
                                "stop it to change them.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            FadeSlideIn(delayMillis = 100) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    // Preview shares the single playback pipeline, so it cannot run
                    // while monitoring or a timer already owns it.
                    val previewEnabled = !isProtectionRunning && !isTimerRunning

                    OwlenCard(contentPadding = 0.dp) {
                        OwlenRow(
                            title = if (isPreviewPlaying) "Stop preview" else "Preview sound",
                            subtitle = when {
                                isProtectionRunning -> "Unavailable while protection is running"
                                isTimerRunning -> "Unavailable while the timer is running"
                                isPreviewPlaying -> "Playing ${config.sound.displayName}…"
                                else -> "Hear ${config.sound.displayName} for 8 seconds"
                            },
                            leading = {
                                RowIcon(
                                    if (isPreviewPlaying) Icons.Rounded.Stop else Icons.Rounded.PlayArrow,
                                    tint = if (previewEnabled) Primary else TextSecondary
                                )
                            },
                            onClick = if (previewEnabled) {
                                {
                                    if (isPreviewPlaying) {
                                        viewModel.stopPreview()
                                    } else {
                                        viewModel.startPreview(
                                            sound = config.sound,
                                            durationMs = PREVIEW_DURATION_MS,
                                            volume = settings.maxVolume
                                        )
                                    }
                                }
                            } else {
                                null
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OwlenInfoCard(
                        text = when {
                            config.fadeOutMinutes == 0 ->
                                "${config.sound.displayName} plays at $volumePct% for " +
                                    "${formatDuration(config.durationMinutes)}, then stops."
                            config.fadeOutMinutes >= config.durationMinutes ->
                                "${config.sound.displayName} eases down to silence for the entire " +
                                    "${formatDuration(config.durationMinutes)} — it never holds at " +
                                    "full volume with fade-out set this long."
                            else ->
                                "${config.sound.displayName} plays for " +
                                    "${formatDuration(config.durationMinutes)}, easing down to silence " +
                                    "over the last ${formatDuration(config.fadeOutMinutes)}."
                        }
                    )
                }
            }

            if (timerState != null) {
                Spacer(modifier = Modifier.height(20.dp))
                TimerCountdown(
                    remainingMs = timerState.remainingMs,
                    totalDurationMs = timerState.totalDurationMs,
                    volume = timerState.volume,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Fixed footer: Start/Stop stays on screen without scrolling, so the
        // control is never hidden below the countdown card mid-timer.
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 24.dp)
        ) {
            when {
                isTimerRunning -> OwlenButton(
                    text = "Stop Timer",
                    onClick = { viewModel.stopTimer() },
                    variant = OwlenButtonVariant.Danger,
                    modifier = Modifier.fillMaxWidth()
                )

                isProtectionRunning -> {
                    OwlenButton(
                        text = "Start Timer",
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Sleep protection is running. Stop it to use the timer.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                else -> OwlenButton(
                    text = "Start Timer",
                    onClick = {
                        viewModel.startTimer(
                            sound = config.sound,
                            durationMs = config.durationMinutes * 60_000L,
                            targetVolume = settings.maxVolume,
                            fadeOutMs = config.fadeOutMinutes * 60_000L
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    when (openDialog) {
        TimerDialog.SOUND -> OptionPickerDialog(
            title = "Timer Sound",
            options = MaskingSound.entries.map { it.displayName },
            selectedIndex = MaskingSound.entries.indexOf(config.sound),
            onSelect = { index ->
                viewModel.saveTimerConfig(config.copy(sound = MaskingSound.entries[index]))
                openDialog = TimerDialog.NONE
            },
            onDismiss = { openDialog = TimerDialog.NONE }
        )

        TimerDialog.DURATION -> RangePickerDialog(
            title = "Duration",
            range = 5..180,
            value = config.durationMinutes,
            unitLabel = "minutes",
            onConfirm = { minutes ->
                // Fade-out can never outlast the timer itself
                viewModel.saveTimerConfig(
                    config.copy(
                        durationMinutes = minutes,
                        fadeOutMinutes = config.fadeOutMinutes.coerceAtMost(minutes)
                    )
                )
                openDialog = TimerDialog.NONE
            },
            onDismiss = { openDialog = TimerDialog.NONE }
        )

        TimerDialog.FADE_OUT -> RangePickerDialog(
            title = "Fade Out Over",
            range = 0..config.durationMinutes.coerceAtMost(60),
            value = config.fadeOutMinutes,
            unitLabel = "minutes",
            onConfirm = { minutes ->
                viewModel.saveTimerConfig(config.copy(fadeOutMinutes = minutes))
                openDialog = TimerDialog.NONE
            },
            onDismiss = { openDialog = TimerDialog.NONE }
        )

        TimerDialog.NONE -> Unit
    }
}

@Composable
private fun RowIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: androidx.compose.ui.graphics.Color = TextSecondary
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(20.dp)
    )
}

@Composable
private fun TimerCountdown(
    remainingMs: Long,
    totalDurationMs: Long,
    volume: Float,
    modifier: Modifier = Modifier
) {
    OwlenCard(modifier = modifier) {
        Text(
            text = "TIME REMAINING",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = formatRemaining(remainingMs),
            style = MaterialTheme.typography.headlineMedium,
            color = Primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        val progress = if (totalDurationMs > 0L) {
            (1f - remainingMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Stroke)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Primary)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Volume",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Text(
                text = "${(volume * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
        }
    }
}

private fun formatRemaining(remainingMs: Long): String {
    val totalSeconds = (remainingMs / 1000L).coerceAtLeast(0L)
    val hours = totalSeconds / 3600L
    val minutes = (totalSeconds % 3600L) / 60L
    val seconds = totalSeconds % 60L
    return if (hours > 0L) {
        "$hours:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    } else {
        "$minutes:${seconds.toString().padStart(2, '0')}"
    }
}
