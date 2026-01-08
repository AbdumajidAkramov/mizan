package dev.esbi.mizan.feature.financialmirror.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.feature.financialmirror.domain.model.FinancialMirrorSummary
import dev.esbi.mizan.feature.financialmirror.domain.usecase.ObserveFinancialMirrorUseCase
import dev.esbi.mizan.feature.financialmirror.domain.usecase.RefreshFinancialMirrorUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Factory for creating FinancialMirrorStore instances
 * Implements MVIKotlin pattern with Executor-Reducer architecture
 */
class FinancialMirrorStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val observeFinancialMirrorUseCase: ObserveFinancialMirrorUseCase,
    private val refreshFinancialMirrorUseCase: RefreshFinancialMirrorUseCase
) {

    fun create(): FinancialMirrorStore =
        object : FinancialMirrorStore,
            Store<FinancialMirrorStore.Intent, FinancialMirrorStore.State, FinancialMirrorStore.Label> by storeFactory.create(
                name = "FinancialMirrorStore",
                initialState = FinancialMirrorStore.State(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Msg {
        object Loading : Msg()
        data class DataLoaded(val data: FinancialMirrorSummary) : Msg()
        data class Error(val message: String) : Msg()
        data class ProjectionViewChanged(val view: FinancialMirrorStore.ProjectionView) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<FinancialMirrorStore.Intent, FinancialMirrorStore.Action, FinancialMirrorStore.State, Msg, FinancialMirrorStore.Label>() {

        override fun executeAction(action: FinancialMirrorStore.Action) {
            when(action){
                is FinancialMirrorStore.Action.Init -> {
                    observeFinancialMirror()
                    refresh()
                }
            }
        }

        override fun executeIntent(intent: FinancialMirrorStore.Intent) {
            when (intent) {
                is FinancialMirrorStore.Intent.Refresh -> refresh()
                is FinancialMirrorStore.Intent.Retry -> retry()
                is FinancialMirrorStore.Intent.SelectProjectionView -> {
                    dispatch(Msg.ProjectionViewChanged(intent.view))
                }
            }
        }

        private fun observeFinancialMirror() {
            scope.launch {
                observeFinancialMirrorUseCase().collect { data ->
                    dispatch(Msg.DataLoaded(data))
                }
            }
        }

        private fun refresh() {
            dispatch(Msg.Loading)
            scope.launch {
                try {
                    refreshFinancialMirrorUseCase()
                } catch (e: Exception) {
                    dispatch(Msg.Error(e.message ?: "Unknown error"))
                    publish(
                        FinancialMirrorStore.Label.ShowError(
                            e.message ?: "Failed to refresh data"
                        )
                    )
                }
            }
        }

        private fun retry() {
            refresh()
        }
    }

    private object ReducerImpl : Reducer<FinancialMirrorStore.State, Msg> {
        override fun FinancialMirrorStore.State.reduce(msg: Msg): FinancialMirrorStore.State =
            when (msg) {
                is Msg.Loading -> copy(isLoading = true, error = null)
                is Msg.DataLoaded -> copy(
                    isLoading = false,
                    financialMirrorData = msg.data,
                    error = null
                )

                is Msg.Error -> copy(isLoading = false, error = msg.message)
                is Msg.ProjectionViewChanged -> copy(selectedProjectionView = msg.view)
            }
    }
}
