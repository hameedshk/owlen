package com.owlen.app.presentation.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.GreenEdge
import com.owlen.app.presentation.ui.theme.GreenEdgeDeep
import com.owlen.app.presentation.ui.theme.NeoPopPlate
import com.owlen.app.presentation.ui.theme.OnPrimary
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.PrimaryEdge
import com.owlen.app.presentation.ui.theme.PrimaryEdgeDeep
import com.owlen.app.presentation.ui.theme.Safety
import com.owlen.app.presentation.ui.theme.SafetyEdge
import com.owlen.app.presentation.ui.theme.SafetyEdgeDeep
import com.owlen.app.presentation.ui.theme.Stroke
import com.owlen.app.presentation.ui.theme.StrokeBright
import com.owlen.app.presentation.ui.theme.SurfaceCard
import com.owlen.app.presentation.ui.theme.TextDisabled
import com.owlen.app.presentation.ui.theme.TextPrimary

enum class NeoPopButtonVariant { Primary, Secondary, Danger, Success }

private class ButtonStyle(
    val face: Color,
    val edgeRight: Color,
    val edgeBottom: Color,
    val stroke: Color,
    val text: Color,
    val strokedEdges: Boolean
)

/**
 * Standard NeoPop CTA: 52dp face over 6dp plunk edges (58dp total height).
 * Label is always rendered uppercase.
 */
@Composable
fun NeoPopButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: NeoPopButtonVariant = NeoPopButtonVariant.Primary,
    enabled: Boolean = true
) {
    val style = when (variant) {
        NeoPopButtonVariant.Primary ->
            ButtonStyle(Primary, PrimaryEdge, PrimaryEdgeDeep, PrimaryEdge, OnPrimary, false)
        NeoPopButtonVariant.Secondary ->
            ButtonStyle(SurfaceCard, Stroke, Stroke, StrokeBright, TextPrimary, true)
        NeoPopButtonVariant.Danger ->
            ButtonStyle(Safety, SafetyEdge, SafetyEdgeDeep, SafetyEdge, OnPrimary, false)
        NeoPopButtonVariant.Success ->
            ButtonStyle(Green, GreenEdge, GreenEdgeDeep, GreenEdge, OnPrimary, false)
    }

    NeoPopPlate(
        onClick = onClick,
        modifier = modifier.height(58.dp),
        faceColor = style.face,
        edgeRight = style.edgeRight,
        edgeBottom = style.edgeBottom,
        strokeColor = if (enabled) style.stroke else Stroke,
        depth = 6.dp,
        enabled = enabled,
        strokedEdges = style.strokedEdges,
        shimmer = variant == NeoPopButtonVariant.Primary
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = if (enabled) style.text else TextDisabled
        )
    }
}
