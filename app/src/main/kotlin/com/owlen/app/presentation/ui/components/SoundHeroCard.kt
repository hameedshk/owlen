package com.owlen.app.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.SurfaceCard
import com.owlen.app.presentation.ui.theme.TextPrimary
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Hero card with Canvas-based sound illustration, scrim for text legibility,
 * label/sound name, and chevron button. Uses static seeded Random for stable positions.
 *
 * Features:
 * - 6 Canvas-based scene variations (Rain, Ocean, Fan, White/Pink/Brown Noise)
 * - Haptic feedback on tap and long-press
 * - Long-press support for preview (if onLongPress provided)
 * - Accessibility labels for screen readers
 * - Optimized drawing with cached points and theme constants
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SoundHeroCard(
    sound: MaskingSound,
    label: String = "Selected Sound",
    onClick: () -> Unit,
    onLongPress: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .combinedClickable(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                },
                onLongClick = onLongPress?.let {
                    {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        it()
                    }
                }
            )
    ) {
        // Canvas scene
        CanvasScene(sound = sound, modifier = Modifier.matchParentSize())

        // Bottom scrim for text legibility
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(60.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f)
                        )
                    )
                )
        )

        // Content: label, sound name, and chevron
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPrimary.copy(alpha = 0.7f)
                )
                Text(
                    text = sound.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.2f))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Select ${sound.displayName}",
                    tint = TextPrimary,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
private fun CanvasScene(
    sound: MaskingSound,
    modifier: Modifier = Modifier
) {
    val random = remember(sound) { Random(sound.ordinal.toLong()) }

    Box(modifier = modifier.background(SurfaceCard)) {
        androidx.compose.foundation.Canvas(modifier = Modifier.matchParentSize()) {
            drawScene(sound, random)
        }
    }
}

private fun DrawScope.drawScene(sound: MaskingSound, random: Random) {
    when (sound) {
        MaskingSound.RAIN -> drawRainScene(random)
        MaskingSound.OCEAN_WAVES -> drawOceanScene(random)
        MaskingSound.FAN -> drawFanScene(random)
        MaskingSound.WHITE_NOISE -> drawWhiteNoiseScene(random)
        MaskingSound.PINK_NOISE -> drawPinkNoiseScene(random)
        MaskingSound.BROWN_NOISE -> drawBrownNoiseScene(random)
    }
}

private fun DrawScope.drawRainScene(random: Random) {
    val theme = SoundSceneTheme.Rain
    // Deep indigo → slate gradient sky
    drawSkyGradient(startColor = theme.skyStartColor, endColor = theme.skyEndColor)
    // Mountain silhouettes
    drawMountains(
        colors = listOf(theme.mountainColor1, theme.mountainColor2),
        baseY = size.height * 0.6f
    )
    // Diagonal rain streaks with wind sway effect
    drawStreaks(
        count = theme.streakCount,
        angleRange = theme.streakAngleRange,
        color = theme.streakColor,
        random = random,
        windSway = true  // Add horizontal drift for dynamic wind effect
    )
}

private fun DrawScope.drawOceanScene(random: Random) {
    val theme = SoundSceneTheme.Ocean
    // Teal → navy gradient sky
    drawSkyGradient(startColor = theme.skyStartColor, endColor = theme.skyEndColor)
    // Stacked sine waves (water) with foam/spray accents
    drawWaves(
        bandCount = theme.bandCount,
        colorRange = theme.waveColors,
        baseY = size.height * 0.4f,
        frequency = theme.frequency,
        phaseShift = theme.phaseShiftPerBand,
        random = random,
        drawFoam = true  // Add white foam circles at wave peaks for visual interest
    )
}

private fun DrawScope.drawFanScene(random: Random) {
    val theme = SoundSceneTheme.Fan
    // Warm grey → charcoal gradient
    drawSkyGradient(startColor = theme.skyStartColor, endColor = theme.skyEndColor)
    // Concentric arcs (fan blades) with subtle rotation for motion effect
    drawArcs(
        count = theme.arcCount,
        colorRange = theme.bladeColors,
        centerX = size.width / 2,
        centerY = size.height / 2,
        baseRadius = theme.baseRadius,
        radiusStep = theme.radiusStep,
        baseOpacity = theme.baseOpacity,
        opacityFadePerArc = theme.opacityFadePerArc,
        rotateAngle = 15f  // Subtle rotation angle for dynamic appearance
    )
}

