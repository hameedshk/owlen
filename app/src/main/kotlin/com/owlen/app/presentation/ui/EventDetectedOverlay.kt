package com.owlen.app.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owlen.app.domain.model.EventClass
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.StrokeBright
import com.owlen.app.presentation.ui.theme.SurfaceCard
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import kotlinx.coroutines.delay

private const val AUTO_DISMISS_SECONDS = 5

@Composable
fun EventDetectedOverlay(
    eventClass: EventClass = EventClass.GARBAGE_COLLECTION,
    disturbanceScore: Int = 89,
    maskingSound: MaskingSound = MaskingSound.BROWN_NOISE,
    maskingVolume: Float = 0.25f,
    isVisible: Boolean = true,
    onDismiss: () -> Unit = {}
) {
    var visible by remember(isVisible) { mutableStateOf(isVisible) }
    var countdown by remember { mutableFloatStateOf(AUTO_DISMISS_SECONDS.toFloat()) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            visible = true
            countdown = AUTO_DISMISS_SECONDS.toFloat()
            while (countdown > 0f) {
                delay(100L)
                countdown = (countdown - 0.1f).coerceAtLeast(0f)
            }
            visible = false
            onDismiss()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = androidx.compose.animation.core.tween(300)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = androidx.compose.animation.core.tween(300)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceCard, RectangleShape)
                    .drawBehind {
                        // StrokeBright top border
                        drawLine(
                            color = StrokeBright,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                        // Amber left accent 4dp
                        drawLine(
                            color = Primary,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 4.dp.toPx()
                        )
                    }
                    .padding(24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.RecordVoiceOver,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = eventClass.displayName,
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimary
                            )
                            Text(
                                text = "Detected",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Disturbance Score",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = disturbanceScore.toString(),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Playing ${maskingSound.displayName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LinearProgressIndicator(
                            progress = { maskingVolume },
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = Primary,
                            trackColor = Primary.copy(alpha = 0.2f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${(maskingVolume * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Auto dismiss after ${countdown.toInt() + 1} second${if (countdown.toInt() + 1 != 1) "s" else ""}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
