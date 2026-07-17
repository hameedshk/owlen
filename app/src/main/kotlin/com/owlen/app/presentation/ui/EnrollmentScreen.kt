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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.owlen.app.presentation.enrollment.EnrollmentViewModel.Phase
import com.owlen.app.presentation.ui.components.NeoPopButton
import com.owlen.app.presentation.ui.components.NeoPopButtonVariant
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.Stroke
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.neoPopCard

private const val TAKES_REQUIRED = 3

@Composable
fun EnrollmentScreen(
    name: String,
    takesCompleted: Int,
    phase: Phase,
    liveDb: Float,
    monitoringActive: Boolean,
    onNameChanged: (String) -> Unit,
    onRecordTake: () -> Unit,
    onRestart: () -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Custom Disturbance",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Record a sound that disturbs your sleep.\nOwlen will react when it hears it again.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        when {
            monitoringActive -> {
                Text(
                    text = "Stop sleep protection to enroll sounds.\nThe microphone is in use.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                NeoPopButton(
                    text = "Back",
                    onClick = onDone,
                    variant = NeoPopButtonVariant.Secondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            phase == Phase.SAVED -> {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .neoPopCard(fill = Green.copy(alpha = 0.15f), stroke = Green),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✓", style = MaterialTheme.typography.titleLarge, color = Green)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Saved. Owlen now listens for this sound.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Green,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                NeoPopButton(
                    text = "Done",
                    onClick = onDone,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                NeoPopButton(
                    text = "Enroll another sound",
                    onClick = onRestart,
                    variant = NeoPopButtonVariant.Secondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            phase == Phase.INCONSISTENT -> {
                Text(
                    text = "Those recordings didn't sound alike.\nMake the same sound each time, and stay quiet otherwise.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                NeoPopButton(
                    text = "Try Again",
                    onClick = onRestart,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            else -> {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChanged,
                    label = { Text(text = "Name (e.g. Bedroom door)") },
                    singleLine = true,
                    enabled = takesCompleted == 0 && phase == Phase.IDLE,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Stroke,
                        focusedLabelColor = Primary,
                        unfocusedLabelColor = TextSecondary,
                        cursorColor = Primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Take progress — hard squares
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    repeat(TAKES_REQUIRED) { index ->
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    if (index < takesCompleted) Green else Stroke,
                                    RectangleShape
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Take ${(takesCompleted + 1).coerceAtMost(TAKES_REQUIRED)} of $TAKES_REQUIRED",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                when (phase) {
                    Phase.RECORDING -> {
                        Box(
                            modifier = Modifier.size(96.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Primary, strokeWidth = 4.dp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Listening — make the sound now",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Primary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${liveDb.toInt()} dB",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }

                    Phase.PROCESSING -> {
                        CircularProgressIndicator(color = Primary, strokeWidth = 4.dp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Processing...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }

                    else -> {
                        NeoPopButton(
                            text = if (takesCompleted == 0) "Record" else "Record next take",
                            onClick = onRecordTake,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Get ready, then tap Record and make the sound\nwithin 3 seconds. Stay quiet otherwise.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                NeoPopButton(
                    text = "Cancel",
                    onClick = onDone,
                    variant = NeoPopButtonVariant.Secondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
