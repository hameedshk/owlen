package com.owlen.app.presentation.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

/**
 * Owlen panel: soft fill with rounded corners and no border. Depth comes from
 * fill contrast, not stroke. An optional [tint] washes a translucent colour
 * over the fill — used for warning-state cards.
 */
fun Modifier.owlenCard(
    fill: Color = SurfaceCard,
    tint: Color = Color.Transparent,
    shape: Shape = RoundedCornerShape(OwlenRadius.card)
): Modifier = this
    .background(fill, shape)
    .background(tint, shape)

/**
 * One-shot fade + upward slide used to stagger content onto a screen.
 * Give later elements a larger [delayMillis] for a cascading entrance.
 */
@Composable
fun FadeSlideIn(
    delayMillis: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val visibleState = remember { MutableTransitionState(false).apply { targetState = true } }
    AnimatedVisibility(
        visibleState = visibleState,
        modifier = modifier,
        enter = fadeIn(tween(durationMillis = 450, delayMillis = delayMillis)) +
            slideInVertically(
                tween(durationMillis = 450, delayMillis = delayMillis, easing = FastOutSlowInEasing)
            ) { it / 4 }
    ) { content() }
}
