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
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.ui.components.accounts.AddAccountButton
import dev.esbi.mizan.ui.utils.Icons
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

    // Group accounts by groupId
    val groupedAccounts = remember(accounts) {
        accounts.groupBy { it.groupId }
    }

    // Account List
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groupedAccounts.forEach { (_, groupAccounts) ->
            if (groupAccounts.isNotEmpty()) {
                item {
                    AccountSectionHeader(
                        title = "ACCOUNTS",
                        iconRes = Icons.ic_attach_money
                    )
                }
                items(groupAccounts, key = { it.id }) { account ->
                    AccountSelectorCard(
                        account = account,
                        isSelected = account.id == selectedAccount?.id,
                        currencyFormat = currencyFormat,
                        onClick = { onAccountClick(account) }
                    )
                }
            }
        }

        // Add Account Button
        item {
            Spacer(modifier = Modifier.height(16.dp))
            AddAccountButton(onClick = onAddAccountClick)
        }
    }
}
