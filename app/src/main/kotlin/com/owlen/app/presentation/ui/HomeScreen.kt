package com.owlen.app.presentation.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.owlen.app.R
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.Sensitivity
import com.owlen.app.presentation.ui.theme.FadeSlideIn
import com.owlen.app.presentation.ui.theme.GlassBorder
import com.owlen.app.presentation.ui.theme.GlassFill
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.GreenDim
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.TextDisabled
import com.owlen.app.presentation.ui.theme.glass
import com.owlen.app.presentation.ui.theme.glow
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.Warning
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    isProtectionActive: Boolean = false,
    sleepHour: Int = 22,
    wakeHour: Int = 7,
    preferredSound: MaskingSound = MaskingSound.BROWN_NOISE,
    sensitivity: Sensitivity = Sensitivity.MEDIUM,
    interruptedAtMs: Long? = null,
    onInterruptionClick: () -> Unit = {},
    onInterruptionDismiss: () -> Unit = {},
    onProtectionToggle: (Boolean) -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToActive: () -> Unit = {}
) {
    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when {
            hour < 12 -> "Good Morning"
            hour < 17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }

    val sleepTimeDisplay = remember(sleepHour) {
        val h = if (sleepHour % 12 == 0) 12 else sleepHour % 12
        val amPm = if (sleepHour < 12) "AM" else "PM"
        "$h:00 $amPm"
    }

    val wakeTimeDisplay = remember(wakeHour) {
        val h = if (wakeHour % 12 == 0) 12 else wakeHour % 12
        val amPm = if (wakeHour < 12) "AM" else "PM"
        "$h:00 $amPm"
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar — logo only; Settings accessible via bottom nav
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_owlen_mascot),
                contentDescription = "Owlen",
                tint = Color.Unspecified,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Owlen",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FadeSlideIn {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = greeting,
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Let's keep tonight quiet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Interruption banner
            if (interruptedAtMs != null) {
                val interruptedTime = remember(interruptedAtMs) {
                    SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(interruptedAtMs))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glass(RoundedCornerShape(12.dp), fill = Warning.copy(alpha = 0.10f))
                        .clickable(onClick = onInterruptionClick)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Warning,
                        contentDescription = null,
                        tint = Warning,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Interrupted at $interruptedTime · Tap to review",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Warning,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onInterruptionDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Dismiss",
                            tint = Warning,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Hero power button: rotating shimmer ring + breathing glow + press feedback
            val innerFill = if (isProtectionActive) Green.copy(alpha = 0.10f) else GlassFill
            val iconTint = if (isProtectionActive) Green else TextSecondary
            val buttonScale = if (isProtectionActive) pulseScale else 1f

            val ringRotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    tween(if (isProtectionActive) 4000 else 16000, easing = LinearEasing)
                ),
                label = "ringRotation"
            )
            val glowBreath by infiniteTransition.animateFloat(
                initialValue = 0.6f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    tween(2200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "glowBreath"
            )

            val interactionSource = remember { MutableInteractionSource() }
            val pressed by interactionSource.collectIsPressedAsState()
            val pressScale by animateFloatAsState(
                targetValue = if (pressed) 0.94f else 1f,
                label = "pressScale"
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(vertical = 36.dp)
                    .size(160.dp)
                    .scale(buttonScale * pressScale)
                    .then(
                        if (isProtectionActive) Modifier.glow(Green, 36.dp, alpha = 0.24f * glowBreath)
                        else Modifier.glow(Primary, 24.dp, alpha = 0.15f * glowBreath)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .graphicsLayer { rotationZ = ringRotation }
                        .border(
                            width = 2.dp,
                            brush = if (isProtectionActive)
                                Brush.sweepGradient(
                                    listOf(Green, GreenDim, Green.copy(alpha = 0.15f), Green)
                                )
                            else
                                Brush.sweepGradient(
                                    listOf(
                                        TextDisabled,
                                        Primary.copy(alpha = 0.55f),
                                        TextDisabled,
                                        TextDisabled
                                    )
                                ),
                            shape = CircleShape
                        )
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .background(innerFill)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onProtectionToggle(!isProtectionActive) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Rounded.PowerSettingsNew,
                            contentDescription = if (isProtectionActive) "Stop protection" else "Start protection",
                            tint = iconTint,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isProtectionActive) "Active" else "Tap to Start",
                            style = MaterialTheme.typography.labelMedium,
                            color = iconTint,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Text(
                text = if (isProtectionActive)
                    "Monitoring your sleep environment"
                else
                    "Ready to protect your sleep",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isProtectionActive) Green.copy(alpha = 0.80f) else TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sleep schedule card
            FadeSlideIn(delayMillis = 150) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glass(RoundedCornerShape(16.dp))
                        .padding(vertical = 20.dp, horizontal = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ScheduleTimeItem(
                            icon = Icons.Rounded.DarkMode,
                            label = "Sleep",
                            time = sleepTimeDisplay
                        )
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(48.dp)
                                .background(GlassBorder)
                        )
                        ScheduleTimeItem(
                            icon = Icons.Rounded.LightMode,
                            label = "Wake",
                            time = wakeTimeDisplay
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick-info chips: sound & sensitivity (tappable → settings)
            FadeSlideIn(delayMillis = 280) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickInfoChip(
                        icon = Icons.Rounded.GraphicEq,
                        text = preferredSound.displayName,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToSettings
                    )
                    QuickInfoChip(
                        icon = Icons.Rounded.Tune,
                        text = sensitivity.name.lowercase()
                            .replaceFirstChar { it.uppercase() },
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToSettings
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleTimeItem(
    icon: ImageVector,
    label: String,
    time: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = time,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
    }
}

@Composable
private fun QuickInfoChip(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .glass(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = TextPrimary,
            maxLines = 1
        )
    }
}
