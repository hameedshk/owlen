package com.owlen.app.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.Stroke
import com.owlen.app.presentation.ui.theme.SurfaceSunken
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.neoPopCard
import kotlinx.coroutines.flow.distinctUntilChanged

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
            .neoPopCard()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .background(SurfaceSunken, RectangleShape)
                    .border(1.dp, Stroke, RectangleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NumberDrumPicker(
                    value = if (hour == 0) 12 else hour,
                    range = 1..12,
                    onValueChange = onHourChange
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

            Spacer(modifier = Modifier.width(16.dp))

            OwlenSegmented(
                options = listOf("AM", "PM"),
                selectedIndex = if (isAm) 0 else 1,
                onSelect = { index -> onAmPmChange(index == 0) }
            )
        }
    }
}

@Composable
internal fun RangePickerDialog(
    title: String,
    range: IntRange,
    value: Int,
    formatWithLeadingZero: Boolean = false,
    onValueChange: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    OwlenDialog(
        title = title,
        onDismiss = onDismiss,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                NumberDrumPicker(
                    value = value,
                    range = range,
                    onValueChange = onValueChange,
                    formatWithLeadingZero = formatWithLeadingZero
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NumberDrumPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    formatWithLeadingZero: Boolean = false
) {
    val items = range.toList()
    val initialIndex = items.indexOf(value).coerceAtLeast(0)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val selectedIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index: Int ->
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
                modifier = Modifier.size(56.dp, 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayText,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (index == selectedIndex) Primary
                    else TextSecondary.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
