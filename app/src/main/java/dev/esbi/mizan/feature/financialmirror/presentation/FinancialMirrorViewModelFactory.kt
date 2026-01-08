package dev.esbi.mizan.feature.financialmirror.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Factory for creating FinancialMirrorViewModel instances
 * Required for Dagger2 injection with ViewModelProvider
 */
class FinancialMirrorViewModelFactory @Inject constructor(
    private val viewModel: FinancialMirrorViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}
