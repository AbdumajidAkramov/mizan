package dev.esbi.mizan.presentation.feature.accounts.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.presentation.mvikotlin.executor.CompositeExecutor
import dev.esbi.mizan.presentation.mvikotlin.executor.ExecutorsSet
import dev.esbi.mizan.presentation.mvikotlin.utils.create
import javax.inject.Inject

class AccountsStoreImpl @Inject constructor(
    private val executors: ExecutorsSet<
            AccountsStore.Intent,
            AccountsStore.Action,
            AccountsStore.State,
            AccountsStore.Message,
            AccountsStore.Label>,
    private val observer: AccountsStoreObserver,
    private val storeFactory: StoreFactory,
) : AccountsStore,
    Store<AccountsStore.Intent, AccountsStore.State, AccountsStore.Label> by storeFactory.create(
        name = "AccountsStore",
        initialState = AccountsStore.State(),
        bootstrapper = SimpleBootstrapper(
            AccountsStore.Action.FetchMainCurrency()
        ),
        executorFactory = { CompositeExecutor(executors) },
        reducer = AccountsReducer(),
        observers = setOf(observer)
    )