package com.owlen.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.Layout
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
 */
@Composable
fun SoundHeroCard(
    sound: MaskingSound,
    label: String = "Selected Sound",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
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
                    contentDescription = null,
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
    // Deep indigo → slate gradient sky
    drawSkyGradient(
        startColor = Color(0xFF1a237e),
        endColor = Color(0xFF37474f)
    )
    // Mountain silhouettes
    drawMountains(
        colors = listOf(Color(0xFF0d1b4f), Color(0xFF1a237e)),
        baseY = size.height * 0.6f
    )
    // Diagonal rain streaks
    drawStreaks(
        count = 20,
        angleRange = 60f..75f,
        color = Color.White.copy(alpha = 0.3f),
        random = random
    )
}

private fun DrawScope.drawOceanScene(random: Random) {
    // Teal → navy gradient sky
    drawSkyGradient(
        startColor = Color(0xFF00897b),
        endColor = Color(0xFF0d47a1)
    )
    // Stacked sine waves (water)
    drawWaves(
        bandCount = 4,
        colorRange = listOf(
            Color(0xFF00695c),
            Color(0xFF004d73),
            Color(0xFF0d3b66),
            Color(0xFF001a4d)
        ),
        baseY = size.height * 0.4f
    )
}

private fun DrawScope.drawFanScene(random: Random) {
    // Warm grey → charcoal gradient
    drawSkyGradient(
        startColor = Color(0xFF78909c),
        endColor = Color(0xFF37474f)
    )
    // Concentric arcs (fan blades)
    drawArcs(
        count = 5,
        colorRange = listOf(
            Color(0xFFeceff1),
            Color(0xFFb0bec5),
            Color(0xFF90a4ae),
            Color(0xFF78909c),
            Color(0xFF546e7a)
        ),
        centerX = size.width / 2,
        centerY = size.height / 2
    )
}

private fun DrawScope.drawWhiteNoiseScene(random: Random) {
    // Cool grey → near-black gradient
    drawSkyGradient(
        startColor = Color(0xFFcfd8dc),
        endColor = Color(0xFF263238)
    )
    // Fine even dot field
    drawDotField(
        density = 8,
        dotRadius = 0.5f,
        color = Color.White.copy(alpha = 0.2f),
        random = random
    )
}

private fun DrawScope.drawPinkNoiseScene(random: Random) {
    // Dusty rose → plum gradient
    drawSkyGradient(
        startColor = Color(0xFFf1a1b8),
        endColor = Color(0xFF7b1fa2)
    )
    // Soft horizontal bands
    val bandHeight = size.height / 4
    listOf(
        Color(0xFFf1a1b8),
        Color(0xFFe91e63),
        Color(0xFfc2185b),
        Color(0xFF7b1fa2)
    ).forEachIndexed { index, color ->
        drawRect(
            color = color.copy(alpha = 0.4f),
            topLeft = Offset(0f, bandHeight * index),
            size = androidx.compose.ui.geometry.Size(size.width, bandHeight)
        )
    }
}

private fun DrawScope.drawBrownNoiseScene(random: Random) {
    // Amber-brown → espresso gradient
    drawSkyGradient(
        startColor = Color(0xFFbf360c),
        endColor = Color(0xFF1a0f00)
    )
    // Low rolling hills
    drawRollingHills(
        colors = listOf(Color(0xFF8b4513), Color(0xFF5d4037), Color(0xFF3e2723)),
        baseY = size.height * 0.5f
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
    random: Random
) {
    repeat(count) {
        val angle = random.nextFloat() * (angleRange.endInclusive - angleRange.start) + angleRange.start
        val radians = Math.toRadians(angle.toDouble()).toFloat()
        val startX = random.nextFloat() * size.width
        val startY = -50f
        val length = random.nextFloat() * 80 + 40
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
    baseY: Float
) {
    repeat(bandCount) { band ->
        val color = colorRange.getOrElse(band) { colorRange.last() }
        val amplitude = 10f + band * 5f
        val frequency = 0.01f
        val phaseShift = band * 30f

        val points = mutableListOf<Offset>()
        var x = 0f
        while (x <= size.width) {
            val y = baseY + (band * 15f) + amplitude * sin((x * frequency + phaseShift) * Math.PI / 180).toFloat()
            points.add(Offset(x, y))
            x += 5f
        }

        val path = androidx.compose.ui.graphics.Path().apply {
            points.forEachIndexed { index, point ->
                if (index == 0) moveTo(point.x, point.y) else lineTo(point.x, point.y)
            }
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path, color = color)
    }
}

private fun DrawScope.drawArcs(
    count: Int,
    colorRange: List<Color>,
    centerX: Float,
    centerY: Float
) {
    repeat(count) { arc ->
        val color = colorRange.getOrElse(arc) { colorRange.last() }
        val radius = 30f + arc * 15f
        drawCircle(
            color = color.copy(alpha = 0.5f - arc * 0.08f),
            radius = radius,
            center = Offset(centerX, centerY)
        )
    }
}

private fun DrawScope.drawDotField(
    density: Int,
    dotRadius: Float,
    color: Color,
    random: Random
) {
    val spacing = size.width / density
    var y = 0f
    while (y < size.height) {
        var x = 0f
        while (x < size.width) {
            if (random.nextFloat() > 0.3f) {
                drawCircle(
                    color = color,
                    radius = dotRadius,
                    center = Offset(x + random.nextFloat() * spacing * 0.5f, y + random.nextFloat() * spacing * 0.5f)
                )
            }
            x += spacing
        }
        y += spacing
    }
}

private fun DrawScope.drawRollingHills(colors: List<Color>, baseY: Float) {
    colors.forEachIndexed { index, color ->
        val offset = index * 15f
        val waveLength = size.width / (2 + index)
        val amplitude = 20f + index * 10f

        val points = mutableListOf<Offset>()
        var x = 0f
        while (x <= size.width) {
            val y = baseY + offset + amplitude * sin((x / waveLength) * Math.PI).toFloat()
            points.add(Offset(x, y))
            x += 10f
        }

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
