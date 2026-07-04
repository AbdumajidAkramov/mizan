package dev.esbi.mizan.feature.newtransaction.ui.accountselector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.design.components.accounts.AddAccountButton
import dev.esbi.mizan.design.theme.colors.MizanTheme

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
                    Text(
                        text = "Accounts",
                        style = MaterialTheme.typography.labelSmall,
                        color = MizanTheme.premium.text.tertiary,
                    )
                }
                items(groupAccounts, key = { it.id }) { account ->
                    AccountSelectorCard(
                        account = account,
                        isSelected = account.id == selectedAccount?.id,
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
        item {
            Spacer(modifier = Modifier.height(56.dp))
        }
    }
}
