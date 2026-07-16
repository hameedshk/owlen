package com.owlen.app.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owlen.app.presentation.ui.theme.GlassFillStrong
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.OnPrimary
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.glass
import com.owlen.app.presentation.ui.theme.glow
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.Warning

@Composable
fun MorningSummaryScreen(
    protectedHours: Int = 7,
    protectedMinutes: Int = 48,
    eventCount: Int = 4,
    maskingCount: Int = 3,
    maskingDurationMinutes: Int = 18,
    interruptionCount: Int = 0,
    adjustSensitivitySuggested: Boolean = false,
    onAdjustSensitivity: () -> Unit = {},
    onViewDetails: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val quietNight = eventCount == 0

    // Outcome framing: say what Owlen did, not just the raw counts
    val outcomeText = when {
        quietNight -> "A quiet night — no masking needed."
        eventCount == 1 -> "Owlen protected you through 1 disruption last night."
        else -> "Owlen protected you through $eventCount disruptions last night."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Good Morning",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = outcomeText,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Summary card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glass(RoundedCornerShape(12.dp))
        ) {
            // Protected time row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Protected",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Text(
                    text = "${protectedHours}h ${protectedMinutes}m",
                    style = MaterialTheme.typography.titleLarge,
                    color = Green,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(color = GlassFillStrong, thickness = 1.dp)

            if (quietNight) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Quiet night — no masking needed.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            } else {
                // Events & masking row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Events",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = eventCount.toString(),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Masking",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = maskingCount.toString(),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "  ${maskingDurationMinutes} min",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }

                HorizontalDivider(color = GlassFillStrong, thickness = 1.dp)

                // Interruptions row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Interruptions",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = interruptionCount.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = if (interruptionCount == 0) Green else Warning,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Spec: prompt when several maskings had low scores — likely false positives
        if (adjustSensitivitySuggested) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .glass(RoundedCornerShape(12.dp), fill = Warning.copy(alpha = 0.10f))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Several maskings were triggered by low-scoring sounds.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Adjust sensitivity?",
                    style = MaterialTheme.typography.labelLarge,
                    color = Warning,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(onClick = onAdjustSensitivity)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (!quietNight) {
            Button(
                onClick = onViewDetails,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .glow(Primary, 20.dp, alpha = 0.22f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = OnPrimary
                )
            ) {
                Text(
                    text = "View Details",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        } else {
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .glow(Primary, 20.dp, alpha = 0.22f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = OnPrimary
                )
            ) {
                Text(
                    text = "Done",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
