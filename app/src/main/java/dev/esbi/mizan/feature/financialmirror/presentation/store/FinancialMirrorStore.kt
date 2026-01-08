package dev.esbi.mizan.feature.financialmirror.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.feature.financialmirror.domain.model.FinancialMirrorSummary

/**
 * MVIKotlin Store for Financial Mirror Screen
 * Manages state, intents, and labels for financial projections and analysis
 */
interface FinancialMirrorStore : Store<FinancialMirrorStore.Intent, FinancialMirrorStore.State, FinancialMirrorStore.Label> {

    sealed interface Action{
        data object Init : Action
    }

    sealed class Intent {
        object Refresh : Intent()
        object Retry : Intent()
        data class SelectProjectionView(val view: ProjectionView) : Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val financialMirrorData: FinancialMirrorSummary? = null,
        val error: String? = null,
        val selectedProjectionView: ProjectionView = ProjectionView.REALISTIC
    )

    enum class ProjectionView {
        CONSERVATIVE,
        REALISTIC,
        OPTIMISTIC
    }

    sealed class Label {
        data class ShowError(val message: String) : Label()
        data class NavigateToInvestment(val opportunityId: String) : Label()
    }
}