private fun DrawScope.drawWhiteNoiseScene(random: Random) {
    val theme = SoundSceneTheme.WhiteNoise
    // Cool grey → near-black gradient
    drawSkyGradient(startColor = theme.skyStartColor, endColor = theme.skyEndColor)
    // Fine dot field with increased density for richer static effect
    drawDotField(
        density = theme.dotDensity,
        dotRadius = theme.dotRadius,
        color = theme.dotColor,
        random = random,
        skipProbability = theme.skipProbability  // Reduced skip for denser dots
    )
}

private fun DrawScope.drawPinkNoiseScene(random: Random) {
    val theme = SoundSceneTheme.PinkNoise
    // Dusty rose → plum gradient
    drawSkyGradient(startColor = theme.skyStartColor, endColor = theme.skyEndColor)
    // Soft horizontal gradient bands (smoother than flat colors)
    val bandHeight = size.height / theme.bandCount
    theme.bandColors.forEachIndexed { index, color ->
        // Use gradient within each band for smooth color transition
        val nextColor = theme.bandColors.getOrElse(index + 1) { theme.bandColors.last() }
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(color.copy(alpha = theme.bandOpacity), nextColor.copy(alpha = theme.bandOpacity)),
                startY = bandHeight * index,
                endY = bandHeight * (index + 1)
            ),
            topLeft = Offset(0f, bandHeight * index),
            size = androidx.compose.ui.geometry.Size(size.width, bandHeight)
        )
    }
}

private fun DrawScope.drawBrownNoiseScene(random: Random) {
    val theme = SoundSceneTheme.BrownNoise
    // Amber-brown → espresso gradient
    drawSkyGradient(startColor = theme.skyStartColor, endColor = theme.skyEndColor)
    // Low rolling hills
    drawRollingHills(
        colors = theme.hillColors,
        baseY = size.height * 0.5f,
        baseAmplitude = theme.baseAmplitude,
        amplitudeStep = theme.amplitudeStep
    )
    // Grain texture overlay for earthy feel
    drawDotField(
        density = 12,
        dotRadius = 0.3f,
        color = Color.Black.copy(alpha = 0.08f),
        random = random,
        skipProbability = 0.5f  // Sparse grain for subtle effect
    )
}

private fun DrawScope.drawSkyGradient(startColor: Color, endColor: Color) {
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(startColor, endColor)
        )
    )
}

private fun DrawScope.drawMountains(colors: List<Color>, baseY: Float) {
    colors.forEachIndexed { index, color ->
        val offset = index * 20f
        val points = listOf(
            Offset(0f, baseY + offset),
            Offset(size.width * 0.3f, size.height * 0.2f + offset),
            Offset(size.width * 0.6f, baseY * 0.9f + offset),
            Offset(size.width, baseY + offset),
            Offset(size.width, size.height),
            Offset(0f, size.height)
        )
        val path = androidx.compose.ui.graphics.Path().apply {
            points.forEachIndexed { i, point ->
                if (i == 0) moveTo(point.x, point.y) else lineTo(point.x, point.y)
            }
            close()
        }
        drawPath(path, color = color)
    }
}

private fun DrawScope.drawStreaks(
    count: Int,
    angleRange: ClosedFloatingPointRange<Float>,
    color: Color,
    random: Random,
    windSway: Boolean = false
) {
    repeat(count) {
        val angle = random.nextFloat() * (angleRange.endInclusive - angleRange.start) + angleRange.start
        // Add wind sway effect: vary angle slightly based on vertical position for flowing motion
        val swayedAngle = if (windSway) angle + random.nextFloat() * 5 - 2.5f else angle
        val radians = Math.toRadians(swayedAngle.toDouble()).toFloat()
        val startX = random.nextFloat() * size.width
        // Spread start heights across the card (not just a thin band above it)
        // so streaks visibly cross most of the scene, not just the top edge.
        val startY = random.nextFloat() * size.height * 0.6f - 100f
        val length = random.nextFloat() * 160 + 120
        val endX = startX + length * cos(radians)
        val endY = startY + length * sin(radians)

        drawLine(
            color = color,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 1f
        )
    }
}

