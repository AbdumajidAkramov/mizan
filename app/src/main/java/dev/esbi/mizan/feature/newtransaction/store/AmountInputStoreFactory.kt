package dev.esbi.mizan.feature.newtransaction.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.feature.newtransaction.store.executors.NewTransactionExecutor
import javax.inject.Inject
import javax.inject.Provider

internal class AmountInputStoreFactory @Inject constructor(
    private val storeFactory: Provider<StoreFactory>,
    private val amountInputExecutor: Provider<NewTransactionExecutor>,
) {

    fun create(): NewTransactionStore {
        return object : NewTransactionStore,
            Store<NewTransactionStore.Intent, AmountInputState, NewTransactionStore.Label> by
            storeFactory.get().create(
                name = "AmountInputStore",
                initialState = AmountInputState(),
                bootstrapper = SimpleBootstrapper(
                    NewTransactionStore.Action.Init
                ),
                executorFactory = { amountInputExecutor.get() },
                reducer = NewTransactionReducer,
            ) {}
    }
}
