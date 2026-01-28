package dev.esbi.mizan.feature.newtransaction.accountselect

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.text.NumberFormat
import java.util.Locale

/**
 * Account Selection Bottom Sheet Content
 * Premium design matching the app's design system
 */
@Composable
fun AccountSelectionContent(
    accounts: List<Account>,
    selectedAccountId: Long?,
    onAccountClick: (Account) -> Unit,
    onAddAccountClick: () -> Unit,
    onClose: () -> Unit
) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    // Group accounts by type
    val groupedAccounts = remember(accounts) {
        accounts.groupBy { account ->
            when (account.type) {
                Account.Type.CASH -> AccountGroupType.CASH
                Account.Type.CARD -> AccountGroupType.BANK
                Account.Type.SAVINGS -> AccountGroupType.BANK
                Account.Type.DEBT -> AccountGroupType.BANK
                Account.Type.INVESTMENT -> AccountGroupType.BANK
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MizanTheme.premium.background.primary)
    ) {
        // Drag Handle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MizanTheme.premium.colors.surface3)
            )
        }

        // Header
        AccountSelectionHeader(onClose = onClose)

        // Account List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = MizanTheme.premium.spacing.lg,
                vertical = MizanTheme.premium.spacing.md
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Cash Section
            groupedAccounts[AccountGroupType.CASH]?.let { cashAccounts ->
                item {
                    AccountSectionHeader(
                        title = "CASH",
                        iconRes = R.drawable.ic_attach_money
                    )
                }
                items(cashAccounts, key = { it.id }) { account ->
                    AccountCard(
                        account = account,
                        isSelected = account.id == selectedAccountId,
                        currencyFormat = currencyFormat,
                        onClick = { onAccountClick(account) }
                    )
                }
            }

            // Bank Accounts Section
            groupedAccounts[AccountGroupType.BANK]?.let { bankAccounts ->
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    AccountSectionHeader(
                        title = "BANK ACCOUNTS",
                        iconRes = R.drawable.ic_home
                    )
                }
                items(bankAccounts, key = { it.id }) { account ->
                    AccountCard(
                        account = account,
                        isSelected = account.id == selectedAccountId,
                        currencyFormat = currencyFormat,
                        onClick = { onAccountClick(account) }
                    )
                }
            }

            // Add Account Button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                AddAccountButton(onClick = onAddAccountClick)
            }
        }
    }
}

@Composable
private fun AccountSelectionHeader(onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MizanTheme.premium.spacing.lg,
                vertical = MizanTheme.premium.spacing.md
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Select Account",
            style = MizanTheme.typography.headingMd,
            color = MizanTheme.premium.text.primary,
            fontWeight = FontWeight.SemiBold
        )

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MizanTheme.premium.colors.surface2)
                .clickable { onClose() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Close",
                tint = MizanTheme.premium.text.secondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun AccountSectionHeader(
    title: String,
    iconRes: Int
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = MizanTheme.premium.text.tertiary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = title,
            style = MizanTheme.typography.labelSm,
            color = MizanTheme.premium.text.tertiary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun AccountCard(
    account: Account,
    isSelected: Boolean,
    currencyFormat: NumberFormat,
    onClick: () -> Unit
) {
    val accountColor = getAccountColor(account)
    val backgroundColor = if (isSelected) {
        MizanTheme.premium.colors.emerald.copy(alpha = 0.1f)
    } else {
        MizanTheme.premium.colors.surface2
    }
    val borderColor = if (isSelected) {
        MizanTheme.premium.colors.emerald
    } else {
        Color.Transparent
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
                    )
                } else Modifier
            )
            .clickable { onClick() },
        color = backgroundColor,
        shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Account Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(accountColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = getAccountIcon(account.type)),
                    contentDescription = null,
                    tint = accountColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Account Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.name,
                    style = MizanTheme.typography.bodyMd,
                    color = if (isSelected) MizanTheme.premium.colors.emerald 
                            else MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = formatBalance(account.balance, account.currency.symbol),
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }

            // Selection Indicator
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MizanTheme.premium.colors.emerald),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AddAccountButton(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .border(
                width = 2.dp,
                color = MizanTheme.premium.glass.border,
                shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .clickable { onClick() },
        color = Color.Transparent,
        shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null,
                    tint = MizanTheme.premium.colors.emerald,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Add New Account",
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.colors.emerald,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Helper functions
private enum class AccountGroupType {
    CASH,
    BANK
}

private fun getAccountColor(account: Account): Color {
    // Try to parse the account's custom color first
    account.color?.let { colorHex ->
        try {
            return Color(android.graphics.Color.parseColor(colorHex))
        } catch (e: Exception) {
            // Fall through to default
        }
    }

    // Default colors based on account type
    return when (account.type) {
        Account.Type.CASH -> Color(0xFF10B981)      // Emerald
        Account.Type.CARD -> Color(0xFF667EEA)      // Primary Blue
        Account.Type.SAVINGS -> Color(0xFF4FACFE)   // Light Blue
        Account.Type.DEBT -> Color(0xFFF5576C)      // Red/Pink
        Account.Type.INVESTMENT -> Color(0xFFC471F5) // Purple
    }
}

private fun getAccountIcon(type: Account.Type): Int {
    return when (type) {
        Account.Type.CASH -> R.drawable.ic_attach_money
        Account.Type.CARD -> R.drawable.ic_attach_money
        Account.Type.SAVINGS -> R.drawable.ic_attach_money
        Account.Type.DEBT -> R.drawable.ic_attach_money
        Account.Type.INVESTMENT -> R.drawable.ic_attach_money
    }
}

private fun formatBalance(balance: Double, currencySymbol: String): String {
    val formatted = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(kotlin.math.abs(balance))

    return if (balance < 0) {
        "-$currencySymbol$formatted"
    } else {
        "$currencySymbol$formatted"
    }
}
