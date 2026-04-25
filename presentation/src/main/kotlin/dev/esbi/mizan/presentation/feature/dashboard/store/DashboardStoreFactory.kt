package dev.esbi.mizan.presentation.feature.dashboard.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.dashboard.DashboardSummary
import dev.esbi.mizan.domain.usecase.GetDashboardSummaryUseCase
import dev.esbi.mizan.domain.usecase.dashboard.ObserveDashboardSummaryUseCase
import dev.esbi.mizan.domain.usecase.dashboard.RefreshDashboardUseCase
import dev.esbi.mizan.presentation.feature.dashboard.store.DashboardStore.Intent
import dev.esbi.mizan.presentation.feature.dashboard.store.DashboardStore.Label
import dev.esbi.mizan.presentation.feature.dashboard.store.DashboardStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

class DashboardStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val observeDashboardSummaryUseCase: ObserveDashboardSummaryUseCase,
    private val refreshDashboardUseCase: RefreshDashboardUseCase,
    private val getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
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
                    getDashboardSummaryUseCase,
                    mainDispatcher
                )
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data class DashboardLoaded(val data: DashboardSummary?) : Msg
        data class TotalUpdated(
            val total: BigDecimal,
            val mainCurrency: Currency
        ) : Msg
        data object Loading : Msg
        data class Error(val message: String) : Msg
    }

    private class ExecutorImpl(
        private val observeDashboardSummaryUseCase: ObserveDashboardSummaryUseCase,
        private val refreshDashboardUseCase: RefreshDashboardUseCase,
        private val getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
        mainDispatcher: CoroutineDispatcher
    ) : CoroutineExecutor<Intent, DashboardStore.Action, State, Msg, Label>(
        mainContext = mainDispatcher
    ) {
        override fun executeAction(action: DashboardStore.Action) {
            when (action) {
                DashboardStore.Action.Init -> {
                    observeDashboard()
                    observeGrandTotal()
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

        private fun observeGrandTotal() {
            getDashboardSummaryUseCase()
                .onEach { summary ->
                    dispatch(
                        Msg.TotalUpdated(
                            total = summary.totalBalance,
                            mainCurrency = summary.mainCurrency
                        )
                    )
                }
                .catch { error ->
                    dispatch(Msg.Error(error.message ?: "Failed to compute total"))
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

                is Msg.TotalUpdated -> copy(
                    totalBalance = msg.total,
                    mainCurrency = msg.mainCurrency,
                    isLoading = false,
                    error = null
                )

                is Msg.Loading -> copy(isLoading = true, error = null)
                is Msg.Error -> copy(isLoading = false, error = msg.message)
            }
    }
}
