package dev.esbi.mizan.feature.accountmanagement.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.repository.AccountRepository
import kotlinx.coroutines.CoroutineDispatcher
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

        accountRepository.observeAccounts()
            .onEach { accounts ->
                val accountItems = accounts
                    .filter { !it.isArchived }
                    .map { account ->
                        AccountManagementStore.AccountItem(
                            id = account.id,
                            groupId = account.groupId,
                            name = account.name,
                            type = account.type,
                            balance = account.balance,
                            currencyCode = account.currency.code,
                            iconName = account.iconName,
                            color = account.color,
                            isArchived = account.isArchived,
                            excludeFromTotal = account.excludeFromTotal,
                            description = account.description
                        )
                    }

                val totalBalance = accountItems
                    .filter { !it.excludeFromTotal }
                    .sumOf { it.balance }

                dispatch(AccountManagementStore.Message.AccountsLoaded(accountItems, totalBalance))
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
                    type = accountItem.type,
                    balance = accountItem.balance,
                    currency = Currency(
                        code = accountItem.currencyCode,
                        name = accountItem.currencyCode,
                        symbol = accountItem.currencyCode,
                        rateToBase = 1.0,
                        isBaseCurrency = accountItem.currencyCode == "UZS"
                    ),
                    iconName = accountItem.iconName,
                    color = accountItem.color,
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
                    type = account.type,
                    balance = account.balance,
                    currency = Currency(
                        code = account.currencyCode,
                        name = account.currencyCode,
                        symbol = account.currencyCode,
                        rateToBase = 1.0,
                        isBaseCurrency = account.currencyCode == "UZS"
                    ),
                    iconName = account.iconName,
                    color = account.color,
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

    fun confirmDeleteAccount(id: Long) {
        scope.launch {
            try {
                accountRepository.deleteAccount(id)
                dispatch(AccountManagementStore.Message.AccountDeleted(id))
            } catch (e: Exception) {
                dispatch(AccountManagementStore.Message.ErrorOccurred("Failed to delete account: ${e.message}"))
            }
        }
    }
}
