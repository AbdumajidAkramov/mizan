package dev.esbi.mizan.presentation.feature.accountselector.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.presentation.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher

class DefaultAccountSelectorStore(
    storeFactory: StoreFactory,
    initialState: AccountSelectorStore.State,
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val executorFactory: AccountSelectorExecutor.Factory
) : AccountSelectorStore,
    Store<AccountSelectorStore.Intent, AccountSelectorStore.State, AccountSelectorStore.Label> by storeFactory.create(
        name = "AccountSelectorStore",
        initialState = initialState,
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = { executorFactory.create() },
        reducer = AccountSelectorReducer
    )
