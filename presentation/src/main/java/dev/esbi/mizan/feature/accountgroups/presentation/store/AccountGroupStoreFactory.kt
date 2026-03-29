package dev.esbi.mizan.feature.accountgroups.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.domain.repository.AccountRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AccountGroupStoreFactory(
    private val storeFactory: StoreFactory,
    private val accountRepository: AccountRepository
) {
    fun create(): AccountGroupStore =
        object : AccountGroupStore, Store<AccountGroupStore.Intent, AccountGroupStore.State, AccountGroupStore.Label>
        by storeFactory.create(
            name = "AccountGroupStore",
            initialState = AccountGroupStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = {
                AccountGroupExecutor(
                    mainDispatcher = Dispatchers.Main,
                    accountRepository = accountRepository
                )
            },
            reducer = AccountGroupReducer()
        ) {}
}
