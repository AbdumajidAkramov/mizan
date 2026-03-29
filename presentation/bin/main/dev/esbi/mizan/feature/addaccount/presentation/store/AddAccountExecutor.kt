package dev.esbi.mizan.feature.addaccount.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.feature.addaccount.presentation.store.AddAccountStore.Intent
import dev.esbi.mizan.feature.addaccount.presentation.store.AddAccountStore.Label
import dev.esbi.mizan.feature.addaccount.presentation.store.AddAccountStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class AddAccountExecutor(
    mainDispatcher: CoroutineDispatcher,
    private val accountRepository: AccountRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>(mainContext = mainDispatcher) {

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.UpdateName -> dispatch(Message.NameChanged(intent.name))
            is Intent.UpdateBalance -> dispatch(Message.BalanceChanged(intent.balance))
            is Intent.SelectCurrency -> dispatch(Message.CurrencySelected(intent.currency))
            is Intent.UpdateDescription -> dispatch(Message.DescriptionChanged(intent.description))
            is Intent.SaveAccount -> validateAndSave()
        }
    }

    private fun validateAndSave() {
        val currentState = state()
        val errors = mutableMapOf<State.Field, String?>()

        if (currentState.name.isBlank()) {
            errors[State.Field.NAME] = "Account name cannot be empty"
        }

        if (currentState.selectedCurrency == null) {
            errors[State.Field.CURRENCY] = "Currency must be selected"
        }

        if (errors.isNotEmpty()) {
            dispatch(Message.ValidationFailed(errors))
            return
        }

        val parsedBalance = currentState.balance.toDoubleOrNull() ?: 0.0

        val newAccount = Account(
            id = 0L,
            groupId = 1L, // Default to General Accounts
            name = currentState.name.trim(),
            type = Account.Type.BANK, 
            balance = parsedBalance,
            currency = currentState.selectedCurrency!!,
            iconName = "ic_accounts",
            isArchived = false,
            excludeFromTotal = false,
            description = currentState.description.takeIf { it.isNotBlank() }
        )

        scope.launch {
            dispatch(Message.Loading(true))
            try {
                accountRepository.createAccount(newAccount)
                dispatch(Message.Loading(false))
                publish(Label.AccountSaved)
            } catch (e: Exception) {
                dispatch(Message.Loading(false))
                publish(Label.ShowMessage(e.message ?: "Failed to save account"))
            }
        }
    }
}
