package dev.esbi.mizan.presentation.feature.goals.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.presentation.feature.goals.domain.model.Goal

interface GoalsStore : Store<GoalsStore.Intent, GoalsStore.State, GoalsStore.Label> {

    sealed interface Action {
        data object Init : Action
    }

    sealed interface Intent {
        data class AddGoal(
            val name: String,
            val targetAmount: Double,
            val deadline: Long?,
            val icon: String,
            val color: String
        ) : Intent

        data class AddAmountToGoal(val goalId: Long, val amount: Double) : Intent
        data class DeleteGoal(val goalId: Long) : Intent
        data class ShowAddSavingsDialog(val goalId: Long) : Intent
        data object ShowAddGoalDialog : Intent
        data object DismissDialog : Intent
    }

    data class State(
        val goals: List<Goal> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val totalSaved: Double = 0.0,
        val totalTarget: Double = 0.0,
        val overallProgress: Double = 0.0,
        val showAddGoalDialog: Boolean = false,
        val showAddSavingsDialog: Boolean = false,
        val selectedGoalId: Long? = null
    )

    sealed interface Label {
        data class ShowError(val message: String) : Label
        data class ShowSuccess(val message: String) : Label
    }
}
