package dev.esbi.mizan.feature.budget.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.feature.budget.domain.model.BudgetSummary

/**
 * MVIKotlin Store for Budget Screen
 * Manages state, intents, and labels for budget management
 */
interface BudgetStore : Store<BudgetStore.Intent, BudgetStore.State, BudgetStore.Label> {

    sealed interface Action {
        data object Init : Action
    }

    sealed interface Intent {
        data object Refresh : Intent
        data object Retry : Intent
        data class UpdateCategoryBudget(val categoryId: String, val budgetAmount: Double) : Intent
        data class CreateCategoryBudget(val categoryId: String, val categoryName: String, val budgetAmount: Double) : Intent
        data class DeleteCategoryBudget(val categoryId: String) : Intent
        data class ShowEditDialog(val categoryId: String) : Intent
        data object ShowAddDialog : Intent
        data object DismissDialog : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val budgetSummary: BudgetSummary? = null,
        val error: String? = null,
        val showEditDialog: Boolean = false,
        val showAddDialog: Boolean = false,
        val editingCategoryId: String? = null
    )

    sealed interface Label {
        data class ShowError(val message: String) : Label
        data class ShowSuccess(val message: String) : Label
    }
}
