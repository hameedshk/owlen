package com.owlen.app.presentation.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke as StrokeStyle
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * CRED-style NeoPop plunk plate: a hard-edged face raised above two drawn
 * edge parallelograms (right + bottom). Pressing translates the face down
 * and right by [depth] so it lands flush on the back plane — the "plunk".
 *
 * The back edges of the parallelograms are pinned to the outer bounds, so
 * the component's total footprint never changes during the press.
 *
 * Feel details: a haptic tick fires on press-down, the face darkens ~8%
 * at full press so the plunk reads even on bright faces, and [shimmer]
 * sweeps a slanted highlight across the face every few seconds (reserve
 * it for primary CTAs).
 */
@Composable
fun NeoPopPlate(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    faceColor: Color = SurfaceCard,
    edgeRight: Color = NeutralEdge,
    edgeBottom: Color = NeutralEdge,
    strokeColor: Color = Stroke,
    depth: Dp = 6.dp,
    enabled: Boolean = true,
    strokedEdges: Boolean = false,
    shimmer: Boolean = false,
    contentAlignment: Alignment = Alignment.Center,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val interaction = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val press by animateFloatAsState(
        targetValue = if (pressed && enabled) 1f else 0f,
        animationSpec = tween(durationMillis = 70, easing = LinearEasing),
        label = "plunk"
    )

    val haptics = LocalHapticFeedback.current
    LaunchedEffect(pressed) {
        if (pressed && enabled) {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    // Shimmer phase: sweep takes the first ~28% of a 4s cycle, then rests
    val shimmerPhase = if (shimmer && enabled) {
        val transition = rememberInfiniteTransition(label = "shimmer")
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 4000, easing = LinearEasing)
            ),
            label = "shimmerPhase"
        ).value
    } else {
        0f
    }

    val pressedFace = lerp(faceColor, Color.Black, 0.08f * press)

    Box(
        modifier = modifier.drawBehind {
            if (press >= 1f) return@drawBehind
            val d = depth.toPx()
            val s = d * press
            val faceRight = size.width - d + s
            val faceBottom = size.height - d + s

            val rightFace = Path().apply {
                moveTo(faceRight, s)
                lineTo(size.width, d)
                lineTo(size.width, size.height)
                lineTo(faceRight, faceBottom)
                close()
            }
            val bottomFace = Path().apply {
                moveTo(s, faceBottom)
                lineTo(faceRight, faceBottom)
                lineTo(size.width, size.height)
                lineTo(d, size.height)
                close()
            }
            if (strokedEdges) {
                drawPath(rightFace, NeutralEdge)
                drawPath(bottomFace, NeutralEdge)
                drawPath(rightFace, strokeColor, style = StrokeStyle(1.dp.toPx()))
                drawPath(bottomFace, strokeColor, style = StrokeStyle(1.dp.toPx()))
            } else {
                drawPath(rightFace, edgeRight)
                drawPath(bottomFace, edgeBottom)
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = depth, bottom = depth)
                .offset {
                    val px = (depth.toPx() * press).roundToInt()
                    IntOffset(px, px)
                }
                .background(if (enabled) pressedFace else SurfaceSunken, RectangleShape)
                .border(1.dp, strokeColor, RectangleShape)
                .drawWithContent {
                    drawContent()
                    if (shimmerPhase > 0f && shimmerPhase < 0.28f) {
                        // Slanted highlight bar sweeping left → right across the face
                        val progress = shimmerPhase / 0.28f
                        val band = size.width * 0.28f
                        val x = -band + (size.width + band * 2) * progress
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.10f),
                                    Color.Transparent
                                ),
                                start = Offset(x, size.height),
                                end = Offset(x + band, 0f)
                            )
                        )
                    }
                }
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = interaction,
                            indication = null,
                            enabled = enabled,
                            onClick = onClick
                        )
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = contentAlignment
        ) {
            content()
        }
    }
}

/**
 * NeoPop panel: matte fill with a hard 1dp stroke. The 2dp corner keeps the
 * stroke antialiased without reading as rounded.
 */
fun Modifier.neoPopCard(
    fill: Color = SurfaceCard,
    stroke: Color = Stroke,
    tint: Color = Color.Transparent,
    shape: Shape = RoundedCornerShape(2.dp)
): Modifier = this
    .background(fill, shape)
    .background(tint, shape)
    .border(1.dp, stroke, shape)

/**
 * CRED signature: small "+" ticks drawn just outside the four corners of a
 * panel. Reserve for one or two marquee cards per screen — more is noise.
 */
fun Modifier.cornerTicks(
    color: Color = TextSecondary.copy(alpha = 0.35f)
): Modifier = this.drawBehind {
    val arm = 4.dp.toPx()
    val strokeW = 1.dp.toPx()
    val gap = 6.dp.toPx()
    val corners = listOf(
        Offset(-gap, -gap),
        Offset(size.width + gap, -gap),
        Offset(-gap, size.height + gap),
        Offset(size.width + gap, size.height + gap)
    )
    corners.forEach { c ->
        drawLine(color, Offset(c.x - arm, c.y), Offset(c.x + arm, c.y), strokeW)
        drawLine(color, Offset(c.x, c.y - arm), Offset(c.x, c.y + arm), strokeW)
    }
}

/**
 * App-wide matte backdrop: solid near-black, optionally textured with a
 * static dot grid. Deliberately animation-free (night battery use).
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
