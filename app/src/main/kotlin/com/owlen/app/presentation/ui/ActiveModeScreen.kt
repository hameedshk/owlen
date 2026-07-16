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
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owlen.app.presentation.ui.theme.Background
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.GreenDim
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.PrimaryDim
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.glass
import com.owlen.app.presentation.ui.theme.glow
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.Warning
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DIM_AFTER_MS = 10_000L
private const val HOLD_TO_STOP_MS = 1_200

// BRD battery budget: <= 8% over 8 hours of protection
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
    // Night dim: fade to a near-black minimal UI after inactivity; tap to wake
    var dimmed by remember { mutableStateOf(false) }
    var wakeKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(wakeKey) {
        delay(DIM_AFTER_MS)
        dimmed = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            // Night dim covers the ambient glow with solid near-black —
            // the screen must not light the room
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
 * Minimal low-luminance face for a dark bedroom: dim clock, faint shield.
 * No white or bright pixels — the screen should not light the room.
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
    // Shield pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "shield_pulse")
    val shieldScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shield_scale"
    )

    // Expanding halo pulse behind the shield — a calm "breathing" beat
    val halo by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing)
        ),
        label = "shield_halo"
    )

    val startTimeFormatted = remember(sessionStartTimeMs) {
        SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(sessionStartTimeMs))
    }

    val lowBattery = batteryPercent < 20

    // Projection at wake time, using the BRD drain budget (~1%/h)
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

        // Green shield
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(shieldScale)
                .glow(Green, 32.dp, alpha = 0.20f)
                .drawBehind {
                    val radius = size.maxDimension / 2f * (0.8f + 0.9f * halo)
                    drawCircle(
                        color = Green.copy(alpha = (1f - halo) * 0.16f),
                        radius = radius,
                        center = center
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Shield,
                contentDescription = "Protection active",
                tint = Green,
                modifier = Modifier.size(80.dp)
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
                .glass(RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Microphone Active",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
            EqualizerBars(color = Green)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .glass(RoundedCornerShape(12.dp))
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

/** Gently dancing bars signalling live listening — calmer than a blinking dot. */
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
                    .background(color, RoundedCornerShape(1.5.dp))
            )
        }
    }
}

/**
 * Press-and-hold stop control. Amber, not safety red — red is reserved for
 * Baby Cry / Smoke Alarm. Holding fills the button; releasing early cancels,
 * so a groggy fumble at 3 AM can't kill protection.
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
            .glass(RoundedCornerShape(12.dp))
            .border(2.dp, Primary, RoundedCornerShape(12.dp))
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
