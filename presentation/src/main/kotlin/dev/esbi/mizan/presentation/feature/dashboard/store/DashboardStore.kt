package dev.esbi.mizan.presentation.feature.dashboard.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.dashboard.DashboardSummary
import dev.esbi.mizan.presentation.feature.dashboard.store.DashboardStore.Intent
import dev.esbi.mizan.presentation.feature.dashboard.store.DashboardStore.Label
import dev.esbi.mizan.presentation.feature.dashboard.store.DashboardStore.State
import java.math.BigDecimal

interface DashboardStore : Store<Intent, State, Label> {

    sealed interface Action {
        data object Init : Action
    }

    sealed interface Intent {
        data object Refresh : Intent
        data object Retry : Intent
        data class CategoryClicked(val categoryId: String) : Intent
        data object AddTransactionClicked : Intent
        data object ViewAllTransactionsClicked : Intent
        data object ProfileClicked : Intent
    }

    data class State(
        val dashboardData: DashboardSummary? = null,
        val totalBalance: BigDecimal = BigDecimal.ZERO,
        val mainCurrency: Currency? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Label {
        data class NavigateToCategory(val categoryId: String) : Label
        data class ShowError(val message: String) : Label
        data object NavigateToNewTransaction : Label
        data object NavigateToTransactionsHub : Label
        data object NavigateToProfile : Label
    }
}
