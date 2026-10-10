package com.owlen.app.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.owlen.app.presentation.ui.theme.SurfaceCard
import com.owlen.app.presentation.ui.theme.owlenCard

/** Soft rounded card with no border (depth from fill contrast). */
@Composable
fun OwlenCard(
    modifier: Modifier = Modifier,
    fill: Color = SurfaceCard,
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .owlenCard(fill = fill)
            .padding(contentPadding)
    ) {
        content()
    }
}
