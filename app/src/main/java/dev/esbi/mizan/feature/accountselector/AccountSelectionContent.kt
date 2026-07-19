package dev.esbi.mizan.feature.accountselector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.theme.MizanTheme
import dev.esbi.mizan.design.theme.colors.LocalPremiumSystem
import dev.esbi.mizan.design.utils.IconRes
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.features.addtransaction.ui.accountselector.AccountSectionHeader
import dev.esbi.mizan.features.addtransaction.ui.accountselector.AccountSelectorCard
import dev.esbi.mizan.presentation.feature.accountselector.store.AccountSelectorStore
import java.math.BigDecimal

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

    // Group accounts by groupId
    val groupedAccounts = remember(state.accounts) {
        state.accounts.groupBy { it.groupId }
    }

    // Account List
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groupedAccounts.forEach { (_, accounts) ->
            if (accounts.isNotEmpty()) {
                item {
                    AccountSectionHeader(
                        title = "ACCOUNTS",
                        iconRes = IconRes.ic_attach_money
                    )
                }
                items(accounts, key = { it.id }) { account ->
                    AccountSelectorCard(
                        account = account,
                        isSelected = account.id == state.selectedAccountId,
                        onClick = { onIntent(AccountSelectorStore.Intent.SelectAccount(account)) }
                    )
                }
            }
        }

        // Add Account Button
        item {
            Spacer(modifier = Modifier.height(16.dp))
            _root_ide_package_.dev.esbi.mizan.design.components.accounts.AddAccountButton(onClick = onAddAccountClick)
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
                painter = painterResource(id = IconRes.ic_close),
                contentDescription = "Close",
                tint = premiumSystem.text.secondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

/*
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
                    painter = painterResource(id = MizanIcons.ic_add),
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
*/


@Preview(showBackground = false)
@Composable
fun AccountSelectionContentPreview() {

    val uzs = Currency(
        code = "UZS",
        name = "O'zbek so'mi",
        symbol = "so'm",
        exchangeRate = java.math.BigDecimal.ONE,
        unitPosition = dev.esbi.mizan.domain.model.UnitPosition.FRONT,
        decimalDigits = 0,
        orderIndex = 0,
        isMainCurrency = true,
        isUserDefined = false
    )

    val usd = Currency(
        code = "USD",
        name = "US Dollar",
        symbol = "$",
        exchangeRate = java.math.BigDecimal("12500"),
        unitPosition = dev.esbi.mizan.domain.model.UnitPosition.FRONT,
        decimalDigits = 2,
        orderIndex = 1,
        isMainCurrency = false,
        isUserDefined = false
    )

    val mockAccounts = listOf(
        Account(
            id = 1L,
            groupId = 100L,
            name = "Cash Wallet",
            balance = BigDecimal(250_000.0),
            currency = uzs,
            isArchived = false,
            excludeFromTotal = false,
            description = "Main daily cash"
        ),
        Account(
            id = 2L,
            groupId = 100L,
            name = "Humo Card",
            balance = BigDecimal(1_450_000.0),
            currency = uzs,
            isArchived = false,
            excludeFromTotal = false,
            description = null
        ),
        Account(
            id = 3L,
            groupId = 200L,
            name = "Visa USD",
            balance = BigDecimal(320.0),
            currency = usd,
            isArchived = false,
            excludeFromTotal = false,
            description = "Online payments"
        )
    )

    MizanTheme() {
        AccountSelectionContent(
            state = AccountSelectorStore.State(
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
