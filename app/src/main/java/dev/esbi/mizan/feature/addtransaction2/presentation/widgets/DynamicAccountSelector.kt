package dev.esbi.mizan.feature.addtransaction2.presentation.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DynamicAccountSelector(
    accounts: List<Account>,
    selectedAccount: Account?,
    onSelectAccount: (Account) -> Unit,
    label: String,
    excludeAccount: Account? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MizanTheme.typography.bodyMd,
            color = MizanTheme.premium.text.tertiary,
            modifier = Modifier.padding(bottom = MizanTheme.premium.spacing.md)
        )

        val availableAccounts = if (excludeAccount != null) {
            accounts.filter { it.id != excludeAccount.id }
        } else {
            accounts
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            availableAccounts.forEach { account ->
                AccountItem(
                    account = account,
                    isSelected = selectedAccount?.id == account.id,
                    onClick = { onSelectAccount(account) }
                )
            }
        }
    }
}

@Composable
private fun AccountItem(
    account: Account,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MizanTheme.premium.colors.primary else MizanTheme.premium.colors.surface2,
            contentColor = if (isSelected) Color.White else MizanTheme.premium.text.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Account Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.name,
                    style = MizanTheme.typography.bodySm,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp
                )

                Spacer(Modifier.size(2.dp))

                Text(
                    text = formatCurrency(account.balance, account.currency.name),
                    style = MizanTheme.typography.bodyXs,
                    color = if (isSelected) Color.White.copy(alpha = 0.8f) else MizanTheme.premium.text.tertiary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

private fun formatCurrency(amount: BigDecimal, currency: String): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    try {
        format.currency = java.util.Currency.getInstance(currency)
    } catch (e: IllegalArgumentException) {
        // If currency code is invalid, use default
    }
    return format.format(amount)
}
