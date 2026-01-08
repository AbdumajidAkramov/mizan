package dev.esbi.mizan.feature.transactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Factory for creating TransactionsViewModel instances
 * Required for Dagger2 injection with ViewModelProvider
 */
class TransactionsViewModelFactory @Inject constructor(
    private val viewModel: TransactionsViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}
