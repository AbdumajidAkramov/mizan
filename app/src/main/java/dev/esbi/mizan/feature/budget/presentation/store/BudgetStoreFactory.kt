package dev.esbi.mizan.feature.budget.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.feature.budget.domain.model.BudgetSummary
import dev.esbi.mizan.feature.budget.domain.usecase.CreateCategoryBudgetUseCase
import dev.esbi.mizan.feature.budget.domain.usecase.DeleteCategoryBudgetUseCase
import dev.esbi.mizan.feature.budget.domain.usecase.ObserveBudgetSummaryUseCase
import dev.esbi.mizan.feature.budget.domain.usecase.RefreshBudgetsUseCase
import dev.esbi.mizan.feature.budget.domain.usecase.UpdateCategoryBudgetUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Factory for creating BudgetStore instances
 * Implements MVIKotlin pattern with Executor-Reducer architecture
 */
class BudgetStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val observeBudgetSummaryUseCase: ObserveBudgetSummaryUseCase,
    private val updateCategoryBudgetUseCase: UpdateCategoryBudgetUseCase,
    private val createCategoryBudgetUseCase: CreateCategoryBudgetUseCase,
    private val deleteCategoryBudgetUseCase: DeleteCategoryBudgetUseCase,
    private val refreshBudgetsUseCase: RefreshBudgetsUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) {

    fun create(): BudgetStore =
        object : BudgetStore, Store<BudgetStore.Intent, BudgetStore.State, BudgetStore.Label> by storeFactory.create(
            name = "BudgetStore",
            initialState = BudgetStore.State(isLoading = true),
            bootstrapper = SimpleBootstrapper(BudgetStore.Action.Init),
            executorFactory = {
                ExecutorImpl(
                    observeBudgetSummaryUseCase,
                    updateCategoryBudgetUseCase,
                    createCategoryBudgetUseCase,
                    deleteCategoryBudgetUseCase,
                    refreshBudgetsUseCase,
                    mainDispatcher
                )
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class DataLoaded(val summary: BudgetSummary) : Msg
        data class Error(val message: String) : Msg
        data class ShowEditDialog(val categoryId: String) : Msg
        data object ShowAddDialog : Msg
        data object DismissDialog : Msg
    }

    private class ExecutorImpl(
        private val observeBudgetSummaryUseCase: ObserveBudgetSummaryUseCase,
        private val updateCategoryBudgetUseCase: UpdateCategoryBudgetUseCase,
        private val createCategoryBudgetUseCase: CreateCategoryBudgetUseCase,
        private val deleteCategoryBudgetUseCase: DeleteCategoryBudgetUseCase,
        private val refreshBudgetsUseCase: RefreshBudgetsUseCase,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher
    ) : CoroutineExecutor<BudgetStore.Intent, BudgetStore.Action, BudgetStore.State, Msg, BudgetStore.Label>(
        mainContext = mainDispatcher
    ) {
        
        override fun executeAction(action: BudgetStore.Action) {
            when (action) {
                BudgetStore.Action.Init -> {
                    observeBudgets()
                    refresh()
                }
            }
        }

        override fun executeIntent(intent: BudgetStore.Intent) {
            when (intent) {
                is BudgetStore.Intent.Refresh -> refresh()
                is BudgetStore.Intent.Retry -> retry()
                is BudgetStore.Intent.UpdateCategoryBudget -> updateBudget(intent.categoryId, intent.budgetAmount)
                is BudgetStore.Intent.CreateCategoryBudget -> createBudget(intent.categoryId, intent.categoryName, intent.budgetAmount)
                is BudgetStore.Intent.DeleteCategoryBudget -> deleteBudget(intent.categoryId)
                is BudgetStore.Intent.ShowEditDialog -> dispatch(Msg.ShowEditDialog(intent.categoryId))
                is BudgetStore.Intent.ShowAddDialog -> dispatch(Msg.ShowAddDialog)
                is BudgetStore.Intent.DismissDialog -> dispatch(Msg.DismissDialog)
            }
        }

        private fun observeBudgets() {
            observeBudgetSummaryUseCase()
                .onEach { summary ->
                    dispatch(Msg.DataLoaded(summary))
                }
                .catch { e ->
                    dispatch(Msg.Error(e.message ?: "Unknown error"))
                }
                .launchIn(scope)
        }

        private fun refresh() {
            dispatch(Msg.Loading)
            scope.launch {
                try {
                    refreshBudgetsUseCase()
                } catch (e: Exception) {
                    dispatch(Msg.Error(e.message ?: "Unknown error"))
                    publish(BudgetStore.Label.ShowError(e.message ?: "Failed to refresh budgets"))
                }
            }
        }

        private fun retry() {
            refresh()
        }

        private fun updateBudget(categoryId: String, budgetAmount: Double) {
            scope.launch {
                try {
                    updateCategoryBudgetUseCase(categoryId, budgetAmount)
                    dispatch(Msg.DismissDialog)
                    publish(BudgetStore.Label.ShowSuccess("Budget updated successfully"))
                } catch (e: Exception) {
                    publish(BudgetStore.Label.ShowError(e.message ?: "Failed to update budget"))
                }
            }
        }

        private fun createBudget(categoryId: String, categoryName: String, budgetAmount: Double) {
            scope.launch {
                try {
                    createCategoryBudgetUseCase(categoryId, categoryName, budgetAmount)
                    dispatch(Msg.DismissDialog)
                    publish(BudgetStore.Label.ShowSuccess("Budget created successfully"))
                } catch (e: Exception) {
                    publish(BudgetStore.Label.ShowError(e.message ?: "Failed to create budget"))
                }
            }
        }

        private fun deleteBudget(categoryId: String) {
            scope.launch {
                try {
                    deleteCategoryBudgetUseCase(categoryId)
                    publish(BudgetStore.Label.ShowSuccess("Budget deleted successfully"))
                } catch (e: Exception) {
                    publish(BudgetStore.Label.ShowError(e.message ?: "Failed to delete budget"))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<BudgetStore.State, Msg> {
        override fun BudgetStore.State.reduce(msg: Msg): BudgetStore.State =
            when (msg) {
                is Msg.Loading -> copy(isLoading = true, error = null)
                is Msg.DataLoaded -> copy(
                    isLoading = false,
                    budgetSummary = msg.summary,
                    error = null
                )
                is Msg.Error -> copy(isLoading = false, error = msg.message)
                is Msg.ShowEditDialog -> copy(
                    showEditDialog = true,
                    showAddDialog = false,
                    editingCategoryId = msg.categoryId
                )
                is Msg.ShowAddDialog -> copy(
                    showAddDialog = true,
                    showEditDialog = false,
                    editingCategoryId = null
                )
                is Msg.DismissDialog -> copy(
                    showEditDialog = false,
                    showAddDialog = false,
                    editingCategoryId = null
                )
            }
    }
}
