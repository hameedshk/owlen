package com.owlen.app.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.owlen.app.presentation.ui.theme.StrokeBright
import com.owlen.app.presentation.ui.theme.SurfaceCard
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.neoPopCard

/**
 * NeoPop dialog chrome: hard-cornered matte panel with a bright 1dp stroke,
 * uppercase title, content slot, and optional NeoPop footer buttons.
 * State/logic stays at the call site — this is chrome only.
 */
@Composable
fun NeoPopDialog(
    title: String,
    onDismiss: () -> Unit,
    confirmText: String? = null,
    onConfirm: (() -> Unit)? = null,
    dismissText: String? = null,
    confirmVariant: NeoPopButtonVariant = NeoPopButtonVariant.Primary,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .neoPopCard(fill = SurfaceCard, stroke = StrokeBright, shape = RectangleShape)
                .padding(20.dp)
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
            if (confirmText != null || dismissText != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (dismissText != null) {
                        NeoPopButton(
                            text = dismissText,
                            onClick = onDismiss,
                            variant = NeoPopButtonVariant.Secondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (confirmText != null && onConfirm != null) {
                        NeoPopButton(
                            text = confirmText,
                            onClick = onConfirm,
                            variant = confirmVariant,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
