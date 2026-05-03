package dev.esbi.mizan.presentation.feature.addtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Action
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Label
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Message
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class AddNewTransactionCurrencyExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val currencyRepository: CurrencyRepository,
) : CoroutineExecutor<Intent, Action, State, Message, Label>(mainDispatcher) {

    override fun executeAction(action: Action) {
        when (action) {
            is Action.InitCurrencies -> {
                fetchCurrencies()
            }

            else -> Unit
        }
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            else -> Unit
        }
    }

    private fun fetchCurrencies() {
        combine(
            currencyRepository.observeMainCurrency(),
            currencyRepository.observeSubCurrencies()
        ) { mainCurrency, subCurrencies ->
            listOf(mainCurrency) + subCurrencies
        }
            .onStart {
                dispatch(Message.UpdateLoading(true))
            }
            .onEach { currencies ->
                val filteredList = currencies.filterNotNull()
                dispatch(Message.UpdateCurrencies(filteredList))
                dispatch(Message.UpdateLoading(false))

                // Auto-select base currency if available
                if (state().selectedCurrency == null && filteredList.isNotEmpty()) {
                    val baseCurrency = filteredList.find { it.isMainCurrency }
                    baseCurrency?.let {
                        dispatch(Message.UpdateCurrency(it))
                    }
                }
            }
            .catch { error ->
                dispatch(Message.UpdateLoading(false))
                dispatch(Message.UpdateError("Failed to load currencies: ${error.message}"))
            }
            .launchIn(scope)
    }
}
