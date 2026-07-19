package dev.esbi.mizan.features.addtransaction.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.design.utils.IconRes

@Composable
fun TransactionTypeChip(
    label: String,
    color: Color,
    iconRes: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(MizanTheme.premium.radius.full))
            .border(
                width = 1.5.dp,
                color = color,
                shape = RoundedCornerShape(MizanTheme.premium.radius.full)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = label,
            style = MizanTheme.typography.bodySm,
            color = color,
            fontWeight = FontWeight.Medium
        )
        // Dropdown chevron indicator
        Icon(
            painter = painterResource(id = IconRes.ic_chevron_down),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
    }
}
