package com.owlen.app.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BabyChangingStation
import androidx.compose.material.icons.rounded.NotificationImportant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.owlen.app.domain.model.EventClass
import com.owlen.app.presentation.ui.theme.NeoPopPlate
import com.owlen.app.presentation.ui.theme.Safety
import com.owlen.app.presentation.ui.theme.SafetyDim
import com.owlen.app.presentation.ui.theme.SafetyEdge
import com.owlen.app.presentation.ui.theme.SafetyEdgeDeep
import com.owlen.app.presentation.ui.theme.TextPrimary

@Composable
fun SafetyAlertScreen(
    eventClass: EventClass = EventClass.SMOKE_ALARM,
    onDismiss: () -> Unit = {}
) {
    val isBabyCry = eventClass == EventClass.BABY_CRY
    val alertText = if (isBabyCry) "BABY CRY DETECTED" else "SMOKE ALARM DETECTED"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SafetyDim),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isBabyCry) Icons.Rounded.BabyChangingStation else Icons.Rounded.NotificationImportant,
                contentDescription = alertText,
                tint = Safety,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = alertText,
                style = MaterialTheme.typography.headlineMedium,
                color = Safety,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Masking Stopped",
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            NeoPopPlate(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                faceColor = Color.White,
                edgeRight = SafetyEdge,
                edgeBottom = SafetyEdgeDeep,
                strokeColor = Safety,
                depth = 6.dp
            ) {
                Text(
                    text = "DISMISS",
                    style = MaterialTheme.typography.labelLarge,
                    color = Safety
                )
            }
        }
    }
}
