package dev.esbi.mizan.feature.transactionshub

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import dev.esbi.mizan.feature.transactionshub.ui.TransactionsHubContent
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore

/**
 * TransactionsHub screen composable
 * Entry point for the transactions hub feature
 */
@Composable
fun TransactionsHubScreen(
    viewModel: TransactionsHubViewModel,
    onBackClick: () -> Unit,
    onAddTransactionClick: () -> Unit,
    onEditTransactionClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val labels = viewModel.labels

    val lifecycleOwner = LocalLifecycleOwner.current

    // Labellarni xavfsiz eshitish (Lifecycle-ga sezgir holatda)
    LaunchedEffect(viewModel.labels, lifecycleOwner) {
        viewModel.labels
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { label ->
                when (label) {
                    is TransactionsHubStore.Label.NavigateBack -> onBackClick()
                    is TransactionsHubStore.Label.NavigateToAddTransaction -> onAddTransactionClick()
                    is TransactionsHubStore.Label.NavigateToEditTransaction -> onEditTransactionClick(
                        label.transaction.id
                    )
                }
            }
    }

    TransactionsHubContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}
