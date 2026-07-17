package com.owlen.app.presentation.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owlen.app.domain.model.EventClass
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.Stroke
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.Warning
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SessionEventItem(
    val timestampMs: Long,
    val eventClass: EventClass,
    val score: Int,
    val wasMasked: Boolean,
    val maskingSound: MaskingSound? = null,
    val maskingVolume: Float = 0f,
    val durationMs: Long = 0L
)

@Composable
fun SessionDetailsScreen(
    events: List<SessionEventItem> = sampleSessionEvents(),
    onNavigateBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "HISTORY",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Text(
                    text = "Session Details",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
            }
        }

        if (events.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No sessions yet. Start your first night of protection.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                items(events) { event ->
                    SessionEventRow(event = event)
                    HorizontalDivider(
                        color = Stroke,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SessionEventRow(event: SessionEventItem) {
    val timeFormatted = remember(event.timestampMs) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(event.timestampMs))
    }

    val durationFormatted = remember(event.durationMs) {
        val minutes = (event.durationMs / 60000).toInt()
        val seconds = ((event.durationMs % 60000) / 1000).toInt()
        "${minutes}m ${seconds.toString().padStart(2, '0')}s"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Time column
        Text(
            text = timeFormatted,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            modifier = Modifier.width(40.dp)
        )

        // Event info column
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = event.eventClass.displayName,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            // A bare score means nothing to users — pair it with a severity word
            val (severityLabel, severityColor) = when {
                event.score >= 80 -> "Severe" to Warning
                event.score >= 60 -> "Strong" to Primary
                event.score >= 30 -> "Mild" to TextSecondary
                else -> "Faint" to TextSecondary
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = severityLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = severityColor,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "  ·  Score ${event.score}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }

            if (event.wasMasked && event.maskingSound != null) {
                Spacer(modifier = Modifier.height(8.dp))

                // Masked indicator row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Green, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = event.maskingSound.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Volume bar
                    LinearProgressIndicator(
                        progress = { event.maskingVolume },
                        modifier = Modifier
                            .width(60.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Primary,
                        trackColor = Primary.copy(alpha = 0.2f)
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${(event.maskingVolume * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Duration $durationFormatted",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ignored",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}

private fun sampleSessionEvents(): List<SessionEventItem> = listOf(
    SessionEventItem(
        timestampMs = System.currentTimeMillis() - 3600_000L * 3,
        eventClass = EventClass.GARBAGE_COLLECTION,
        score = 88,
        wasMasked = true,
        maskingSound = MaskingSound.BROWN_NOISE,
        maskingVolume = 0.18f,
        durationMs = 140_000L
    ),
    SessionEventItem(
        timestampMs = System.currentTimeMillis() - 3600_000L * 2,
        eventClass = EventClass.MOTORCYCLE,
        score = 32,
        wasMasked = false
    ),
    SessionEventItem(
        timestampMs = System.currentTimeMillis() - 3600_000L,
        eventClass = EventClass.DOG_BARKING,
        score = 74,
        wasMasked = true,
        maskingSound = MaskingSound.BROWN_NOISE,
        maskingVolume = 0.22f,
        durationMs = 65_000L
    )
)
