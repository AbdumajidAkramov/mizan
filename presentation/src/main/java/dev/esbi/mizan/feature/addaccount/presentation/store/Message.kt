package dev.esbi.mizan.feature.addaccount.presentation.store

import dev.esbi.mizan.domain.model.Currency

internal sealed interface Message {
    data class NameChanged(val name: String) : Message
    data class BalanceChanged(val balance: String) : Message
    data class CurrencySelected(val currency: Currency) : Message
    data class DescriptionChanged(val description: String) : Message
    data class ValidationFailed(val errors: Map<AddAccountStore.State.Field, String?>) : Message
    data class Loading(val isLoading: Boolean) : Message
}
