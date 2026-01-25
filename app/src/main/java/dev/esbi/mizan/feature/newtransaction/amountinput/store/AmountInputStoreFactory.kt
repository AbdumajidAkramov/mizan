package dev.esbi.mizan.feature.newtransaction.amountinput.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.feature.newtransaction.amountinput.store.executors.AmountInputExecutor
import javax.inject.Inject

internal class AmountInputStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val amountInputExecutor: AmountInputExecutor,
) {

    fun create(): AmountInputStore {
        return object : AmountInputStore,
            Store<AmountInputStore.Intent, AmountInputState, AmountInputStore.Label> by
            storeFactory.create(
                name = "AmountInputStore",
                initialState = AmountInputState(),
                bootstrapper = SimpleBootstrapper(),
                executorFactory = { amountInputExecutor },
                reducer = AmountInputReducer,
            ) {}
    }
}
