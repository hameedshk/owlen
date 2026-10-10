package com.owlen.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.owlen.app.presentation.ui.theme.Background
import com.owlen.app.presentation.ui.theme.DotGrid

/**
 * App-wide matte backdrop: solid near-black fill. Deliberately animation-free (night battery use).
 * No dot grid in the new design.
 */
@Composable
fun OwlenBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        content()
    }
}

/**
 * Legacy: MatteBackground with dot grid. Kept for backward compatibility during migration.
 * Use OwlenBackground for new screens.
 */
@Composable
fun MatteBackground(
    modifier: Modifier = Modifier,
    showGrid: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .drawBehind {
                if (!showGrid) return@drawBehind
                val spacing = 24.dp.toPx()
                val dotRadius = 1.dp.toPx()
                var y = spacing
                while (y < size.height) {
                    var x = spacing
                    while (x < size.width) {
                        drawCircle(DotGrid, dotRadius, Offset(x, y))
                        x += spacing
                    }
                    y += spacing
                }
            }
    ) {
        content()
    }
}
