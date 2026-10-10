package com.owlen.app.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.Sensitivity
import com.owlen.app.presentation.ui.components.OwlenButton
import com.owlen.app.presentation.ui.components.OwlenRadio
import com.owlen.app.presentation.ui.components.OwlenRow
import com.owlen.app.presentation.ui.components.OwlenRowDivider
import com.owlen.app.presentation.ui.components.OwlenSegmented
import com.owlen.app.presentation.ui.components.NumberDrumPicker
import com.owlen.app.presentation.ui.components.SectionHeader
import com.owlen.app.presentation.ui.components.TimePickerCard
import com.owlen.app.presentation.ui.theme.FadeSlideIn
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.Stroke
import com.owlen.app.presentation.ui.theme.SurfaceSunken
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.neoPopCard
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun SetupScreen(
    initialSleepHour: Int = 22,
    initialWakeHour: Int = 7,
    initialWakeMinute: Int = 0,
    initialSound: MaskingSound = MaskingSound.BROWN_NOISE,
    initialSensitivity: Sensitivity = Sensitivity.MEDIUM,
    onNavigateBack: () -> Unit,
    onNavigateToCalibration: () -> Unit,
    onSaveSettings: (sleepHour: Int, wakeHour: Int, wakeMinute: Int, sound: MaskingSound, sensitivity: Sensitivity) -> Unit = { _, _, _, _, _ -> }
) {
    var sleepHour by remember(initialSleepHour) { mutableIntStateOf(initialSleepHour) }
    var wakeHour by remember(initialWakeHour) { mutableIntStateOf(initialWakeHour) }
    var wakeMinute by remember(initialWakeMinute) { mutableIntStateOf(initialWakeMinute) }
    var selectedSound by remember(initialSound) { mutableStateOf(initialSound) }
    var selectedSensitivity by remember(initialSensitivity) { mutableStateOf(initialSensitivity) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            LinearProgressIndicator(
                progress = { 0.66f },
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = Green,
                trackColor = Stroke
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            FadeSlideIn {
                Column {
                    SectionHeader(text = "Sleep Time")
                    Spacer(modifier = Modifier.height(12.dp))
                    TimePickerCard(
                        hour = sleepHour % 12,
                        minute = 0,
                        isAm = sleepHour < 12,
                        onHourChange = { h ->
                            val pm = sleepHour >= 12
                            sleepHour = if (pm) (h % 12) + 12 else h % 12
                        },
                        onMinuteChange = {},
                        onAmPmChange = { isAm ->
                            sleepHour = if (!isAm) (sleepHour % 12) + 12 else sleepHour % 12
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            FadeSlideIn(delayMillis = 100) {
                Column {
                    SectionHeader(text = "Wake Time")
                    Spacer(modifier = Modifier.height(12.dp))
                    TimePickerCard(
                        hour = wakeHour % 12,
                        minute = wakeMinute,
                        isAm = wakeHour < 12,
                        onHourChange = { h ->
                            val pm = wakeHour >= 12
                            wakeHour = if (!pm) h % 12 else (h % 12) + 12
                        },
                        onMinuteChange = { wakeMinute = it },
                        onAmPmChange = { isAm ->
                            wakeHour = if (!isAm) (wakeHour % 12) + 12 else wakeHour % 12
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            FadeSlideIn(delayMillis = 200) {
                Column {
                    SectionHeader(text = "Protection Sound")
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "This will be played when needed.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(modifier = Modifier.fillMaxWidth().neoPopCard()) {
                        MaskingSound.entries.forEachIndexed { index, sound ->
                            OwlenRow(
                                title = sound.displayName,
                                selected = selectedSound == sound,
                                onClick = { selectedSound = sound },
                                leading = { OwlenRadio(selected = selectedSound == sound) }
                            )
                            if (index < MaskingSound.entries.size - 1) OwlenRowDivider()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            FadeSlideIn(delayMillis = 300) {
                Column {
                    SectionHeader(text = "Sensitivity")
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Controls how easily Owlen responds.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(modifier = Modifier.fillMaxWidth().neoPopCard()) {
                        Sensitivity.entries.forEachIndexed { index, sensitivity ->
                            OwlenRow(
                                title = sensitivity.name.lowercase().replaceFirstChar { it.uppercase() },
                                selected = selectedSensitivity == sensitivity,
                                onClick = { selectedSensitivity = sensitivity },
                                leading = { OwlenRadio(selected = selectedSensitivity == sensitivity) }
                            )
                            if (index < Sensitivity.entries.size - 1) OwlenRowDivider()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 32.dp)
        ) {
            OwlenButton(
                text = "Continue",
                onClick = {
                    onSaveSettings(sleepHour, wakeHour, wakeMinute, selectedSound, selectedSensitivity)
                    onNavigateToCalibration()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