private fun DrawScope.drawWaves(
    bandCount: Int,
    colorRange: List<Color>,
    baseY: Float,
    frequency: Float = 0.01f,
    phaseShift: Float = 30f,
    random: Random? = null,
    drawFoam: Boolean = false
) {
    repeat(bandCount) { band ->
        val color = colorRange.getOrElse(band) { colorRange.last() }
        // Scaled to canvas height (not fixed px) so the wave crests are
        // actually visible against the card instead of a few flat pixels.
        val amplitude = size.height * (0.025f + band * 0.015f)
        val bandHeight = size.height * 0.05f

        // Cache wave points for performance (remove recalculation on every frame)
        val points = WaveCalculator.calculateWavePoints(
            width = size.width,
            baseY = baseY,
            band = band,
            amplitude = amplitude,
            frequency = frequency,
            phaseShift = phaseShift,
            bandHeight = bandHeight
        )

        val path = androidx.compose.ui.graphics.Path().apply {
            points.forEachIndexed { index, point ->
                if (index == 0) moveTo(point.x, point.y) else lineTo(point.x, point.y)
            }
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path, color = color)

        // Draw foam/spray at wave peaks for visual interest
        if (drawFoam && random != null && band == bandCount - 1) {
            points.forEachIndexed { index, point ->
                if (index % 8 == 0 && random.nextFloat() > 0.3f) {  // Sparse foam spots
                    drawCircle(
                        color = Color.White.copy(alpha = 0.4f),
                        radius = 2f,
                        center = Offset(point.x, point.y - amplitude)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawArcs(
    count: Int,
    colorRange: List<Color>,
    centerX: Float,
    centerY: Float,
    baseRadius: Float = 30f,
    radiusStep: Float = 15f,
    baseOpacity: Float = 0.5f,
    opacityFadePerArc: Float = 0.08f,
    rotateAngle: Float = 0f  // Rotation angle for dynamic fan effect
) {
    // Stroked rings (not filled disks) so each arc stays visible instead of
    // being overpainted by the next, larger circle drawn on top of it.
    rotate(degrees = rotateAngle, pivot = Offset(centerX, centerY)) {
        repeat(count) { arc ->
            val color = colorRange.getOrElse(arc) { colorRange.last() }
            val radius = baseRadius + arc * radiusStep
            // Opacity fades with each arc layer for depth effect
            val opacity = (baseOpacity - arc * opacityFadePerArc).coerceIn(0f, 1f)

            drawCircle(
                color = color.copy(alpha = opacity),
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = radiusStep * 0.6f)
            )
        }
    }
}

private fun DrawScope.drawDotField(
    density: Int,
    dotRadius: Float,
    color: Color,
    random: Random,
    skipProbability: Float = 0.3f  // Probability of skipping a dot (0f = all dots, 1f = no dots)
) {
    val spacing = size.width / density
    var y = 0f
    while (y < size.height) {
        var x = 0f
        while (x < size.width) {
            // Lower skipProbability = denser dot field
            if (random.nextFloat() > skipProbability) {
                drawCircle(
                    color = color,
                    radius = dotRadius,
                    center = Offset(
                        x + random.nextFloat() * spacing * 0.5f,
                        y + random.nextFloat() * spacing * 0.5f
                    )
                )
            }
            x += spacing
        }
        y += spacing
    }
}

private fun DrawScope.drawRollingHills(
    colors: List<Color>,
    baseY: Float,
    baseAmplitude: Float = 20f,
    amplitudeStep: Float = 10f
) {
    colors.forEachIndexed { index, color ->
        // Cache hill points for performance
        val points = WaveCalculator.calculateHillPoints(
            width = size.width,
            baseY = baseY,
            hillIndex = index,
            baseAmplitude = baseAmplitude,
            amplitudeStep = amplitudeStep
        )

        val path = androidx.compose.ui.graphics.Path().apply {
            points.forEachIndexed { i, point ->
                if (i == 0) moveTo(point.x, point.y) else lineTo(point.x, point.y)
            }
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path, color = color)
    }
}
