package dev.esbi.mizan.feature.accountmanagement.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.domain.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Factory for creating AccountManagementStore instances
 */
class AccountManagementStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val accountRepository: AccountRepository
) {

    fun create(): AccountManagementStore =
        object : AccountManagementStore,
            Store<AccountManagementStore.Intent, AccountManagementStore.State, AccountManagementStore.Label>
            by storeFactory.create(
                name = "AccountManagementStore",
                initialState = AccountManagementStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = {
                    AccountManagementExecutor(
                        mainDispatcher = Dispatchers.Main,
                        accountRepository = accountRepository
                    )
                },
                reducer = AccountManagementReducer()
            ) {}
}
