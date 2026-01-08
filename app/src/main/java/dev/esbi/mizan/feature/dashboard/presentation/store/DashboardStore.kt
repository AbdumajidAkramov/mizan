package dev.esbi.mizan.feature.dashboard.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.feature.dashboard.domain.model.DashboardSummary
import dev.esbi.mizan.feature.dashboard.presentation.store.DashboardStore.Intent
import dev.esbi.mizan.feature.dashboard.presentation.store.DashboardStore.Label
import dev.esbi.mizan.feature.dashboard.presentation.store.DashboardStore.State

interface DashboardStore : Store<Intent, State, Label> {

    sealed interface Action {
        data object Init : Action
    }

    sealed interface Intent {
        data object Refresh : Intent
        data object Retry : Intent
        data class CategoryClicked(val categoryId: String) : Intent
    }

    data class State(
        val dashboardData: DashboardSummary? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Label {
        data class NavigateToCategory(val categoryId: String) : Label
        data class ShowError(val message: String) : Label
    }
}
