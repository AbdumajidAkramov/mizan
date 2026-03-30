package dev.esbi.mizan.presentation.feature.accountmanagement.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.AccountGroup
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.repository.AccountRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Executor for AccountManagement - handles business logic and side effects
 */
internal class AccountManagementExecutor(
    private val mainDispatcher: CoroutineDispatcher,
    private val accountRepository: AccountRepository
) : CoroutineExecutor<
        AccountManagementStore.Intent,
        Unit,
        AccountManagementStore.State,
        AccountManagementStore.Message,
        AccountManagementStore.Label>(
    mainContext = mainDispatcher
) {

    override fun executeAction(action: Unit) {
        loadAccounts()
    }

    override fun executeIntent(intent: AccountManagementStore.Intent) {
        when (intent) {
            is AccountManagementStore.Intent.LoadAccounts -> {
                loadAccounts()
            }

            is AccountManagementStore.Intent.SaveAccount -> {
                saveAccount(intent.account)
            }

            is AccountManagementStore.Intent.DeleteAccount -> {
                publish(AccountManagementStore.Label.ShowDeleteConfirmation(
                    state().accounts.find { it.id == intent.id } ?: return
                ))
            }

            is AccountManagementStore.Intent.ConfirmDeleteAccount -> {
                scope.launch {
                    try {
                        dispatch(AccountManagementStore.Message.LoadingChanged(true))
                        accountRepository.markAccountAsDeleted(intent.id)
                        dispatch(AccountManagementStore.Message.AccountDeleted(intent.id))
                        dispatch(AccountManagementStore.Message.LoadingChanged(false))
                        dispatch(AccountManagementStore.Message.EditSheetHidden)
                    } catch(e: Exception) {
                        dispatch(AccountManagementStore.Message.LoadingChanged(false))
                        dispatch(AccountManagementStore.Message.ErrorOccurred("Failed to delete account: ${e.message}"))
                    }
                }
            }

            is AccountManagementStore.Intent.ArchiveAccount -> {
                archiveAccount(intent.id)
            }

            is AccountManagementStore.Intent.OpenAddAccountSheet -> {
                dispatch(AccountManagementStore.Message.EditSheetShown(null))
            }

            is AccountManagementStore.Intent.OpenEditAccountSheet -> {
                dispatch(AccountManagementStore.Message.EditSheetShown(intent.account))
            }

            is AccountManagementStore.Intent.CloseAddEditSheet -> {
                dispatch(AccountManagementStore.Message.EditSheetHidden)
            }

            is AccountManagementStore.Intent.SearchAccounts -> {
                dispatch(AccountManagementStore.Message.SearchQueryChanged(intent.query))
            }

            is AccountManagementStore.Intent.BackClicked -> {
                publish(AccountManagementStore.Label.NavigateBack)
            }
        }
    }

    private fun loadAccounts() {
        dispatch(AccountManagementStore.Message.LoadingChanged(true))

        combine(
            accountRepository.observeAccounts(),
            accountRepository.observeAccountGroups()
        ) { accounts, groups ->
            Pair(accounts, groups)
        }
            .onEach { (accounts, groups) ->
                val groupMap = groups.associateBy { it.id }

                val accountItems = accounts
                    .filter { !it.isArchived && !it.isDeleted }
                    .map { account ->
                        AccountManagementStore.AccountItem(
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
                    AccountManagementStore.AccountGroupItem(
                        id = group.id,
                        name = group.name,
                        isSystemGroup = group.isSystemGroup,
                        accounts = accountItems.filter { it.groupId == group.id }
                    )
                }.filter { it.accounts.isNotEmpty() }

                dispatch(AccountManagementStore.Message.AccountsLoaded(accountItems, groupItems, totalBalance))
                dispatch(AccountManagementStore.Message.LoadingChanged(false))
            }
            .launchIn(scope)
    }

    private fun saveAccount(accountItem: AccountManagementStore.AccountItem) {
        scope.launch {
            try {
                dispatch(AccountManagementStore.Message.LoadingChanged(true))

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
                    dispatch(AccountManagementStore.Message.AccountSaved(
                        accountItem.copy(id = newId)
                    ))
                } else {
                    accountRepository.updateAccount(account)
                    dispatch(AccountManagementStore.Message.AccountSaved(accountItem))
                }

                dispatch(AccountManagementStore.Message.EditSheetHidden)
                dispatch(AccountManagementStore.Message.LoadingChanged(false))
            } catch (e: Exception) {
                e.printStackTrace()
                dispatch(AccountManagementStore.Message.ErrorOccurred("Failed to save account: ${e.message}"))
                dispatch(AccountManagementStore.Message.LoadingChanged(false))
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
                dispatch(AccountManagementStore.Message.AccountArchived(id))
            } catch (e: Exception) {
                dispatch(AccountManagementStore.Message.ErrorOccurred("Failed to archive account: ${e.message}"))
            }
        }
    }
}
