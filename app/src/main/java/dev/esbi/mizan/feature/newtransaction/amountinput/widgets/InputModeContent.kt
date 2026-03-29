package dev.esbi.mizan.feature.newtransaction.amountinput.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.presentation.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@Composable
internal fun InputModeContent(
    inputMode: InputMode,
    onModeChange: (InputMode) -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                MizanTheme.premium.colors.surface1,
                RoundedCornerShape(MizanTheme.premium.radius.full)
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        listOf(
            InputMode.Manual to IconValue(Icons.ic_calculate),
            InputMode.Voice to IconValue(Icons.ic_mic),
            InputMode.Scan to IconValue(Icons.ic_camera_alt)
        ).forEach { (mode, icon) ->
            val isSelected = inputMode == mode
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MizanTheme.premium.colors.surface3 else Color.Transparent)
                    .clickable {
                        onModeChange(mode)
                    },
                contentAlignment = Alignment.Center
            ) {
                dev.esbi.mizan.ui.kit.icon.MizanIcon(
                    icon = icon,
                    modifier = Modifier.size(22.dp),
                    tint = if (isSelected) MizanTheme.premium.text.primary else MizanTheme.premium.text.tertiary
                )
            }
        }
    }
}
