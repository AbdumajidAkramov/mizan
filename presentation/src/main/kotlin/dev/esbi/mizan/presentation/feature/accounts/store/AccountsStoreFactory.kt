package dev.esbi.mizan.presentation.feature.accounts.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.util.CurrencyConverter
import dev.esbi.mizan.presentation.feature.accounts.store.AccountsStore.Action
import dev.esbi.mizan.presentation.feature.accounts.store.AccountsStore.Intent
import dev.esbi.mizan.presentation.feature.accounts.store.AccountsStore.Label
import dev.esbi.mizan.presentation.feature.accounts.store.AccountsStore.State
import dev.esbi.mizan.presentation.feature.accounts.store.AccountsStore.AccountGroupUIModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val repository: AccountRepository,
    private val currencyConverter: CurrencyConverter,
    private val mainDispatcher: CoroutineDispatcher
) {

    fun create(): AccountsStore =
        object : AccountsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AccountsStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Action.Init),
            executorFactory = { ExecutorImpl(repository, currencyConverter, mainDispatcher) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Message {
        data object Loading : Message
        data class Error(val message: String) : Message
        data class AccountsLoaded(
            val accounts: List<Account>,
            val groups: List<AccountGroupUIModel>,
            val totalBalance: Double
        ) : Message
        data class SearchUpdated(val query: String, val groups: List<AccountGroupUIModel>, val totalBalance: Double) : Message
    }

    private class ExecutorImpl(
        private val repository: AccountRepository,
        private val currencyConverter: CurrencyConverter,
        mainDispatcher: CoroutineDispatcher
    ) : CoroutineExecutor<Intent, Action, State, Message, Label>(
        mainContext = mainDispatcher
    ) {

        private var allAccounts: List<Account> = emptyList()

        override fun executeAction(action: Action) {
            when (action) {
                is Action.Init -> observeAccounts()
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.NavigateBackClicked -> publish(Label.NavigateBack)
                is Intent.AddAccountClicked -> publish(Label.NavigateToAddAccount)
                is Intent.EditAccountClicked -> publish(Label.NavigateToEditAccount(intent.id))
                is Intent.ArchiveAccountClicked -> archiveAccount(intent.id)
                is Intent.Search -> performSearch(intent.query)
            }
        }

        private fun observeAccounts() {
            repository.observeAccounts()
                .onStart { dispatch(Message.Loading) }
                .onEach { accounts ->
                    allAccounts = accounts
                    val (groups, total) = processAccounts(accounts, state().searchQuery)
                    dispatch(Message.AccountsLoaded(accounts, groups, total))
                }
                .catch { e ->
                    dispatch(Message.Error(e.message ?: "Unknown error"))
                    publish(Label.ShowError(e.message ?: "Could not load accounts"))
                }
                .launchIn(scope)
        }

        private fun archiveAccount(id: Long) {
            scope.launch {
                try {
                    val account = repository.getAccount(id)
                    if (account != null) {
                        repository.updateAccount(account.copy(isArchived = true))
                    }
                } catch (e: Exception) {
                    publish(Label.ShowError("Could not archive account"))
                }
            }
        }

        private fun performSearch(query: String) {
            val (groups, total) = processAccounts(allAccounts, query)
            dispatch(Message.SearchUpdated(query, groups, total))
        }

        private fun processAccounts(
            accounts: List<Account>,
            query: String
        ): Pair<List<AccountGroupUIModel>, Double> {
            val activeAccounts = accounts.filter { !it.isArchived }
            val filteredAccounts = if (query.isBlank()) {
                activeAccounts
            } else {
                activeAccounts.filter { it.name.contains(query, ignoreCase = true) }
            }

            // Calculate total balance across all currencies (in base currency)
            val totalBalance = filteredAccounts.sumOf { 
                if (it.excludeFromTotal) 0.0 else currencyConverter.convertToBase(it.balance, it.currency.code)
            }

            // Group by groupId
            val groups = filteredAccounts
                .groupBy { it.groupId }
                .map { (groupId, accounts) ->
                    AccountGroupUIModel(
                        id = groupId.toString(),
                        label = "Accounts",
                        accounts = accounts
                    )
                }

            return Pair(groups, totalBalance)
        }
    }

    private object ReducerImpl : Reducer<State, Message> {
        override fun State.reduce(msg: Message): State =
            when (msg) {
                is Message.Loading -> copy(isLoading = true, error = null)
                is Message.Error -> copy(isLoading = false, error = msg.message)
                is Message.AccountsLoaded -> copy(
                    isLoading = false,
                    groupedAccounts = msg.groups,
                    totalBalance = msg.totalBalance,
                    error = null
                )
                is Message.SearchUpdated -> copy(
                    searchQuery = msg.query,
                    groupedAccounts = msg.groups,
                    totalBalance = msg.totalBalance
                )
            }
    }
}
