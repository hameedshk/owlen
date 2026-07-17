package com.owlen.app.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.owlen.app.presentation.ui.theme.Stroke
import com.owlen.app.presentation.ui.theme.SurfaceCard
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.neoPopCard

/** Matte NeoPop panel with a hard 1dp stroke. */
@Composable
fun NeoPopCard(
    modifier: Modifier = Modifier,
    fill: Color = SurfaceCard,
    stroke: Color = Stroke,
    tint: Color = Color.Transparent,
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .neoPopCard(fill = fill, stroke = stroke, tint = tint)
            .padding(contentPadding)
    ) {
        content()
    }
}

/** Uppercase letter-spaced overline used above cards and lists. */
@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = TextSecondary,
        modifier = modifier
    )
}
