package dev.esbi.mizan.presentation.feature.addaccount.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.AccountGroup
import dev.esbi.mizan.domain.model.Currency

interface AddAccountStore : Store<AddAccountStore.Intent, AddAccountStore.State, AddAccountStore.Label> {

    sealed interface Intent {
        data class UpdateName(val name: String) : Intent
        data class UpdateBalance(val balance: String) : Intent
        data class SelectCurrency(val currency: Currency) : Intent
        data class SelectGroup(val groupId: Long) : Intent
        data class UpdateDescription(val description: String) : Intent
        data object SaveAccount : Intent
    }

    data class State(
        val name: String = "",
        val balance: String = "",
        val selectedCurrency: Currency? = Currency.UZS, // Defaulting to UZS 
        val availableCurrencies: List<Currency> = listOf(
            Currency.UZS,
            Currency(code = "USD", name = "US Dollar", symbol = "$", rateToBase = 12500.0, isBaseCurrency = false),
            Currency(code = "EUR", name = "Euro", symbol = "€", rateToBase = 13500.0, isBaseCurrency = false),
            Currency(code = "RUB", name = "Russian Ruble", symbol = "₽", rateToBase = 130.0, isBaseCurrency = false)
        ),
        val selectedGroupId: Long = 1L, // Default to "General" group
        val availableGroups: List<AccountGroup> = emptyList(),
        val description: String = "",
        val validationErrors: Map<Field, String?> = emptyMap(),
        val isLoading: Boolean = false
    ) {
        enum class Field {
            NAME, CURRENCY, GROUP
        }
    }

    sealed interface Label {
        data object AccountSaved : Label
        data class ShowMessage(val message: String) : Label
    }

    sealed interface Message {
        data class NameChanged(val name: String) : Message
        data class BalanceChanged(val balance: String) : Message
        data class CurrencySelected(val currency: Currency) : Message
        data class GroupSelected(val groupId: Long) : Message
        data class DescriptionChanged(val description: String) : Message
        data class ValidationFailed(val errors: Map<State.Field, String?>) : Message
        data class Loading(val isLoading: Boolean) : Message
        data class GroupsLoaded(val groups: List<AccountGroup>) : Message
    }
}
