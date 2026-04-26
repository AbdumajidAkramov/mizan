package dev.esbi.mizan.feature.newtransaction2.transactiontype

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.presentation.feature.addtransaction.model.TransactionType
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

data class TransactionTypeInfo(
    val type: TransactionType,
    val label: String,
    val description: String,
    val iconRes: Int,
    val color: Color,
    val bgColor: Color
)

@Composable
fun TransactionTypeItem(
    typeInfo: TransactionTypeInfo,
    isSelected: Boolean,
    scale: Float,
    onClick: () -> Unit,
    onPress: () -> Unit,
    onRelease: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(
                if (isSelected) {
                    MizanTheme.premium.colors.emerald.copy(alpha = 0.1f)
                } else {
                    MizanTheme.premium.colors.surface2
                }
            )
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MizanTheme.premium.colors.emerald else Color.Transparent,
                shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .clickable {
                onPress()
                onClick()
                onRelease()
            }
            .padding(MizanTheme.premium.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        // Icon Container
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                .background(if (isSelected) typeInfo.color else typeInfo.bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = typeInfo.iconRes),
                contentDescription = null,
                tint = if (isSelected) Color.White else typeInfo.color,
                modifier = Modifier.size(24.dp)
            )
        }

        // Text Column
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = typeInfo.label,
                style = MizanTheme.typography.bodyMd,
                color = if (isSelected) MizanTheme.premium.colors.emerald
                else MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = typeInfo.description,
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
        }

        // Check Icon (only when selected)
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.emerald),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = Icons.ic_chevron_right),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        } else {
            Spacer(modifier = Modifier.width(24.dp))
        }
    }
}
