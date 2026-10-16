package com.owlen.app.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.owlen.app.presentation.ui.theme.OnPrimary
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.Stroke
import com.owlen.app.presentation.ui.theme.SurfaceSunken
import com.owlen.app.presentation.ui.theme.TextSecondary

/** Amber-on-stroke slider styling shared by settings dialogs. */
@Composable
fun owlenSliderColors(): SliderColors = SliderDefaults.colors(
    thumbColor = Primary,
    activeTrackColor = Primary,
    inactiveTrackColor = Stroke
)

/** Circular Owlen radio indicator: amber dot springs in when selected. */
@Composable
fun OwlenRadio(
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val innerScale by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "radioInner"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) Primary else Stroke,
        animationSpec = tween(150),
        label = "radioBorder"
    )
    Box(
        modifier = modifier
            .size(18.dp)
            .border(1.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (innerScale > 0.01f) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .scale(innerScale)
                    .background(Primary, CircleShape)
            )
        }
    }
}

/**
 * Soft rounded segmented toggle (e.g. AM/PM): selected segment gets an amber
 * face with dark text, unselected segments sit transparent in the control's
 * own recessed well (so it reads against both plain screens and dialog/card
 * panels, which share the same fill as a plain pill). Selection change
 * animates via color crossfade.
 */
@Composable
fun OwlenSegmented(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(SurfaceSunken, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedIndex
            val segmentColor by animateColorAsState(
                targetValue = if (selected) Primary else Color.Transparent,
                animationSpec = tween(180),
                label = "segmentFill"
            )
            val textColor by animateColorAsState(
                targetValue = if (selected) OnPrimary else TextSecondary,
                animationSpec = tween(180),
                label = "segmentText"
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(segmentColor)
                    .clickable { onSelect(index) }
                    .padding(horizontal = 6.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor,
                    maxLines = 1
                )
            }
        }
    }
}
