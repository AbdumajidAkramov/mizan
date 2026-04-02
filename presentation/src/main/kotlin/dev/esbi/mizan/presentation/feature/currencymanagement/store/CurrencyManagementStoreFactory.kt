package dev.esbi.mizan.presentation.feature.currencymanagement.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers

class CurrencyManagementStoreFactory(
    private val storeFactory: StoreFactory,
    private val currencyRepository: CurrencyRepository
) {
    fun create(): CurrencyManagementStore =
        object : CurrencyManagementStore,
            Store<CurrencyManagementStore.Intent, CurrencyManagementStore.State, CurrencyManagementStore.Label>
            by storeFactory.create(
                name = "CurrencyManagementStore",
                initialState = CurrencyManagementStore.State(),
                bootstrapper = SimpleBootstrapper(CurrencyManagementStore.Action.Init),
                executorFactory = {
                    CurrencyManagementExecutor(
                        mainDispatcher = Dispatchers.Main,
                        currencyRepository = currencyRepository
                    )
                },
                reducer = CurrencyManagementReducer()
            ) {}
}
