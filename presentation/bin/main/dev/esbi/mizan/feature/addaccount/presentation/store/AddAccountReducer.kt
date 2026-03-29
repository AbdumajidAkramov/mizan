package dev.esbi.mizan.feature.addaccount.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import dev.esbi.mizan.feature.addaccount.presentation.store.AddAccountStore.State

internal class AddAccountReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State =
        when (msg) {
            is Message.NameChanged -> copy(name = msg.name, validationErrors = validationErrors - State.Field.NAME)
            is Message.BalanceChanged -> copy(balance = msg.balance)
            is Message.CurrencySelected -> copy(selectedCurrency = msg.currency, validationErrors = validationErrors - State.Field.CURRENCY)
            is Message.DescriptionChanged -> copy(description = msg.description)
            is Message.ValidationFailed -> copy(validationErrors = msg.errors)
            is Message.Loading -> copy(isLoading = msg.isLoading)
        }
}
