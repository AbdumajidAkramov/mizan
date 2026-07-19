package dev.esbi.mizan.design.components.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.design.utils.dashedBorder

@Composable
fun AddAccountButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .dashedBorder(
                color = MizanTheme.premium.colors.emerald.copy(alpha = 0.3f),
                strokeWidth = 2.dp,
                dashLength = 8.dp,
                gapLength = 6.dp,
                cornerRadius = MizanTheme.premium.radius.xl
            )
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.05f))
            .clickable { onClick() }
            .padding(MizanTheme.premium.spacing.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
        ) {
            // Plus Icon Container
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.1f))
                    .border(
                        width = 1.dp,
                        color = MizanTheme.premium.colors.emerald.copy(alpha = 0.3f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MizanTheme.premium.colors.emerald
                )
            }

            // Text Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Add New Account",
                    style = MizanTheme.typography.bodyLg.copy(
                        fontWeight = FontWeight.Medium,
                        color = MizanTheme.premium.colors.emerald
                    )
                )
                Text(
                    text = "Cash, Bank, Card, or Savings",
                    style = MizanTheme.typography.bodySm.copy(
                        color = MizanTheme.premium.text.tertiary
                    )
                )
            }
        }
    }
}
