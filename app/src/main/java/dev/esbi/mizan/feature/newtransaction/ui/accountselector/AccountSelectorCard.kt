package dev.esbi.mizan.feature.newtransaction.ui.accountselector

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.design.kit.icon.IconValue
import dev.esbi.mizan.design.kit.icon.MizanIcon
import dev.esbi.mizan.design.theme.colors.LocalPremiumSystem
import dev.esbi.mizan.design.utils.IconRes
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
internal fun AccountSelectorCard(
    account: Account,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val premiumSystem = LocalPremiumSystem.current
    val accountColor = getAccountColor(account)

    // Glassmorphism effect
    val backgroundColor = if (isSelected) {
        premiumSystem.glass.bg.copy(alpha = 0.9f)
    } else {
        premiumSystem.glass.bg
    }
    val borderColor = if (isSelected) {
        premiumSystem.colors.emerald
    } else {
        premiumSystem.glass.border
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(premiumSystem.radius.xl))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        premiumSystem.colors.primary.copy(alpha = 0.1f),
                        premiumSystem.colors.primary.copy(alpha = 0.05f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(premiumSystem.radius.xl)
            )
            .clickable { onClick() },
        color = Color.Transparent,
        shape = RoundedCornerShape(premiumSystem.radius.xl)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Account Icon (first letter)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(accountColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = account.name.take(1).uppercase(),
                    color = accountColor,
                    fontWeight = FontWeight.Bold
                )
            }

            // Account Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) premiumSystem.colors.emerald
                    else premiumSystem.text.primary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = formatBalance(account.balance, account.currency.symbol),
                    style = MaterialTheme.typography.bodySmall,
                    color = premiumSystem.text.tertiary
                )
            }

            // Selection Indicator
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(premiumSystem.colors.emerald),
                    contentAlignment = Alignment.Center
                ) {
                    MizanIcon(
                        icon = IconValue(IconRes.ic_check),
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// Helper functions
internal fun getAccountColor(account: Account): Color {
    return Color(0xFF667EEA) // Default primary color
}


internal fun formatBalance(balance: BigDecimal, currencySymbol: String): String {
    val formatted = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(balance)

    return if (balance < BigDecimal.ZERO) {
        "-$formatted $currencySymbol"
    } else {
        "$formatted $currencySymbol"
    }
}