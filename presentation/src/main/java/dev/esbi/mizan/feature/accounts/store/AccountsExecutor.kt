package dev.esbi.mizan.feature.accounts.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.util.CurrencyConverter
import dev.esbi.mizan.feature.accounts.store.AccountsStore.Intent
import dev.esbi.mizan.feature.accounts.store.AccountsStore.Label
import dev.esbi.mizan.feature.accounts.store.AccountsStore.Message
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AccountsExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val accountRepository: AccountRepository,
    private val currencyConverter: CurrencyConverter
) : CoroutineExecutor<Intent, Unit, AccountsStore.State, Message, Label>(
    mainContext = mainDispatcher
) {

    interface Factory {
        fun create(): AccountsExecutor
    }

    override fun executeAction(action: Unit) {
        loadAccounts()
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.InitialLoad -> loadAccounts()
            is Intent.Refresh -> loadAccounts()
            is Intent.SearchQueryChanged -> dispatch(Message.SearchQueryChanged(intent.query))
            is Intent.AddAccount -> publish(Label.NavigateToAddAccount)
            is Intent.AccountClicked -> publish(Label.NavigateToAccountDetail(intent.accountId))
        }
    }

    private fun loadAccounts() {
        dispatch(Message.LoadingChanged(true))

        accountRepository.observeAccounts()
            .onEach { accounts ->
                val activeAccounts = accounts.filter { !it.isArchived }

                val grouped = buildGroupedAccounts(activeAccounts)

                val totalBalance = activeAccounts
                    .filter { !it.excludeFromTotal }
                    .sumOf { currencyConverter.convertToBase(it.balance, it.currency.code) }

                dispatch(
                    Message.AccountsLoaded(
                        groupedAccounts = grouped,
                        totalBalance = totalBalance
                    )
                )
            }
            .catch { e ->
                dispatch(Message.ErrorChanged(e.message ?: "Failed to load accounts"))
                publish(Label.ShowError(e.message ?: "Failed to load accounts"))
            }
            .launchIn(scope)
    }

    private fun buildGroupedAccounts(
        accounts: List<Account>
    ): Map<Account.Type, List<AccountsStore.AccountUIModel>> {
        return accounts
            .groupBy { it.type }
            .mapValues { (_, typeAccounts) ->
                typeAccounts.map { account ->
                    AccountsStore.AccountUIModel(
                        id = account.id,
                        name = account.name,
                        subtitle = account.description,
                        balance = account.balance,
                        currencyCode = account.currency.code,
                        balanceInBase = currencyConverter.convertToBase(
                            account.balance,
                            account.currency.code
                        ),
                        iconName = account.iconName,
                        color = account.color
                    )
                }
            }
            .toSortedMap(compareBy { it.ordinal })
    }
}
