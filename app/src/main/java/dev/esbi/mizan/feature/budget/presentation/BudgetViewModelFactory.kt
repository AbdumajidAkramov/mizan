package dev.esbi.mizan.feature.budget.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Factory for creating BudgetViewModel instances
 * Required for Dagger2 injection with ViewModelProvider
 */
class BudgetViewModelFactory @Inject constructor(
    private val viewModel: BudgetViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}
