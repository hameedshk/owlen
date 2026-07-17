package com.owlen.app.presentation.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owlen.app.presentation.ui.theme.Background
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.GreenDim
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.PrimaryDim
import com.owlen.app.presentation.ui.theme.SurfaceSunken
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.Warning
import com.owlen.app.presentation.ui.theme.neoPopCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DIM_AFTER_MS = 10_000L
private const val HOLD_TO_STOP_MS = 1_200

private const val DRAIN_PCT_PER_HOUR = 1.0f

@Composable
fun ActiveModeScreen(
    sessionStartTimeMs: Long = System.currentTimeMillis(),
    batteryPercent: Int = 98,
    wakeTimeHour: Int = 6,
    wakeTimeMinute: Int = 0,
    statusText: String = "Monitoring...",
    onStopProtection: () -> Unit = {}
) {
    var dimmed by remember { mutableStateOf(false) }
    var wakeKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(wakeKey) {
        delay(DIM_AFTER_MS)
        dimmed = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(if (dimmed) Modifier.background(Background) else Modifier)
            .pointerInput(dimmed) {
                if (dimmed) {
                    detectTapGestures {
                        dimmed = false
                        wakeKey++
                    }
                }
            }
    ) {
        Crossfade(targetState = dimmed, label = "night_dim") { isDimmed ->
            if (isDimmed) {
                DimmedNightFace(statusText = statusText)
            } else {
                ActiveModeContent(
                    sessionStartTimeMs = sessionStartTimeMs,
                    batteryPercent = batteryPercent,
                    wakeTimeHour = wakeTimeHour,
                    wakeTimeMinute = wakeTimeMinute,
                    statusText = statusText,
                    onInteraction = { wakeKey++ },
                    onStopProtection = onStopProtection
                )
            }
        }
    }
}

/**
 * Minimal low-luminance face for a dark bedroom.
 * No white or bright pixels — the screen must not light the room.
 * No dot grid here — battery constraint.
 */
@Composable
private fun DimmedNightFace(statusText: String) {
    var timeText by remember {
        mutableStateOf(SimpleDateFormat("h:mm", Locale.getDefault()).format(Date()))
    }
    LaunchedEffect(Unit) {
        while (true) {
            timeText = SimpleDateFormat("h:mm", Locale.getDefault()).format(Date())
            delay(10_000L)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Shield,
            contentDescription = "Protection active",
            tint = GreenDim.copy(alpha = 0.45f),
            modifier = Modifier.size(36.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = timeText,
            fontSize = 56.sp,
            fontWeight = FontWeight.Light,
            color = PrimaryDim.copy(alpha = 0.55f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = statusText,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary.copy(alpha = 0.35f)
        )
    }
}

@Composable
private fun ActiveModeContent(
    sessionStartTimeMs: Long,
    batteryPercent: Int,
    wakeTimeHour: Int,
    wakeTimeMinute: Int,
    statusText: String,
    onInteraction: () -> Unit,
    onStopProtection: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "active_pulse")

    // Slow stroke-alpha pulse on the shield plate (no scale, no glow)
    val strokeAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shield_stroke"
    )

    val startTimeFormatted = remember(sessionStartTimeMs) {
        SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(sessionStartTimeMs))
    }

    val lowBattery = batteryPercent < 20

    val (projectedAtWake, wakeTimeFormatted) = remember(batteryPercent, wakeTimeHour, wakeTimeMinute) {
        val now = java.util.Calendar.getInstance()
        val nowMinutes = now.get(java.util.Calendar.HOUR_OF_DAY) * 60 + now.get(java.util.Calendar.MINUTE)
        val wakeMinutes = wakeTimeHour * 60 + wakeTimeMinute
        val minutesUntilWake = ((wakeMinutes - nowMinutes) + 24 * 60) % (24 * 60)
        val projected = (batteryPercent - (minutesUntilWake / 60f) * DRAIN_PCT_PER_HOUR)
            .toInt().coerceAtLeast(0)

        val wakeCal = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, wakeTimeHour)
            set(java.util.Calendar.MINUTE, wakeTimeMinute)
        }
        projected to SimpleDateFormat("h:mm a", Locale.getDefault()).format(wakeCal.time)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Square shield plate with pulsing green stroke (no scale, no glow)
        Box(
            modifier = Modifier
                .size(96.dp)
                .neoPopCard(fill = Green.copy(alpha = 0.12f), stroke = Green.copy(alpha = strokeAlpha)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Shield,
                contentDescription = "Protection active",
                tint = Green,
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Protecting Sleep",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = statusText,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Microphone active indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .neoPopCard()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Microphone Active",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
            EqualizerBars(color = Primary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .neoPopCard()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Started",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = startTimeFormatted,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Battery",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (lowBattery) Warning else TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$batteryPercent%",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (lowBattery) Warning else TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "~$projectedAtWake% at $wakeTimeFormatted",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
        }

        if (lowBattery) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Low battery — consider charging",
                style = MaterialTheme.typography.labelSmall,
                color = Warning
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        HoldToStopButton(
            onHoldStart = onInteraction,
            onStop = onStopProtection,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

/** Hard amber bars signalling live listening. */
@Composable
private fun EqualizerBars(
    color: Color,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "eq")
    val durations = listOf(420, 560, 480, 640)
    val levels = durations.mapIndexed { index, duration ->
        transition.animateFloat(
            initialValue = 0.25f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "eq$index"
        )
    }
    Row(
        modifier = modifier.height(16.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        levels.forEach { level ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight(level.value)
                    .background(color, RectangleShape)
            )
        }
    }
}

/**
 * Press-and-hold stop control. Amber face fill — red is reserved for Baby Cry / Smoke Alarm.
 */
@Composable
private fun HoldToStopButton(
    onHoldStart: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var holding by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(64.dp)
            .neoPopCard(fill = SurfaceSunken, stroke = Primary)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onHoldStart()
                        holding = true
                        val fillJob = scope.launch {
                            progress.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(HOLD_TO_STOP_MS, easing = LinearEasing)
                            )
                            onStop()
                        }
                        tryAwaitRelease()
                        holding = false
                        if (progress.value < 1f) {
                            fillJob.cancel()
                            scope.launch { progress.animateTo(0f, tween(200)) }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    drawRect(
                        color = Primary.copy(alpha = 0.30f),
                        size = Size(size.width * progress.value, size.height)
                    )
                }
        )
        Text(
            text = if (holding) "KEEP HOLDING…" else "HOLD TO STOP",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Primary
        )
    }
}
