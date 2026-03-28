package dev.esbi.mizan.feature.accountselector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.Account
import java.text.NumberFormat
import java.util.Locale

/**
 * Simple version of AccountSelectionContent for use in bottom sheets
 * where the parent already manages the account state
 */
@Composable
internal fun AccountSelectionContentSimple(
    accounts: List<Account>,
    selectedAccount: Account?,
    onAccountClick: (Account) -> Unit,
    onAddAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    // Group accounts by type
    val groupedAccounts = remember(accounts) {
        accounts.groupBy { account ->
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
                    isSelected = account.id == selectedAccount?.id,
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
                    isSelected = account.id == selectedAccount?.id,
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
