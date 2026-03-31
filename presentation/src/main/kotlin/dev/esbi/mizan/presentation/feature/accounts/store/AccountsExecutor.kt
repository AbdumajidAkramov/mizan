package dev.esbi.mizan.presentation.feature.accounts.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Executor for AccountManagement - handles business logic and side effects
 */
internal class AccountsExecutor(
    private val mainDispatcher: CoroutineDispatcher,
    private val accountRepository: AccountRepository,
    private val currencyRepository: CurrencyRepository
) : CoroutineExecutor<
        AccountsStore.Intent,
        AccountsStore.Action,
        AccountsStore.State,
        AccountsStore.Message,
        AccountsStore.Label>(
    mainContext = mainDispatcher
) {

    override fun executeAction(action: AccountsStore.Action) {
        when (action) {
            is AccountsStore.Action.FetchMainCurrency -> {
                fetchMainCurrency()
            }
        }
        loadAccounts()
    }

    override fun executeIntent(intent: AccountsStore.Intent) {
        when (intent) {
            is AccountsStore.Intent.LoadAccounts -> {
                loadAccounts()
            }

            is AccountsStore.Intent.CloseToast -> {
                dispatch(AccountsStore.Message.UpdateErrorValue())
            }

            is AccountsStore.Intent.SaveAccount -> {
                saveAccount(intent.account)
            }

            is AccountsStore.Intent.ArchiveAccount -> {
                archiveAccount(intent.id)
            }

            is AccountsStore.Intent.SearchAccounts -> {
                dispatch(AccountsStore.Message.SearchQueryChanged(intent.query))
            }

            is AccountsStore.Intent.BackClicked -> {
                publish(AccountsStore.Label.NavigateBack)
            }

            is AccountsStore.Intent.OpenAddNewAccount -> {
                publish(AccountsStore.Label.NavigateToAddNewAccount())
            }

            is AccountsStore.Intent.OpenEditAccount -> {
                publish(AccountsStore.Label.NavigateToAddNewAccount(intent.accountId))
            }
        }
    }

    private fun fetchMainCurrency() {
        scope.launch {
            currencyRepository.observeCurrencies()
                .collectLatest { currencies ->

                    dispatch(AccountsStore.Message.MainCurrencyChanged(currencies.find { it.isBaseCurrency }))
                }
        }
    }

    private fun loadAccounts() {
        dispatch(AccountsStore.Message.LoadingChanged(true))

        combine(
            accountRepository.observeAccounts(),
            accountRepository.observeAccountGroups()
        ) { accounts, groups ->
            Pair(accounts, groups)
        }
            .onEach { (accounts, groups) ->
                val filteredAccount = accounts.filter { it.isDeleted.not() }
                val groupMap = groups.associateBy { it.id }

                val accountItems = filteredAccount
                    .filter { !it.isArchived && !it.isDeleted }
                    .map { account ->
                        AccountsStore.AccountItem(
                            id = account.id,
                            groupId = account.groupId,
                            groupName = groupMap[account.groupId]?.name ?: "Other",
                            name = account.name,
                            balance = account.balance,
                            currencyCode = account.currency.code,
                            isArchived = account.isArchived,
                            excludeFromTotal = account.excludeFromTotal,
                            description = account.description
                        )
                    }

                val totalBalance = accountItems
                    .filter { !it.excludeFromTotal }
                    .sumOf { it.balance }

                val groupItems = groups.map { group ->
                    AccountsStore.AccountGroupItem(
                        id = group.id,
                        name = group.name,
                        isSystemGroup = group.isSystemGroup,
                        accounts = accountItems.filter { it.groupId == group.id }
                    )
                }.filter { it.accounts.isNotEmpty() }

                dispatch(
                    AccountsStore.Message.AccountsLoaded(
                        accountItems,
                        groupItems,
                        totalBalance
                    )
                )
                dispatch(AccountsStore.Message.LoadingChanged(false))
            }
            .launchIn(scope)
    }

    private fun saveAccount(accountItem: AccountsStore.AccountItem) {
        scope.launch {
            try {
                dispatch(AccountsStore.Message.LoadingChanged(true))

                val account = Account(
                    id = accountItem.id,
                    groupId = accountItem.groupId,
                    name = accountItem.name,
                    balance = accountItem.balance,
                    currency = Currency(
                        code = accountItem.currencyCode,
                        name = accountItem.currencyCode,
                        symbol = accountItem.currencyCode,
                        rateToBase = 1.0,
                        isBaseCurrency = accountItem.currencyCode == "UZS"
                    ),
                    isArchived = accountItem.isArchived,
                    excludeFromTotal = accountItem.excludeFromTotal,
                    description = accountItem.description
                )

                if (accountItem.id == 0L) {
                    val newId = accountRepository.createAccount(account)
                    dispatch(
                        AccountsStore.Message.AccountSaved(
                            accountItem.copy(id = newId)
                        )
                    )
                } else {
                    accountRepository.updateAccount(account)
                    dispatch(AccountsStore.Message.AccountSaved(accountItem))
                }
                dispatch(AccountsStore.Message.LoadingChanged(false))
            } catch (e: Exception) {
                e.printStackTrace()
                dispatch(AccountsStore.Message.ErrorOccurred("Failed to save account: ${e.message}"))
                dispatch(AccountsStore.Message.LoadingChanged(false))
            }
        }
    }

    private fun archiveAccount(id: Long) {
        scope.launch {
            try {
                val account = state().accounts.find { it.id == id } ?: return@launch
                val updatedAccount = Account(
                    id = account.id,
                    groupId = account.groupId,
                    name = account.name,
                    balance = account.balance,
                    currency = Currency(
                        code = account.currencyCode,
                        name = account.currencyCode,
                        symbol = account.currencyCode,
                        rateToBase = 1.0,
                        isBaseCurrency = account.currencyCode == "UZS"
                    ),
                    isArchived = true,
                    excludeFromTotal = account.excludeFromTotal,
                    description = account.description
                )
                accountRepository.updateAccount(updatedAccount)
                dispatch(AccountsStore.Message.AccountArchived(id))
            } catch (e: Exception) {
                dispatch(AccountsStore.Message.ErrorOccurred("Failed to archive account: ${e.message}"))
            }
        }
    }
}
