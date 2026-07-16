package com.owlen.app.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.Sensitivity
import com.owlen.app.presentation.ui.theme.Background
import com.owlen.app.presentation.ui.theme.GlassFill
import com.owlen.app.presentation.ui.theme.GlassFillStrong
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.OnPrimary
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.glass
import com.owlen.app.presentation.ui.theme.glow
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
    // Keyed on initial values: DataStore loads async, so re-seed when they arrive
    var sleepHour by remember(initialSleepHour) { mutableIntStateOf(initialSleepHour) }
    var wakeHour by remember(initialWakeHour) { mutableIntStateOf(initialWakeHour) }
    var wakeMinute by remember(initialWakeMinute) { mutableIntStateOf(initialWakeMinute) }
    var selectedSound by remember(initialSound) { mutableStateOf(initialSound) }
    var selectedSensitivity by remember(initialSensitivity) { mutableStateOf(initialSensitivity) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(48.dp)
            ) {
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
                trackColor = GlassFillStrong
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        // Single scrollable page: all three settings, one Continue
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Sleep Time",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Wake Time",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Protection Sound",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This will be played when needed.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            MaskingSound.entries.forEach { sound ->
                SoundOptionRow(
                    label = sound.displayName,
                    selected = selectedSound == sound,
                    onClick = { selectedSound = sound }
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Sensitivity",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Controls how easily Owlen responds.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Sensitivity.entries.forEach { sensitivity ->
                SoundOptionRow(
                    label = sensitivity.name.lowercase().replaceFirstChar { it.uppercase() },
                    selected = selectedSensitivity == sensitivity,
                    onClick = { selectedSensitivity = sensitivity }
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Bottom button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 32.dp)
        ) {
            Button(
                onClick = {
                    onSaveSettings(sleepHour, wakeHour, wakeMinute, selectedSound, selectedSensitivity)
                    onNavigateToCalibration()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .glow(Primary, 20.dp, alpha = 0.22f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = OnPrimary
                )
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun TimePickerCard(
    hour: Int,
    minute: Int,
    isAm: Boolean,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    onAmPmChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .glass(RoundedCornerShape(12.dp), fill = GlassFillStrong)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hour picker
            NumberDrumPicker(
                value = if (hour == 0) 12 else hour,
                range = 1..12,
                onValueChange = onHourChange
            )

            Text(
                text = ":",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Minute picker
            NumberDrumPicker(
                value = minute,
                range = 0..59,
                onValueChange = onMinuteChange,
                formatWithLeadingZero = true
            )

            Spacer(modifier = Modifier.width(16.dp))

            // AM/PM toggle
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AmPmButton(label = "AM", selected = isAm, onClick = { onAmPmChange(true) })
                AmPmButton(label = "PM", selected = !isAm, onClick = { onAmPmChange(false) })
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NumberDrumPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    formatWithLeadingZero: Boolean = false
) {
    val items = range.toList()
    val initialIndex = items.indexOf(value).coerceAtLeast(0)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                onValueChange(items[index.coerceIn(0, items.size - 1)])
            }
    }

    LazyColumn(
        state = listState,
        flingBehavior = flingBehavior,
        modifier = Modifier
            .height(120.dp)
            .width(56.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items.size) { index ->
            val item = items[index]
            val displayText = if (formatWithLeadingZero) {
                item.toString().padStart(2, '0')
            } else {
                item.toString()
            }
            Box(
                modifier = Modifier
                    .size(56.dp, 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayText,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun AmPmButton(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Primary else GlassFillStrong)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) OnPrimary else TextSecondary
        )
    }
}

@Composable
private fun SoundOptionRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .glass(
                RoundedCornerShape(12.dp),
                fill = if (selected) Primary.copy(alpha = 0.08f) else GlassFill
            )
            .then(
                if (selected) Modifier.border(
                    width = 2.dp,
                    color = Primary,
                    shape = RoundedCornerShape(12.dp)
                ) else Modifier
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (selected) Primary else Background)
                    .border(2.dp, if (selected) Primary else TextSecondary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (selected) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(OnPrimary)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected) Primary else TextPrimary
            )
        }
    }
}

