package dev.esbi.mizan.feature.newtransaction.amountinput.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.feature.newtransaction.amountinput.store.executors.AmountInputExecutor
import javax.inject.Inject
import javax.inject.Provider

internal class AmountInputStoreFactory @Inject constructor(
    private val storeFactory: Provider<StoreFactory>,
    private val amountInputExecutor: Provider<AmountInputExecutor>,
) {

    fun create(): AmountInputStore {
        return object : AmountInputStore,
            Store<AmountInputStore.Intent, AmountInputState, AmountInputStore.Label> by
            storeFactory.get().create(
                name = "AmountInputStore",
                initialState = AmountInputState(),
                bootstrapper = SimpleBootstrapper(),
                executorFactory = { amountInputExecutor.get() },
                reducer = AmountInputReducer,
            ) {}
    }
}
