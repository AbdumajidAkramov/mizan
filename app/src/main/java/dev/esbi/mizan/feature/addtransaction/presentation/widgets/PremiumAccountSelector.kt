package dev.esbi.mizan.feature.addtransaction.presentation.widgets

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
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.addtransaction.presentation.models.Account
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.text.NumberFormat
import java.util.Locale

// ==========================================
// 1. DATA MODELS & MOCK DATA
// ==========================================


// TypeScriptdagi MOCK_ACCOUNTS ga mos ma'lumotlar
val MOCK_ACCOUNTS = listOf(
    Account(
        id = "cash",
        name = "Cash",
        type = "cash",
        balance = 1250.00,
        icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_wallet), // Wallet icon
        color = Color(0xFF10B981)    // Emerald
    ),
    Account(
        id = "bank-checking",
        name = "Bank Checking",
        type = "bank",
        balance = 5430.50,
        icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_track_changes), // Building icon
        color = Color(0xFF667EEA)    // Primary Blue
    ),
    Account(
        id = "savings",
        name = "Savings Account",
        type = "savings",
        balance = 12500.00,
        icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_wallet), // PiggyBank o'rniga Savings yoki shunga o'xshash
        color = Color(0xFF4FACFE)    // Light Blue
    ),
    Account(
        id = "credit-card",
        name = "Credit Card",
        type = "credit",
        balance = -850.00,
        icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_wallet),
        color = Color(0xFFF5576C)    // Red/Pink
    ),
    Account(
        id = "investment",
        name = "Investment",
        type = "investment",
        balance = 8200.00,
        icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_trend_up), // Landmark/Investment o'rniga
        color = Color(0xFFC471F5)    // Purple
    )
)

// ==========================================
// 2. COMPONENT
// ==========================================

@Composable
fun PremiumAccountSelector(
    label: String,
    selectedAccountId: String?,
    onSelectAccount: (String) -> Unit,
    excludeAccountId: String? = null
) {
    // Filtrlash: Agar excludeAccountId berilgan bo'lsa, uni ro'yxatdan olib tashlaymiz
    val availableAccounts = remember(excludeAccountId) {
        MOCK_ACCOUNTS.filter { it.id != excludeAccountId }
    }

    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    Column {
        // Label
        Text(
            text = label,
            style = MizanTheme.typography.bodyMd,
            fontWeight = FontWeight.Medium,
            color = MizanTheme.premium.text.secondary,
            modifier = Modifier.padding(bottom = MizanTheme.premium.spacing.md)
        )

        // List
        Column(verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)) {
            availableAccounts.forEach { account ->
                val isSelected = selectedAccountId == account.id

                // Style variables based on selection
                val backgroundColor =
                    if (isSelected) MizanTheme.premium.colors.surface3 else MizanTheme.premium.colors.surface2
                val borderModifier = if (isSelected) {
                    Modifier.border(
                        2.dp,
                        MizanTheme.premium.colors.success,
                        RoundedCornerShape(MizanTheme.premium.radius.lg)
                    ) // Ring effect
                } else {
                    Modifier
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                        .then(borderModifier)
                        .background(backgroundColor)
                        .clickable { onSelectAccount(account.id) }
                        .padding(MizanTheme.premium.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
                ) {
                    // Icon Box
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
                            .background(account.color.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        dev.esbi.mizan.ui.kit.icon.Icon(
                            icon = account.icon,
                            contentDescription = null,
                            tint = account.color,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Account Info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = account.name,
                            style = MizanTheme.typography.bodyMd,
                            fontWeight = FontWeight.Medium,
                            color = MizanTheme.premium.text.primary,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        Text(
                            text = currencyFormat.format(account.balance),
                            style = MizanTheme.typography.bodySm,
                            color = MizanTheme.premium.text.tertiary
                        )
                    }

                    // Selection Indicator (Checkmark)
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(MizanTheme.premium.colors.success),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "Selected",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. PREVIEW
// ==========================================

@Preview(showBackground = true)
@Composable
private fun AccountSelectorPreview() {
    // MizanTheme contextida
    Box(modifier = Modifier.padding(24.dp)) {
        PremiumAccountSelector(
            label = "From Account",
            selectedAccountId = "cash",
            onSelectAccount = {},
            excludeAccountId = "savings"
        )
    }
}