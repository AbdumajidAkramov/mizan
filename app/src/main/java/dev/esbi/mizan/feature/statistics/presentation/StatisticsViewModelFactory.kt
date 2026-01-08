package dev.esbi.mizan.feature.statistics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Factory for creating StatisticsViewModel instances
 * Required for Dagger2 injection with ViewModelProvider
 */
class StatisticsViewModelFactory @Inject constructor(
    private val viewModel: StatisticsViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}
