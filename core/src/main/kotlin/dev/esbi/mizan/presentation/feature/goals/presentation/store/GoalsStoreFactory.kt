package dev.esbi.mizan.presentation.feature.goals.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.presentation.feature.goals.domain.model.Goal
import dev.esbi.mizan.presentation.feature.goals.domain.repository.GoalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

class GoalsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val goalRepository: GoalRepository,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) {

    fun create(): GoalsStore =
        object : GoalsStore, Store<GoalsStore.Intent, GoalsStore.State, GoalsStore.Label> by storeFactory.create(
            name = "GoalsStore",
            initialState = GoalsStore.State(isLoading = true),
            bootstrapper = SimpleBootstrapper(GoalsStore.Action.Init),
            executorFactory = { ExecutorImpl(goalRepository, mainDispatcher) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class GoalsLoaded(val goals: List<Goal>) : Msg
        data class Error(val message: String) : Msg
        data class ShowAddSavingsDialog(val goalId: Long) : Msg
        data object ShowAddGoalDialog : Msg
        data object DismissDialog : Msg
    }

    private class ExecutorImpl(
        private val goalRepository: GoalRepository,
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ) : CoroutineExecutor<GoalsStore.Intent, GoalsStore.Action, GoalsStore.State, Msg, GoalsStore.Label>(
        mainContext = mainDispatcher
    ) {

        override fun executeAction(action: GoalsStore.Action) {
            when (action) {
                GoalsStore.Action.Init -> observeGoals()
            }
        }

        override fun executeIntent(intent: GoalsStore.Intent) {
            when (intent) {
                is GoalsStore.Intent.AddGoal -> addGoal(intent)
                is GoalsStore.Intent.AddAmountToGoal -> addAmount(intent.goalId, intent.amount)
                is GoalsStore.Intent.DeleteGoal -> deleteGoal(intent.goalId)
                is GoalsStore.Intent.ShowAddSavingsDialog -> dispatch(Msg.ShowAddSavingsDialog(intent.goalId))
                is GoalsStore.Intent.ShowAddGoalDialog -> dispatch(Msg.ShowAddGoalDialog)
                is GoalsStore.Intent.DismissDialog -> dispatch(Msg.DismissDialog)
            }
        }

        private fun observeGoals() {
            goalRepository.observeAll()
                .onEach { goals -> dispatch(Msg.GoalsLoaded(goals)) }
                .catch { e -> dispatch(Msg.Error(e.message ?: "Unknown error")) }
                .launchIn(scope)
        }

        private fun addGoal(intent: GoalsStore.Intent.AddGoal) {
            scope.launch {
                try {
                    goalRepository.insert(
                        Goal(
                            name = intent.name,
                            targetAmount = intent.targetAmount,
                            currentAmount = BigDecimal.ZERO,
                            deadline = intent.deadline,
                            icon = intent.icon,
                            color = intent.color
                        )
                    )
                    dispatch(Msg.DismissDialog)
                    publish(GoalsStore.Label.ShowSuccess("Goal created"))
                } catch (e: Exception) {
                    publish(GoalsStore.Label.ShowError(e.message ?: "Failed to create goal"))
                }
            }
        }

        private fun addAmount(goalId: Long, amount: BigDecimal) {
            scope.launch {
                try {
                    goalRepository.addAmount(goalId, amount)
                    dispatch(Msg.DismissDialog)
                    publish(GoalsStore.Label.ShowSuccess("Savings added"))
                } catch (e: Exception) {
                    publish(GoalsStore.Label.ShowError(e.message ?: "Failed to add savings"))
                }
            }
        }

        private fun deleteGoal(goalId: Long) {
            scope.launch {
                try {
                    goalRepository.delete(goalId)
                    publish(GoalsStore.Label.ShowSuccess("Goal deleted"))
                } catch (e: Exception) {
                    publish(GoalsStore.Label.ShowError(e.message ?: "Failed to delete goal"))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<GoalsStore.State, Msg> {
        override fun GoalsStore.State.reduce(msg: Msg): GoalsStore.State = when (msg) {
            is Msg.Loading -> copy(isLoading = true, error = null)
            is Msg.GoalsLoaded -> {
                val totalSaved = msg.goals.fold(BigDecimal.ZERO) { acc, g -> acc.add(g.currentAmount) }
                val totalTarget = msg.goals.fold(BigDecimal.ZERO) { acc, g -> acc.add(g.targetAmount) }
                val overall = if (totalTarget > BigDecimal.ZERO) (totalSaved.toFloat() / totalTarget.toFloat() * 100).toDouble() else 0.0
                copy(
                    isLoading = false,
                    goals = msg.goals,
                    totalSaved = totalSaved,
                    totalTarget = totalTarget,
                    overallProgress = overall,
                    error = null
                )
            }
            is Msg.Error -> copy(isLoading = false, error = msg.message)
            is Msg.ShowAddSavingsDialog -> copy(showAddSavingsDialog = true, showAddGoalDialog = false, selectedGoalId = msg.goalId)
            is Msg.ShowAddGoalDialog -> copy(showAddGoalDialog = true, showAddSavingsDialog = false, selectedGoalId = null)
            is Msg.DismissDialog -> copy(showAddGoalDialog = false, showAddSavingsDialog = false, selectedGoalId = null)
        }
    }
}
