package dev.esbi.mizan.ui.components.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.kit.balance.BalanceAmount
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal

@Composable
fun AccountRow(
    id: Long,
    name: String,
    balance: Double,
    currencyCode: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    GlassCard(
        cornerRadius = RoundedCornerShape(MizanTheme.premium.radius.md)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Account Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = MizanTheme.premium.text.primary,
                    style = MizanTheme.typography.bodyLg,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            BalanceAmount(
                modifier = Modifier.padding(horizontal = 12.dp),
                balance = BigDecimal(balance),
                currency = currencyCode,
                typography = MizanTheme.typography.bodyLg
            )

            // Chevron
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
