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
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.text.NumberFormat
import java.util.Locale

// ==========================================
// 1. DATA MODELS & MOCK DATA
// ==========================================


// TypeScriptdagi MOCK_ACCOUNTS ga mos ma'lumotlar

// ==========================================
// 2. COMPONENT
// ==========================================

@Composable
fun PremiumAccountSelector(
    label: String,
    selectedAccountId: Long?,
    onSelectAccount: (Long) -> Unit,
    excludeAccountId: String? = null
) {
    // Filtrlash: Agar excludeAccountId berilgan bo'lsa, uni ro'yxatdan olib tashlaymiz
    val availableAccounts = remember(excludeAccountId) { listOf<Account>() }

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
            selectedAccountId = 1,
            onSelectAccount = {},
            excludeAccountId = "savings"
        )
    }
}