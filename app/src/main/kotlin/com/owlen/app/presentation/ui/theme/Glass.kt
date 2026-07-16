package com.owlen.app.presentation.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sin
import kotlin.random.Random

/**
 * Frosted glass panel: translucent tinted fill, top-edge sheen, and a 1dp
 * light-catching gradient border. Simulated glassmorphism — no blur APIs,
 * renders identically on every supported API level (minSdk 26).
 */
fun Modifier.glass(
    shape: Shape,
    fill: Color = GlassFill,
    borderAlpha: Float = 0.14f
): Modifier = this
    .clip(shape)
    .background(GlassTint.copy(alpha = 0.35f), shape)
    .background(fill, shape)
    .background(
        Brush.verticalGradient(
            0f to GlassHighlight,
            0.4f to Color.Transparent,
            1f to Color.Transparent
        ),
        shape
    )
    .border(
        width = 1.dp,
        brush = Brush.linearGradient(
            0f to Color.White.copy(alpha = borderAlpha),
            1f to GlassBorderDim
        ),
        shape = shape
    )

/**
 * Soft radial halo behind an element. [radius] is how far the glow spreads
 * beyond the element's own bounds.
 */
fun Modifier.glow(
    color: Color,
    radius: Dp,
    alpha: Float = 0.30f
): Modifier = this.drawBehind {
    val spread = size.maxDimension / 2f + radius.toPx()
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(color.copy(alpha = alpha), Color.Transparent),
            center = center,
            radius = spread
        ),
        radius = spread,
        center = center
    )
}

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

private class Star(
    val x: Float,
    val y: Float,
    val radiusDp: Float,
    val phase: Float,
    val speed: Float
)

private fun buildStars(count: Int): List<Star> {
    val rnd = Random(42)
    return List(count) {
        Star(
            x = rnd.nextFloat(),
            y = rnd.nextFloat() * 0.75f,
            radiusDp = 0.6f + rnd.nextFloat() * 1.1f,
            phase = rnd.nextFloat() * 6.2832f,
            speed = 0.5f + rnd.nextFloat() * 1.5f
        )
    }
}

private fun DrawScope.drawOrbs(intensity: Float, drift: Float, breath: Float) {
    // Amber glow — top-right, mostly off-canvas
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(
                Primary.copy(alpha = 0.06f * intensity * breath),
                Color.Transparent
            ),
            center = Offset(
                size.width * (1.05f - 0.08f * drift),
                size.height * (-0.05f + 0.06f * drift)
            ),
            radius = size.width * 0.95f
        )
    )
    // Indigo glow — bottom-left
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(
                GlowIndigo.copy(alpha = 0.09f * intensity * breath),
                Color.Transparent
            ),
            center = Offset(
                size.width * (-0.10f + 0.07f * drift),
                size.height * (0.90f - 0.05f * drift)
            ),
            radius = size.width * 1.1f
        )
    )
    // Faint indigo wash — upper-left, ties the two together
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(
                GlowIndigo.copy(alpha = 0.05f * intensity),
                Color.Transparent
            ),
            center = Offset(
                size.width * (0.15f + 0.10f * (1f - drift)),
                size.height * 0.12f
            ),
            radius = size.width * 0.65f
        )
    )
}

/**
 * App-wide backdrop: near-black base with dim amber and indigo glow orbs
 * bleeding in from the edges, plus a faint twinkling starfield. When
 * [animated], the orbs drift and breathe slowly; pass `animated = false`
 * on screens that must stay visually still (e.g. active night mode).
 */
@Composable
fun AmbientBackground(
    modifier: Modifier = Modifier,
    orbIntensity: Float = 1f,
    animated: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        if (animated) {
            AnimatedOrbLayer(orbIntensity)
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind { drawOrbs(orbIntensity, drift = 0.5f, breath = 1f) }
            )
        }
        content()
    }
}

@Composable
private fun AnimatedOrbLayer(orbIntensity: Float) {
    val transition = rememberInfiniteTransition(label = "ambient")
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(26_000, easing = LinearEasing), RepeatMode.Reverse),
        label = "drift"
    )
    val breath by transition.animateFloat(
        initialValue = 0.80f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(7_000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "breath"
    )
    val twinkle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 6.2832f,
        animationSpec = infiniteRepeatable(tween(9_000, easing = LinearEasing), RepeatMode.Reverse),
        label = "twinkle"
    )
    val stars = remember { buildStars(46) }

    Box(
        Modifier
            .fillMaxSize()
            .drawBehind {
                drawOrbs(orbIntensity, drift, breath)
                stars.forEach { star ->
                    val shimmer = (sin(twinkle * star.speed + star.phase) + 1f) / 2f
                    drawCircle(
                        color = Color.White.copy(alpha = 0.05f + 0.28f * shimmer),
                        radius = star.radiusDp.dp.toPx(),
                        center = Offset(star.x * size.width, star.y * size.height)
                    )
                }
            }
    )
}
