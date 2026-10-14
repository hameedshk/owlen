package com.owlen.app.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.SurfaceSunken
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.owlenCard
import com.owlen.app.presentation.util.isAm
import com.owlen.app.presentation.util.to12Hour
import com.owlen.app.presentation.util.to24Hour
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlin.math.roundToInt

@Composable
internal fun OptionPickerDialog(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    OwlenDialog(
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
                OwlenRadio(selected = selected)
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
internal fun SliderDialog(
    title: String,
    initialValue: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    formatValue: (Float) -> String,
    onConfirm: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    var sliderValue by remember { mutableFloatStateOf(initialValue) }

    OwlenDialog(
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
            colors = owlenSliderColors()
        )
    }
}

/**
 * Inline hour/minute/AM-PM drum row, used by the setup flow where the pickers
 * live directly on the screen rather than inside a dialog.
 */
@Composable
internal fun TimePickerCard(
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
            .owlenCard()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HourMinuteDrums(
                hour12 = if (hour == 0) 12 else hour,
                minute = minute,
                onHour12Change = onHourChange,
                onMinuteChange = onMinuteChange
            )

            Spacer(modifier = Modifier.width(16.dp))

            OwlenSegmented(
                options = listOf("AM", "PM"),
                selectedIndex = if (isAm) 0 else 1,
                onSelect = { index -> onAmPmChange(index == 0) }
            )
        }
    }
}

/**
 * 12-hour time picker dialog. Reports the confirmed time as a 24-hour hour
 * plus minute, so callers never deal with AM/PM themselves.
 */
@Composable
internal fun TimePickerDialog(
    title: String,
    hour: Int,
    minute: Int,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var pendingHour12 by remember { mutableIntStateOf(to12Hour(hour)) }
    var pendingMinute by remember { mutableIntStateOf(minute) }
    var pendingAm by remember { mutableStateOf(isAm(hour)) }

    OwlenDialog(
        title = title,
        onDismiss = onDismiss,
        confirmText = "Save",
        onConfirm = { onConfirm(to24Hour(pendingHour12, pendingAm), pendingMinute) },
        dismissText = "Cancel"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HourMinuteDrums(
                hour12 = pendingHour12,
                minute = pendingMinute,
                onHour12Change = { pendingHour12 = it },
                onMinuteChange = { pendingMinute = it }
            )

            Spacer(modifier = Modifier.width(16.dp))

            OwlenSegmented(
                options = listOf("AM", "PM"),
                selectedIndex = if (pendingAm) 0 else 1,
                onSelect = { index -> pendingAm = index == 0 }
            )
        }
    }
}

/** Single-value drum in a dialog, with Cancel/Save so a stray scroll is recoverable. */
@Composable
internal fun RangePickerDialog(
    title: String,
    range: IntRange,
    value: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
    formatWithLeadingZero: Boolean = false,
    unitLabel: String? = null
) {
    var pendingValue by remember { mutableIntStateOf(value) }

    OwlenDialog(
        title = title,
        onDismiss = onDismiss,
        confirmText = "Save",
        onConfirm = { onConfirm(pendingValue) },
        dismissText = "Cancel"
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NumberDrumPicker(
                value = value,
                range = range,
                onValueChange = { pendingValue = it },
                formatWithLeadingZero = formatWithLeadingZero
            )
            if (unitLabel != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = unitLabel,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun HourMinuteDrums(
    hour12: Int,
    minute: Int,
    onHour12Change: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .background(SurfaceSunken, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NumberDrumPicker(
            value = hour12,
            range = 1..12,
            onValueChange = onHour12Change
        )

        Text(
            text = ":",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        NumberDrumPicker(
            value = minute,
            range = 0..59,
            onValueChange = onMinuteChange,
            formatWithLeadingZero = true
        )
    }
}

private val DrumRowHeight = 40.dp
private val DrumViewportHeight = DrumRowHeight * 3

/**
 * Scrolling number drum. A blank slot pads each end so the selected value always
 * sits in the middle row, and the value is reported only once the drum settles —
 * a fling emits one change, not every row it travels past.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NumberDrumPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    formatWithLeadingZero: Boolean = false
) {
    val items = remember(range) { range.toList() }
    val rowHeightPx = with(LocalDensity.current) { DrumRowHeight.toPx() }

    val initialIndex = items.indexOf(value).coerceAtLeast(0)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // Slot i holds value i-1 (slot 0 is the leading blank), so the value under the
    // centre band is exactly the absolute scroll distance measured in rows.
    val centeredIndex by remember(items, rowHeightPx) {
        derivedStateOf {
            val scrolled = listState.firstVisibleItemIndex * rowHeightPx +
                listState.firstVisibleItemScrollOffset
            (scrolled / rowHeightPx).roundToInt().coerceIn(0, items.lastIndex)
        }
    }

    LaunchedEffect(items) {
        snapshotFlow { listState.isScrollInProgress to centeredIndex }
            .filter { (scrolling, _) -> !scrolling }
            .map { (_, index) -> items[index] }
            .distinctUntilChanged()
            .collect { settled -> onValueChange(settled) }
    }

    Box(
        modifier = modifier
            .height(DrumViewportHeight)
            .width(56.dp),
        contentAlignment = Alignment.Center
    ) {
        // Centre band marks the live row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(DrumRowHeight)
                .background(Primary.copy(alpha = 0.10f), RoundedCornerShape(8.dp))
        )

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier.height(DrumViewportHeight),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(DrumRowHeight)) }

            items(items.size) { index ->
                val item = items[index]
                val displayText = if (formatWithLeadingZero) {
                    item.toString().padStart(2, '0')
                } else {
                    item.toString()
                }
                Box(
                    modifier = Modifier.size(56.dp, DrumRowHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = displayText,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (index == centeredIndex) {
                            Primary
                        } else {
                            TextSecondary.copy(alpha = 0.5f)
                        },
                        textAlign = TextAlign.Center
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(DrumRowHeight)) }
        }
    }
}
