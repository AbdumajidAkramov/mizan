package dev.esbi.mizan.presentation.feature.addaccount.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.presentation.feature.addaccount.store.AddAccountStore.Intent
import dev.esbi.mizan.presentation.feature.addaccount.store.AddAccountStore.Label
import dev.esbi.mizan.presentation.feature.addaccount.store.AddAccountStore.Message
import dev.esbi.mizan.presentation.feature.addaccount.store.AddAccountStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.BigDecimal

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

internal class AddAccountExecutor(
    mainDispatcher: CoroutineDispatcher,
    private val accountRepository: AccountRepository,
    private val currencyRepository: CurrencyRepository
) : CoroutineExecutor<Intent, AddAccountStore.Action, State, Message, Label>(mainContext = mainDispatcher) {

    override fun executeAction(action: AddAccountStore.Action) {
        when (action) {
            is AddAccountStore.Action.FetchAccount -> {
                action.accountId?.let { accountId ->
                    fetchAccount(accountId)
                }
            }

            is AddAccountStore.Action.FetchCurrencies -> {
                fetchCurrencies()
            }

            is AddAccountStore.Action.FetchAccountGroups -> {
                loadGroups()
            }
        }
    }

    private fun fetchAccount(accountId: Long) {
        scope.launch {
            accountRepository.getAccount(accountId)?.let { account ->
                dispatch(Message.SetSelectAccount(account))
            }
        }
    }

    private fun fetchCurrencies() {
        scope.launch {
            currencyRepository.observeCurrencies()
                .collectLatest { currencies ->
                    dispatch(Message.UpdateAvailableCurrencies(currencies))
                }
        }
    }

    private fun loadGroups() {
        accountRepository.observeAccountGroups()
            .onEach { groups ->
                dispatch(Message.GroupsLoaded(groups))
            }
            .launchIn(scope)
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.UpdateName -> dispatch(Message.NameChanged(intent.name))
            is Intent.UpdateBalance -> dispatch(Message.BalanceChanged(intent.balance))
            is Intent.SelectCurrency -> dispatch(Message.CurrencySelected(intent.currency))
            is Intent.SelectGroup -> dispatch(Message.GroupSelected(intent.groupId))
            is Intent.UpdateDescription -> dispatch(Message.DescriptionChanged(intent.description))
            is Intent.SaveAccount -> validateAndSave()
            is Intent.ConfirmDeleteAccount -> {
                deleteAccount(accountId = intent.accountId)
            }

            is Intent.UpdateIncludeInTotals -> {
                dispatch(Message.IncludeInTotalsChanged(intent.value))
            }

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

        if (currentState.availableGroups.none { it.id == currentState.selectedGroupId }) {
            errors[State.Field.GROUP] = "Please select a valid group"
        }

        if (errors.isNotEmpty()) {
            dispatch(Message.ValidationFailed(errors))
            return
        }

        val parsedBalance = currentState.balance.toBigDecimal()

        val newAccount = Account(
            id = currentState.accountId ?: 0L,
            groupId = currentState.selectedGroupId,
            name = currentState.name.trim(),
            balance = parsedBalance,
            currency = currentState.selectedCurrency!!,
            isArchived = false,
            excludeFromTotal = currentState.excludeFromTotal,
            description = currentState.description.takeIf { it.isNotBlank() }
        )

        scope.launch {
            dispatch(Message.Loading(true))
            try {
                if (currentState.accountId != null) {
                    accountRepository.updateAccount(newAccount)
                } else {
                    accountRepository.createAccount(newAccount)
                }
                dispatch(Message.Loading(false))
                publish(Label.AccountSaved)
            } catch (e: Exception) {
                dispatch(Message.Loading(false))
                publish(Label.ShowMessage(e.message ?: "Failed to save account"))
            }
        }
    }

    private fun deleteAccount(accountId: Long) {
        scope.launch {
            try {
                accountRepository.markAccountAsDeleted(accountId)
                publish(Label.AccountDeleted())
            } catch (e: Exception) {
                publish(Label.ShowMessage("Failed to delete account: ${e.message}"))
            }
        }
    }
}

fun formatForEditing(amount: BigDecimal): String {
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        decimalSeparator = '.' // Har doim nuqta ishlatish uchun
        groupingSeparator = ' ' // Mingliklarni ajratmaslik (tahrirlashda oson bo'lishi uchun)
    }

    // "0.##" -> Butun qismini ko'rsat, nuqtadan keyin 2 tagacha ixtiyoriy son
    val df = DecimalFormat("0.##", symbols)
    df.isGroupingUsed = false // 15 000 000 emas, 15000000 ko'rinishida chiqadi

    return df.format(amount)
}
fun BigDecimal.toEditString(): String {
    return this.stripTrailingZeros().toPlainString()
}