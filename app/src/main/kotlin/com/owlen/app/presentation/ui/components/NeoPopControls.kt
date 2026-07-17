package com.owlen.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.owlen.app.presentation.ui.theme.OnPrimary
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.Stroke
import com.owlen.app.presentation.ui.theme.StrokeBright
import com.owlen.app.presentation.ui.theme.SurfaceSunken
import com.owlen.app.presentation.ui.theme.TextSecondary

/** Amber-on-stroke slider styling shared by settings dialogs. */
@Composable
fun neoPopSliderColors(): SliderColors = SliderDefaults.colors(
    thumbColor = Primary,
    activeTrackColor = Primary,
    inactiveTrackColor = Stroke
)

/** Square NeoPop radio/check indicator: amber inner square when selected. */
@Composable
fun NeoPopRadio(
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(18.dp)
            .border(1.dp, if (selected) Primary else StrokeBright, RectangleShape),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Primary, RectangleShape)
            )
        }
    }
}

/**
 * Hard-edged segmented toggle (e.g. AM/PM): selected segment gets an amber
 * face with dark text, unselected segments sit in a sunken well.
 */
@Composable
fun NeoPopSegmented(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(SurfaceSunken, RectangleShape)
            .border(1.dp, Stroke, RectangleShape)
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .background(if (selected) Primary else SurfaceSunken, RectangleShape)
                    .clickable { onSelect(index) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected) OnPrimary else TextSecondary
                )
            }
        }
    }
}
