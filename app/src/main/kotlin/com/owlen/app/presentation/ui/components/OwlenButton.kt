package com.owlen.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.painter.Painter
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.OnPrimary
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.Safety
import com.owlen.app.presentation.ui.theme.Stroke
import com.owlen.app.presentation.ui.theme.SurfaceCard
import com.owlen.app.presentation.ui.theme.TextDisabled
import com.owlen.app.presentation.ui.theme.TextPrimary

enum class OwlenButtonVariant { Primary, Secondary, Danger, Success }

/**
 * Flat Owlen CTA: soft 28dp pill, 56dp tall, optional leading icon.
 * Label is NOT uppercased (unlike the old NeoPop button).
 */
@Composable
fun OwlenButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: OwlenButtonVariant = OwlenButtonVariant.Primary,
    enabled: Boolean = true,
    leadingIcon: Painter? = null
) {
    val (backgroundColor, contentColor) = when (variant) {
        OwlenButtonVariant.Primary -> Primary to OnPrimary
        OwlenButtonVariant.Secondary -> SurfaceCard to TextPrimary
        OwlenButtonVariant.Danger -> Safety to OnPrimary
        OwlenButtonVariant.Success -> Green to OnPrimary
    }

    Row(
        modifier = modifier
            .height(56.dp)
            .background(
                color = if (enabled) backgroundColor else Stroke,
                shape = RoundedCornerShape(28.dp)
            )
            .then(
                if (enabled && onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            )
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (leadingIcon != null) {
            Icon(
                painter = leadingIcon,
                contentDescription = null,
                tint = if (enabled) contentColor else TextDisabled,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = if (enabled) contentColor else TextDisabled
        )
    }
}
