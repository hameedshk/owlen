package com.owlen.app.presentation.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owlen.app.presentation.ui.theme.GlassBorder
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.Warning
import com.owlen.app.presentation.ui.theme.glow
import kotlinx.coroutines.delay

private const val CALIBRATION_SECONDS = 60

@Composable
fun CalibrationScreen(
    currentDbLevel: Float = 41f,
    obstructed: Boolean = false,
    onRetry: () -> Unit = {},
    onSkip: () -> Unit = {},
    onCalibrationComplete: () -> Unit
) {
    var secondsRemaining by remember { mutableIntStateOf(CALIBRATION_SECONDS) }
    var sweepAngle by remember { mutableFloatStateOf(360f) }

    val animatedSweep by animateFloatAsState(
        targetValue = sweepAngle,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "calibration_sweep"
    )

    // Countdown pauses while the microphone appears obstructed
    LaunchedEffect(obstructed) {
        if (!obstructed) {
            while (secondsRemaining > 0) {
                delay(1000L)
                secondsRemaining--
                sweepAngle = (secondsRemaining / CALIBRATION_SECONDS.toFloat()) * 360f
            }
            onCalibrationComplete()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Preparing...",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Listening to your room",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Circular countdown
        Box(
            modifier = Modifier
                .size(160.dp)
                .glow(Green, 32.dp, alpha = 0.14f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(160.dp)) {
                val strokeWidth = 6.dp.toPx()
                val padding = strokeWidth / 2
                val arcSize = Size(
                    width = size.width - padding * 2,
                    height = size.height - padding * 2
                )

                // Track ring
                drawArc(
                    color = GlassBorder,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(padding, padding),
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Progress ring (anticlockwise drain = negative sweep)
                drawArc(
                    color = Green,
                    startAngle = -90f,
                    sweepAngle = -animatedSweep,
                    useCenter = false,
                    topLeft = Offset(padding, padding),
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = secondsRemaining.toString(),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "sec remaining",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Current Noise",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${currentDbLevel.toInt()} dB",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (obstructed) {
            Text(
                text = "Microphone may be obstructed.\nReposition your device and retry.",
                style = MaterialTheme.typography.bodyMedium,
                color = Warning,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onRetry) {
                Text(
                    text = "Retry",
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary
                )
            }
        } else {
            Text(
                text = "Keep your phone nearby\nand stay quiet.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onSkip) {
            Text(
                text = "Skip — use default level",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
        }
    }
}
