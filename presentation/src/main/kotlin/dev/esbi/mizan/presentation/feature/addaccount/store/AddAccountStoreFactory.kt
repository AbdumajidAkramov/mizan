package dev.esbi.mizan.presentation.feature.addaccount.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.domain.repository.AccountRepository
import kotlinx.coroutines.Dispatchers

class AddAccountStoreFactory(
    private val storeFactory: StoreFactory,
    private val accountRepository: AccountRepository
) {
    fun create(): AddAccountStore =
        object : AddAccountStore, Store<AddAccountStore.Intent, AddAccountStore.State, AddAccountStore.Label>
        by storeFactory.create(
            name = "AddAccountStore",
            initialState = AddAccountStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = {
                AddAccountExecutor(
                    mainDispatcher = Dispatchers.Main,
                    accountRepository = accountRepository
                )
            },
            reducer = AddAccountReducer()
        ) {}
}
