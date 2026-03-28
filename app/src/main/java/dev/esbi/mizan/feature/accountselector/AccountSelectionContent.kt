package dev.esbi.mizan.feature.accountselector

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.feature.accountselector.store.AccountSelectorStore
import dev.esbi.mizan.ui.theme.colors.LocalPremiumSystem
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

/**
 * Account Selection Bottom Sheet Content
 * Premium design matching the app's design system
 */

@Composable
fun AccountSelectionContent(
    state: AccountSelectorStore.State,
    onIntent: (AccountSelectorStore.Intent) -> Unit,
    onAddAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Load accounts when content is first displayed
    LaunchedEffect(Unit) {
        onIntent(AccountSelectorStore.Intent.LoadAccounts)
    }
    val premiumSystem = LocalPremiumSystem.current
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    // Handle loading state
    if (state.isLoading) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = premiumSystem.colors.primary
            )
        }
        return
    }

    // Handle error state
    state.error?.let { error ->
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = premiumSystem.colors.error
                )

                // Retry button
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(premiumSystem.radius.md))
                        .clickable { onIntent(AccountSelectorStore.Intent.RetryLoad) },
                    color = premiumSystem.colors.primary,
                    shape = RoundedCornerShape(premiumSystem.radius.md)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        text = "Retry",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        return
    }

    // Group accounts by type
    val groupedAccounts = remember(state.accounts) {
        state.accounts.groupBy { account ->
            when (account.type) {
                Account.Type.CASH -> AccountGroupType.CASH
                Account.Type.CARD -> AccountGroupType.BANK
                Account.Type.BANK -> AccountGroupType.BANK
                Account.Type.SAVINGS -> AccountGroupType.BANK
                Account.Type.DEBT -> AccountGroupType.BANK
                Account.Type.CREDIT -> AccountGroupType.BANK
                Account.Type.INVESTMENT -> AccountGroupType.BANK
            }
        }
    }

    // Account List
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
                    isSelected = account.id == state.selectedAccountId,
                    currencyFormat = currencyFormat,
                    onClick = { onIntent(AccountSelectorStore.Intent.SelectAccount(account)) }
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
                    isSelected = account.id == state.selectedAccountId,
                    currencyFormat = currencyFormat,
                    onClick = { onIntent(AccountSelectorStore.Intent.SelectAccount(account)) }
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

@Composable
internal fun AccountSelectionHeader(onClose: () -> Unit) {
    val premiumSystem = LocalPremiumSystem.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = premiumSystem.spacing.md,
                vertical = premiumSystem.spacing.sm
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Select Account",
            style = MaterialTheme.typography.titleMedium,
            color = premiumSystem.text.primary,
            fontWeight = FontWeight.SemiBold
        )

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(premiumSystem.colors.surface2)
                .clickable { onClose() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Close",
                tint = premiumSystem.text.secondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
internal fun AccountSectionHeader(
    title: String,
    iconRes: Int
) {
    val premiumSystem = LocalPremiumSystem.current
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = premiumSystem.text.tertiary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = premiumSystem.text.tertiary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
internal fun AccountCard(
    account: Account,
    isSelected: Boolean,
    currencyFormat: NumberFormat,
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
internal fun AddAccountButton(onClick: () -> Unit) {
    val premiumSystem = LocalPremiumSystem.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        label = "button_scale"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = premiumSystem.spacing.sm
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(premiumSystem.radius.xl))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(premiumSystem.radius.xl),
        color = premiumSystem.colors.emerald.copy(alpha = 0.05f)
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
                    .background(premiumSystem.colors.emerald.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null,
                    tint = premiumSystem.colors.emerald,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Add New Account",
                style = MaterialTheme.typography.bodyMedium,
                color = premiumSystem.colors.emerald,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Helper functions
internal enum class AccountGroupType {
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
        Account.Type.BANK -> Color(0xFF667EEA)      // Primary Blue
        Account.Type.SAVINGS -> Color(0xFF4FACFE)   // Light Blue
        Account.Type.DEBT -> Color(0xFFF5576C)      // Red/Pink
        Account.Type.CREDIT -> Color(0xFFF5576C)    // Red/Pink
        Account.Type.INVESTMENT -> Color(0xFFC471F5) // Purple
    }
}

private fun getAccountIcon(type: Account.Type): Int {
    return when (type) {
        Account.Type.CASH -> R.drawable.ic_attach_money
        Account.Type.CARD -> R.drawable.ic_attach_money
        Account.Type.BANK -> R.drawable.ic_attach_money
        Account.Type.SAVINGS -> R.drawable.ic_attach_money
        Account.Type.DEBT -> R.drawable.ic_attach_money
        Account.Type.CREDIT -> R.drawable.ic_attach_money
        Account.Type.INVESTMENT -> R.drawable.ic_attach_money
    }
}

private fun formatBalance(balance: Double, currencySymbol: String): String {
    val formatted = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(abs(balance))

    return if (balance < 0) {
        "-$formatted $currencySymbol"
    } else {
        "$formatted $currencySymbol"
    }
}


@Preview(showBackground = false)
@Composable
fun AccountSelectionContentPreview() {

    val uzs = Currency(
        code = "UZS",
        name = "O'zbek so'mi",
        symbol = "so'm",
        rateToBase = 1.0,
        isBaseCurrency = true
    )

    val usd = Currency(
        code = "USD",
        name = "US Dollar",
        symbol = "$",
        rateToBase = 12_500.0,
        isBaseCurrency = false
    )

    val mockAccounts = listOf(
        Account(
            id = 1L,
            groupId = 100L,
            name = "Cash Wallet",
            type = Account.Type.CASH,
            balance = 250_000.0,
            currency = uzs,
            iconName = "ic_cash",
            color = "#4CAF50",
            isArchived = false,
            excludeFromTotal = false,
            description = "Main daily cash"
        ),
        Account(
            id = 2L,
            groupId = 100L,
            name = "Humo Card",
            type = Account.Type.CARD,
            balance = 1_450_000.0,
            currency = uzs,
            iconName = "ic_card",
            color = "#2196F3",
            isArchived = false,
            excludeFromTotal = false,
            description = null
        ),
        Account(
            id = 3L,
            groupId = 200L,
            name = "Visa USD",
            type = Account.Type.CARD,
            balance = 320.0,
            currency = usd,
            iconName = "ic_visa",
            color = "#FF9800",
            isArchived = false,
            excludeFromTotal = false,
            description = "Online payments"
        )
    )

    dev.esbi.mizan.ui.theme.MizanTheme() {
        AccountSelectionContent(
            state = dev.esbi.mizan.feature.accountselector.store.AccountSelectorStore.State(
                isLoading = false,
                accounts = mockAccounts,
                selectedAccountId = null,
                error = null
            ),
            onIntent = {},
            onAddAccountClick = {},
        )
    }
}
