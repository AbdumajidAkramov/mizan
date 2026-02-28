package dev.esbi.mizan.feature.accountselector.store

import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AccountSelectorStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val executorFactory: AccountSelectorExecutor.Factory
) {

    fun create(
        selectedAccountId: Long? = null
    ): AccountSelectorStore {
        return DefaultAccountSelectorStore(
            storeFactory = storeFactory,
            initialState = AccountSelectorStore.State(selectedAccountId = selectedAccountId),
            mainDispatcher = mainDispatcher,
            executorFactory = executorFactory
        )
    }
}
