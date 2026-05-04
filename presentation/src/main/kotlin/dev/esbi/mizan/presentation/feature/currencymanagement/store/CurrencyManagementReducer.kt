package dev.esbi.mizan.presentation.feature.currencymanagement.store

import com.arkivanov.mvikotlin.core.store.Reducer
import dev.esbi.mizan.presentation.feature.currencymanagement.store.CurrencyManagementStore.Message
import dev.esbi.mizan.presentation.feature.currencymanagement.store.CurrencyManagementStore.State

internal class CurrencyManagementReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State =
        when (msg) {
            is Message.SubCurrenciesLoaded -> copy(subCurrencies = msg.configs)
            is Message.MainCurrencyLoaded -> copy(mainCurrency = msg.config)
            is Message.CurrencySelected -> copy(selectedCurrencyCode = msg.code)
            is Message.CurrenciesReordered -> copy(subCurrencies = msg.currencies)
            is Message.Loading -> copy(isLoading = msg.isLoading)
            is Message.Syncing -> copy(isSyncing = msg.isSyncing)
            is Message.Error -> copy(error = msg.message)
            is Message.CurrencySettingsUpdated -> {
                val updated = subCurrencies.map { config ->
                    if (config.code == msg.code) {
                        config.copy(
                            exchangeRate = msg.exchangeRate,
                            unitPosition = msg.unitPosition,
                            decimalDigits = msg.decimalDigits
                        )
                    } else config
                }
                copy(subCurrencies = updated)
            }
        }
}
