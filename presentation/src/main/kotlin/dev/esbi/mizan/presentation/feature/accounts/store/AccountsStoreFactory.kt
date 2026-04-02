package dev.esbi.mizan.presentation.feature.accounts.store

import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.presentation.mvikotlin.executor.ExecutorsSet
import javax.inject.Inject
import javax.inject.Provider

/**
 * Factory for creating AccountManagementStore instances
 */
class AccountsStoreFactory @Inject constructor(
    private val storeFactory: Provider<StoreFactory>,
    private val executors: ExecutorsSet<
            AccountsStore.Intent,
            AccountsStore.Action,
            AccountsStore.State,
            AccountsStore.Message,
            AccountsStore.Label>,
    private val observer: AccountsStoreObserver,
) {
    fun create(): AccountsStore {
        return AccountsStoreImpl(
            executors = executors,
            observer = observer,
            storeFactory = storeFactory.get(),
        )
    }
}
