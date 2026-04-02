package dev.esbi.mizan.presentation.feature.currencymanagement.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.presentation.feature.currencymanagement.store.CurrencyManagementStore.Intent
import dev.esbi.mizan.presentation.feature.currencymanagement.store.CurrencyManagementStore.Label
import dev.esbi.mizan.presentation.feature.currencymanagement.store.CurrencyManagementStore.Message
import dev.esbi.mizan.presentation.feature.currencymanagement.store.CurrencyManagementStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class CurrencyManagementExecutor(
    mainDispatcher: CoroutineDispatcher,
    private val currencyRepository: CurrencyRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>(mainContext = mainDispatcher) {

    override fun executeAction(action: Unit) {
        loadSubCurrencies()
    }

    private fun loadSubCurrencies() {
        currencyRepository.observeSubCurrencies()
            .onEach { configs ->
                val main = configs.find { it.isMainCurrency }
                val subs = configs
                dispatch(Message.SubCurrenciesLoaded(subs))
                dispatch(Message.MainCurrencyLoaded(main))
            }
            .launchIn(scope)
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.AddCurrency -> addCurrency(intent.config)
            is Intent.RemoveCurrency -> removeCurrency(intent.code)
            is Intent.Reorder -> reorder(intent.configs)
            is Intent.UpdateSettings -> updateSettings(intent)
            is Intent.SyncRates -> syncRates()
            is Intent.SelectCurrency -> dispatch(Message.CurrencySelected(intent.code))
        }
    }

    private fun addCurrency(config: dev.esbi.mizan.domain.model.CurrencyConfig) {
        scope.launch {
            dispatch(Message.Loading(true))
            try {
                val nextOrder = state().subCurrencies.maxOfOrNull { it.orderIndex }?.plus(1) ?: 0
                currencyRepository.saveSubCurrency(config.copy(orderIndex = nextOrder))
                dispatch(Message.Loading(false))
                publish(Label.CurrencyAdded)
            } catch (e: Exception) {
                dispatch(Message.Loading(false))
                dispatch(Message.Error(e.message))
                publish(Label.ShowMessage(e.message ?: "Failed to add currency"))
            }
        }
    }

    private fun removeCurrency(code: String) {
        val currency = state().subCurrencies.find { it.code == code }
        if (currency?.isMainCurrency == true) {
            publish(Label.ShowMessage("Cannot remove the main currency"))
            return
        }
        scope.launch {
            dispatch(Message.Loading(true))
            try {
                currencyRepository.deleteSubCurrency(code)
                dispatch(Message.Loading(false))
                publish(Label.CurrencyRemoved)
            } catch (e: Exception) {
                dispatch(Message.Loading(false))
                publish(Label.ShowMessage(e.message ?: "Failed to remove currency"))
            }
        }
    }

    private fun reorder(configs: List<dev.esbi.mizan.domain.model.CurrencyConfig>) {
        scope.launch {
            try {
                currencyRepository.updateSubCurrencyOrder(configs)
            } catch (e: Exception) {
                publish(Label.ShowMessage(e.message ?: "Failed to reorder"))
            }
        }
    }

    private fun updateSettings(intent: Intent.UpdateSettings) {
        scope.launch {
            dispatch(Message.Loading(true))
            try {
                currencyRepository.updateSubCurrencySettings(
                    code = intent.code,
                    exchangeRate = intent.exchangeRate,
                    unitPosition = intent.unitPosition,
                    decimalDigits = intent.decimalDigits
                )
                dispatch(Message.CurrencySettingsUpdated(
                    code = intent.code,
                    exchangeRate = intent.exchangeRate,
                    unitPosition = intent.unitPosition,
                    decimalDigits = intent.decimalDigits
                ))
                dispatch(Message.Loading(false))
                publish(Label.ShowMessage("Settings updated"))
            } catch (e: Exception) {
                dispatch(Message.Loading(false))
                publish(Label.ShowMessage(e.message ?: "Failed to update settings"))
            }
        }
    }

    private fun syncRates() {
        scope.launch {
            dispatch(Message.Syncing(true))
            try {
                val result = currencyRepository.syncExchangeRates()
                dispatch(Message.Syncing(false))
                publish(Label.SyncCompleted(result.isSuccess))
                if (result.isFailure) {
                    publish(Label.ShowMessage("Sync failed: ${result.exceptionOrNull()?.message}"))
                }
            } catch (e: Exception) {
                dispatch(Message.Syncing(false))
                publish(Label.SyncCompleted(false))
                publish(Label.ShowMessage(e.message ?: "Sync failed"))
            }
        }
    }
}
