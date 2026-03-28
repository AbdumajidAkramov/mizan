package dev.esbi.mizan.feature.accounts.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import javax.inject.Inject

class AccountsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val executorFactory: AccountsExecutor.Factory
) {

    fun create(): AccountsStore =
        object : AccountsStore,
            Store<AccountsStore.Intent, AccountsStore.State, AccountsStore.Label> by storeFactory.create(
                name = "AccountsStore",
                initialState = AccountsStore.State(),
                bootstrapper = CoroutineBootstrapperImpl(),
                executorFactory = { executorFactory.create() },
                reducer = AccountsReducer()
            ) {}

    private class CoroutineBootstrapperImpl : CoroutineBootstrapper<Unit>() {
        override fun invoke() {
            dispatch(Unit)
        }
    }
}
