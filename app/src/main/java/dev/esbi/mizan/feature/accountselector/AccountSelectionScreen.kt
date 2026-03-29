package dev.esbi.mizan.feature.accountselector

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.esbi.mizan.presentation.feature.accountselector.store.AccountSelectorStore
import dev.esbi.mizan.ui.theme.MizanTheme

@Composable
fun AccountSelectionScreen(
    viewModel: AccountSelectorViewModel,
    onClose: () -> Unit = {},
    onAccountSelected: (dev.esbi.mizan.domain.model.Account) -> Unit = {},
    onAddAccountClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState(initial = AccountSelectorStore.State())

    // Handle label emissions
    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is AccountSelectorStore.Label.AccountSelected -> {
                    onAccountSelected(label.account)
                    onClose()
                }

                is AccountSelectorStore.Label.ShowError -> {
                    // Handle error display (could show a snackbar or toast)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AccountSelectionHeader(onClose = onClose)
        }
    ) { paddingValues ->
        AccountSelectionContent(
            state = state,
            onIntent = viewModel::onIntent,
            onAddAccountClick = onAddAccountClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun AccountSelectionScreenPreview() {
    MizanTheme() {
//        AccountSelectionScreen()
    }
}