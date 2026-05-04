package dev.esbi.mizan.presentation.feature.currencymanagement.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.presentation.feature.currencymanagement.store.CurrencyManagementStore.Action
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
) : CoroutineExecutor<Intent, Action, State, Message, Label>(mainContext = mainDispatcher) {

    override fun executeAction(action: Action) {
        when (action) {
            Action.Init -> loadSubCurrencies()
            Action.SyncRates -> Unit
        }
    }

    private fun loadSubCurrencies() {
        currencyRepository.observeAllCurrencies()
            .onEach { currencies ->
                val main = currencies.find { it.isMainCurrency }
                val secondary = currencies.filter { !it.isMainCurrency }
                dispatch(Message.SubCurrenciesLoaded(secondary))
                dispatch(Message.MainCurrencyLoaded(main))
            }
            .launchIn(scope)
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.AddCurrency -> addCurrency(intent.config)
            is Intent.RemoveCurrency -> removeCurrency(intent.code)
            is Intent.Reorder -> reorder(intent.configs)
            is Intent.ReorderCurrencies -> reorderCurrencies(intent.fromIndex, intent.toIndex)
            is Intent.SaveCurrencyOrder -> saveCurrencyOrder(intent.currencies)
            is Intent.UpdateSettings -> updateSettings(intent)
            is Intent.CreateCustomCurrency -> createCustomCurrency(intent)
            is Intent.SyncRates -> syncRates()
            is Intent.SelectCurrency -> dispatch(Message.CurrencySelected(intent.code))
        }
    }
    
    private fun reorderCurrencies(fromIndex: Int, toIndex: Int) {
        val currentList = state().subCurrencies.filter { !it.isMainCurrency }.toMutableList()
        
        if (fromIndex in currentList.indices && toIndex in currentList.indices) {
            val item = currentList.removeAt(fromIndex)
            currentList.add(toIndex, item)
            
            // Update orderIndex to match new positions
            val reindexedList = currentList.mapIndexed { index, currency ->
                currency.copy(orderIndex = index)
            }
            
            // Optimistic UI update with correct orderIndex
            dispatch(Message.CurrenciesReordered(reindexedList))
        }
    }
    
    private fun saveCurrencyOrder(currencies: List<dev.esbi.mizan.domain.model.Currency>) {
        scope.launch {
            try {
                // CRITICAL FIX: Re-index currencies to match their new physical positions
                val updatedCurrencies = currencies.mapIndexed { index, currency ->
                    currency.copy(orderIndex = index)
                }
                currencyRepository.updateCurrencyOrder(updatedCurrencies)
            } catch (e: Exception) {
                publish(Label.ShowMessage(e.message ?: "Failed to save currency order"))
            }
        }
    }

    private fun addCurrency(config: dev.esbi.mizan.domain.model.Currency) {
        scope.launch {
            dispatch(Message.Loading(true))
            try {
                val nextOrder = state().subCurrencies.maxOfOrNull { it.orderIndex }?.plus(1) ?: 0
                currencyRepository.saveCurrency(config.copy(orderIndex = nextOrder, isSecondary = true))
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
                currencyRepository.deleteCurrency(code)
                dispatch(Message.Loading(false))
                publish(Label.CurrencyRemoved)
            } catch (e: Exception) {
                dispatch(Message.Loading(false))
                publish(Label.ShowMessage(e.message ?: "Failed to remove currency"))
            }
        }
    }

    private fun reorder(configs: List<dev.esbi.mizan.domain.model.Currency>) {
        scope.launch {
            try {
                currencyRepository.updateCurrencyOrder(configs)
            } catch (e: Exception) {
                publish(Label.ShowMessage(e.message ?: "Failed to reorder"))
            }
        }
    }

    private fun updateSettings(intent: Intent.UpdateSettings) {
        scope.launch {
            dispatch(Message.Loading(true))
            try {
                currencyRepository.updateCurrencySettings(
                    code = intent.code,
                    exchangeRate = intent.exchangeRate,
                    unitPosition = intent.unitPosition,
                    decimalDigits = intent.decimalDigits
                )
                dispatch(
                    Message.CurrencySettingsUpdated(
                        code = intent.code,
                        exchangeRate = intent.exchangeRate,
                        unitPosition = intent.unitPosition,
                        decimalDigits = intent.decimalDigits
                    )
                )
                dispatch(Message.Loading(false))
                publish(Label.ShowMessage("Settings updated"))
            } catch (e: Exception) {
                dispatch(Message.Loading(false))
                publish(Label.ShowMessage(e.message ?: "Failed to update settings"))
            }
        }
    }

    private fun createCustomCurrency(intent: Intent.CreateCustomCurrency) {
        scope.launch {
            dispatch(Message.Loading(true))
            try {
                // Validate unit (code) length: 3-5 characters
                val unit = intent.unit.trim().uppercase()
                if (unit.length !in 3..5) {
                    dispatch(Message.Loading(false))
                    publish(Label.ShowMessage("Unit/Symbol must be 3-5 characters"))
                    return@launch
                }

                // Validate name is not empty
                if (intent.name.isBlank()) {
                    dispatch(Message.Loading(false))
                    publish(Label.ShowMessage("Name cannot be empty"))
                    return@launch
                }

                // Validate exchange rate is positive
                if (intent.rate <= java.math.BigDecimal.ZERO) {
                    dispatch(Message.Loading(false))
                    publish(Label.ShowMessage("Exchange rate must be positive"))
                    return@launch
                }

                // Check if code is unique
                val isUnique = currencyRepository.isCurrencyCodeUnique(unit)
                if (!isUnique) {
                    dispatch(Message.Loading(false))
                    publish(Label.ShowMessage("Currency code '$unit' already exists"))
                    return@launch
                }

                // Create the custom currency
                val nextOrder = state().subCurrencies.maxOfOrNull { it.orderIndex }?.plus(1) ?: 0
                val config = dev.esbi.mizan.domain.model.Currency(
                    code = unit,
                    name = intent.name.trim(),
                    symbol = unit, // Use unit as symbol for custom currencies
                    exchangeRate = intent.rate,
                    unitPosition = intent.position,
                    decimalDigits = intent.decimals,
                    orderIndex = nextOrder,
                    isMainCurrency = false,
                    isUserDefined = true
                )

                currencyRepository.saveCurrency(config)
                dispatch(Message.Loading(false))
                publish(Label.CurrencyAdded)
                publish(Label.ShowMessage("Custom currency '${intent.name}' created"))
            } catch (e: Exception) {
                dispatch(Message.Loading(false))
                publish(Label.ShowMessage(e.message ?: "Failed to create custom currency"))
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
