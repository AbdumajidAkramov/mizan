package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Action
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Label
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.Message
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class AddNewTransactionCurrencyExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val currencyRepository: CurrencyRepository,
) : CoroutineExecutor<Intent, Action, State, Message, Label>() {

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
        currencyRepository.observeCurrencies()
            .onStart {
                dispatch(Message.UpdateLoading(true))
            }
            .onEach { currencies ->
                dispatch(Message.UpdateCurrencies(currencies))
                dispatch(Message.UpdateLoading(false))

                // Auto-select base currency if available
                if (state().selectedCurrency == null && currencies.isNotEmpty()) {
                    val baseCurrency = currencies.find { it.isBaseCurrency }
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
