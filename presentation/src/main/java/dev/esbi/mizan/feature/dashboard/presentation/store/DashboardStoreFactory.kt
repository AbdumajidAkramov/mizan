package dev.esbi.mizan.feature.dashboard.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.feature.dashboard.domain.model.DashboardSummary
import dev.esbi.mizan.feature.dashboard.domain.usecase.ObserveDashboardSummaryUseCase
import dev.esbi.mizan.feature.dashboard.domain.usecase.RefreshDashboardUseCase
import dev.esbi.mizan.feature.dashboard.presentation.store.DashboardStore.Intent
import dev.esbi.mizan.feature.dashboard.presentation.store.DashboardStore.Label
import dev.esbi.mizan.feature.dashboard.presentation.store.DashboardStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val observeDashboardSummaryUseCase: ObserveDashboardSummaryUseCase,
    private val refreshDashboardUseCase: RefreshDashboardUseCase,
    private val mainDispatcher: CoroutineDispatcher
) {

    fun create(): DashboardStore =
        object : DashboardStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DashboardStore",
            initialState = State(isLoading = true),
            bootstrapper = SimpleBootstrapper(DashboardStore.Action.Init),
            executorFactory = {
                ExecutorImpl(
                    observeDashboardSummaryUseCase,
                    refreshDashboardUseCase,
                    mainDispatcher
                )
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data class DashboardLoaded(val data: DashboardSummary?) : Msg
        data object Loading : Msg
        data class Error(val message: String) : Msg
    }

    private class ExecutorImpl(
        private val observeDashboardSummaryUseCase: ObserveDashboardSummaryUseCase,
        private val refreshDashboardUseCase: RefreshDashboardUseCase,
        mainDispatcher: CoroutineDispatcher
    ) : CoroutineExecutor<Intent, DashboardStore.Action, State, Msg, Label>(
        mainContext = mainDispatcher
    ) {
        override fun executeAction(action: DashboardStore.Action) {
            when (action) {
                DashboardStore.Action.Init -> {
                    observeDashboard()
                    refresh()
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.Refresh -> refresh()
                is Intent.Retry -> retry()
                is Intent.CategoryClicked -> publish(Label.NavigateToCategory(intent.categoryId))
                is Intent.AddTransactionClicked -> publish(Label.NavigateToNewTransaction)
                is Intent.ViewAllTransactionsClicked -> publish(Label.NavigateToTransactionsHub)
                is Intent.ProfileClicked -> publish(Label.NavigateToProfile)
            }
        }

        private fun observeDashboard() {
            observeDashboardSummaryUseCase()
                .onEach { data ->
                    dispatch(Msg.DashboardLoaded(data))
                }
                .catch { error ->
                    dispatch(Msg.Error(error.message ?: "Unknown error"))
                }
                .launchIn(scope)
        }

        private fun refresh() {
            dispatch(Msg.Loading)
            scope.launch {
                try {
                    refreshDashboardUseCase()
                } catch (e: Exception) {
                    dispatch(Msg.Error(e.message ?: "Failed to refresh dashboard"))
                    publish(Label.ShowError(e.message ?: "Failed to refresh dashboard"))
                }
            }
        }

        private fun retry() {
            refresh()
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.DashboardLoaded -> copy(
                    dashboardData = msg.data,
                    isLoading = false,
                    error = null
                )

                is Msg.Loading -> copy(isLoading = true, error = null)
                is Msg.Error -> copy(isLoading = false, error = msg.message)
            }
    }
}
