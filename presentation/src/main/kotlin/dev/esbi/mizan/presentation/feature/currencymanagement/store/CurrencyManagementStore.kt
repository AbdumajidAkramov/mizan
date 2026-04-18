package dev.esbi.mizan.presentation.feature.currencymanagement.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.UnitPosition
import java.math.BigDecimal

interface CurrencyManagementStore : Store<
        CurrencyManagementStore.Intent,
        CurrencyManagementStore.State,
        CurrencyManagementStore.Label> {

    sealed interface Intent {
        data class AddCurrency(val config: Currency) : Intent
        data class RemoveCurrency(val code: String) : Intent
        data class Reorder(val configs: List<Currency>) : Intent
        data class UpdateSettings(
            val code: String,
            val exchangeRate: BigDecimal,
            val unitPosition: UnitPosition,
            val decimalDigits: Int
        ) : Intent
        data class CreateCustomCurrency(
            val name: String,
            val unit: String,
            val rate: BigDecimal,
            val position: UnitPosition,
            val decimals: Int
        ) : Intent
        data object SyncRates : Intent
        data class SelectCurrency(val code: String) : Intent
    }

    data class State(
        val subCurrencies: List<Currency> = emptyList(),
        val mainCurrency: Currency? = null,
        val selectedCurrencyCode: String? = null,
        val isLoading: Boolean = false,
        val isSyncing: Boolean = false,
        val error: String? = null
    )

    sealed interface Action {
        data object Init : Action
        data object SyncRates : Action
    }

    sealed interface Label {
        data class ShowMessage(val message: String) : Label
        data object CurrencyAdded : Label
        data object CurrencyRemoved : Label
        data class SyncCompleted(val success: Boolean) : Label
    }

    sealed interface Message {
        data class SubCurrenciesLoaded(val configs: List<Currency>) : Message
        data class MainCurrencyLoaded(val config: Currency?) : Message
        data class CurrencySelected(val code: String?) : Message
        data class Loading(val isLoading: Boolean) : Message
        data class Syncing(val isSyncing: Boolean) : Message
        data class Error(val message: String?) : Message
        data class CurrencySettingsUpdated(
            val code: String,
            val exchangeRate: BigDecimal,
            val unitPosition: UnitPosition,
            val decimalDigits: Int
        ) : Message
    }
}
