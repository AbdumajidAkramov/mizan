package dev.esbi.mizan.presentation.feature.addaccount.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers

class AddAccountStoreFactory(
    private val accountId: Long? = null,
    private val storeFactory: StoreFactory,
    private val accountRepository: AccountRepository,
    private val currencyRepository: CurrencyRepository
) {
    fun create(): AddAccountStore =
        object : AddAccountStore,
            Store<AddAccountStore.Intent, AddAccountStore.State, AddAccountStore.Label>
            by storeFactory.create(
                name = "AddAccountStore",
                initialState = AddAccountStore.State(accountId),
                bootstrapper = SimpleBootstrapper(
                    AddAccountStore.Action.FetchAccount(accountId),
                    AddAccountStore.Action.FetchCurrencies(),
                    AddAccountStore.Action.FetchAccountGroups()
                ),
                executorFactory = {
                    AddAccountExecutor(
                        mainDispatcher = Dispatchers.Main,
                        accountRepository = accountRepository,
                        currencyRepository = currencyRepository
                    )
                },
                reducer = AddAccountReducer()
            ) {}
}
