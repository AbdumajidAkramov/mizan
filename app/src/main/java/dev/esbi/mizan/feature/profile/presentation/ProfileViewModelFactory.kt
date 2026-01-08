package dev.esbi.mizan.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Factory for creating ProfileViewModel instances
 * Required for Dagger2 injection with ViewModelProvider
 */
class ProfileViewModelFactory @Inject constructor(
    private val viewModel: ProfileViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}
