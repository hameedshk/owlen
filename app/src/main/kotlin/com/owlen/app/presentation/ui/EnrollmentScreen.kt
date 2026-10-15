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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.owlen.app.presentation.enrollment.EnrollmentViewModel.Phase
import com.owlen.app.presentation.ui.components.OwlenButton
import com.owlen.app.presentation.ui.components.OwlenButtonVariant
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.Stroke
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.ui.theme.owlenCard

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
                OwlenButton(
                    text = "Back",
                    onClick = onDone,
                    variant = OwlenButtonVariant.Secondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            phase == Phase.SAVED -> {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .owlenCard(fill = Green.copy(alpha = 0.15f), shape = CircleShape),
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
                OwlenButton(
                    text = "Done",
                    onClick = onDone,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OwlenButton(
                    text = "Enroll another sound",
                    onClick = onRestart,
                    variant = OwlenButtonVariant.Secondary,
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
                OwlenButton(
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

                // Take progress — soft dots
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    repeat(TAKES_REQUIRED) { index ->
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    if (index < takesCompleted) Green else Stroke,
                                    CircleShape
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
                        OwlenButton(
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

                OwlenButton(
                    text = "Cancel",
                    onClick = onDone,
                    variant = OwlenButtonVariant.Secondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
