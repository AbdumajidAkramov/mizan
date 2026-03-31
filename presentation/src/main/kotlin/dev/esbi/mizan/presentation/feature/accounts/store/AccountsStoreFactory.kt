package dev.esbi.mizan.presentation.feature.accounts.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Factory for creating AccountManagementStore instances
 */
class AccountsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val accountRepository: AccountRepository,
    private val currencyRepository: CurrencyRepository
) {

    fun create(): AccountsStore =
        object : AccountsStore,
            Store<AccountsStore.Intent, AccountsStore.State, AccountsStore.Label>
            by storeFactory.create(
                name = "AccountManagementStore",
                initialState = AccountsStore.State(),
                bootstrapper = SimpleBootstrapper(AccountsStore.Action.FetchMainCurrency()),
                executorFactory = {
                    AccountsExecutor(
                        mainDispatcher = Dispatchers.Main,
                        accountRepository = accountRepository,
                        currencyRepository = currencyRepository
                    )
                },
                reducer = AccountsReducer()
            ) {}
}
