package com.owlen.app.presentation.ui.components

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.owlen.app.presentation.ui.theme.Green
import com.owlen.app.presentation.ui.theme.TextDisabled
import com.owlen.app.presentation.ui.theme.TextPrimary
import com.owlen.app.presentation.ui.theme.TextSecondary

/** List row with a trailing switch. Tapping anywhere on the row toggles it. */
@Composable
fun OwlenSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    enabled: Boolean = true,
    leading: (@Composable () -> Unit)? = null
) {
    OwlenRow(
        title = title,
        modifier = modifier,
        subtitle = subtitle,
        leading = leading,
        onClick = if (enabled) {
            { onCheckedChange(!checked) }
        } else {
            null
        },
        trailing = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Green,
                    checkedThumbColor = TextPrimary,
                    uncheckedTrackColor = TextDisabled,
                    uncheckedThumbColor = TextSecondary
                )
            )
        }
    )
}
