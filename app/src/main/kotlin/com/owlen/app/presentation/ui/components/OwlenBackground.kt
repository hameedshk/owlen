package com.owlen.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.owlen.app.presentation.ui.theme.Background

/**
 * App-wide matte backdrop: solid near-black fill, no texture.
 * Deliberately animation-free (night battery use).
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
