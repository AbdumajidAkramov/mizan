package dev.esbi.mizan.presentation.feature.addaccount.store

import com.arkivanov.mvikotlin.core.store.Reducer
import dev.esbi.mizan.presentation.feature.addaccount.store.AddAccountStore.Message
import dev.esbi.mizan.presentation.feature.addaccount.store.AddAccountStore.State

internal class AddAccountReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State =
        when (msg) {
            is Message.SetSelectAccount -> copy(
                accountId = msg.account.id,
                name = msg.account.name,
                balance = msg.account.balance.toEditString(),
                selectedCurrency = msg.account.currency,
                selectedGroupId = msg.account.groupId,
                description = msg.account.description.orEmpty(),
                isLoading = false,
                excludeFromTotal = msg.account.excludeFromTotal
            )

            is Message.NameChanged -> copy(
                name = msg.name,
                validationErrors = validationErrors - State.Field.NAME
            )

            is Message.BalanceChanged -> copy(balance = msg.balance)
            is Message.CurrencySelected -> copy(
                selectedCurrency = msg.currency,
                validationErrors = validationErrors - State.Field.CURRENCY
            )

            is Message.GroupSelected -> copy(
                selectedGroupId = msg.groupId,
                validationErrors = validationErrors - State.Field.GROUP
            )

            is Message.DescriptionChanged -> copy(description = msg.description)
            is Message.ValidationFailed -> copy(validationErrors = msg.errors)
            is Message.Loading -> copy(isLoading = msg.isLoading)
            is Message.GroupsLoaded -> copy(availableGroups = msg.groups)
            is Message.UpdateAvailableCurrencies -> copy(availableCurrencies = msg.currencies)
            is Message.IncludeInTotalsChanged -> copy(excludeFromTotal = !msg.value)
        }
}
