package dev.esbi.mizan.feature.statistics.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.feature.statistics.domain.model.StatisticsSummary
import dev.esbi.mizan.feature.statistics.domain.model.TimePeriod
import dev.esbi.mizan.feature.statistics.domain.usecase.ObserveStatisticsUseCase
import dev.esbi.mizan.feature.statistics.domain.usecase.RefreshStatisticsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Factory for creating StatisticsStore instances
 * Implements period selection and data refresh logic
 */
class StatisticsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val observeStatisticsUseCase: ObserveStatisticsUseCase,
    private val refreshStatisticsUseCase: RefreshStatisticsUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) {

    fun create(): StatisticsStore =
        object : StatisticsStore, Store<StatisticsStore.Intent, StatisticsStore.State, StatisticsStore.Label> by storeFactory.create(
            name = "StatisticsStore",
            initialState = StatisticsStore.State(isLoading = true),
            bootstrapper = SimpleBootstrapper(StatisticsStore.Action.Init),
            executorFactory = {
                ExecutorImpl(
                    observeStatisticsUseCase,
                    refreshStatisticsUseCase,
                    mainDispatcher
                )
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class DataLoaded(val summary: StatisticsSummary) : Msg
        data class Error(val message: String) : Msg
        data class PeriodChanged(val period: TimePeriod) : Msg
    }

    private class ExecutorImpl(
        private val observeStatisticsUseCase: ObserveStatisticsUseCase,
        private val refreshStatisticsUseCase: RefreshStatisticsUseCase,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher
    ) : CoroutineExecutor<StatisticsStore.Intent, StatisticsStore.Action, StatisticsStore.State, Msg, StatisticsStore.Label>(
        mainContext = mainDispatcher
    ) {

        override fun executeAction(action: StatisticsStore.Action) {
            when (action) {
                StatisticsStore.Action.Init -> {
                    observeStatistics()
                    refresh()
                }
            }
        }

        override fun executeIntent(intent: StatisticsStore.Intent) {
            when (intent) {
                is StatisticsStore.Intent.Refresh -> refresh()
                is StatisticsStore.Intent.Retry -> retry()
                is StatisticsStore.Intent.SelectPeriod -> {
                    dispatch(Msg.PeriodChanged(intent.period))
                    observeStatistics()
                }
            }
        }

        private fun observeStatistics() {
            val currentState = state()
            observeStatisticsUseCase(currentState.selectedPeriod)
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
                    refreshStatisticsUseCase()
                } catch (e: Exception) {
                    dispatch(Msg.Error(e.message ?: "Unknown error"))
                    publish(StatisticsStore.Label.ShowError(e.message ?: "Failed to refresh"))
                }
            }
        }

        private fun retry() {
            observeStatistics()
            refresh()
        }
    }

    private object ReducerImpl : Reducer<StatisticsStore.State, Msg> {
        override fun StatisticsStore.State.reduce(msg: Msg): StatisticsStore.State =
            when (msg) {
                is Msg.Loading -> copy(isLoading = true, error = null)
                is Msg.DataLoaded -> copy(
                    isLoading = false,
                    summary = msg.summary,
                    error = null
                )
                is Msg.Error -> copy(isLoading = false, error = msg.message)
                is Msg.PeriodChanged -> copy(selectedPeriod = msg.period)
            }
    }
}
