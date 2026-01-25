package dev.esbi.mizan.feature.newtransaction.amountinput.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.mvikotlin.executor.CompositeExecutor
import dev.esbi.mizan.mvikotlin.executor.ExecutorsSet
import dev.esbi.mizan.mvikotlin.utils.create
import javax.inject.Inject

internal class AmountInputStoreImpl @Inject constructor(
    private val storeFactory: StoreFactory,
    private val executors: ExecutorsSet<
            AmountInputStore.Intent,
            AmountInputStore.Action,
            AmountInputState,
            AmountInputStore.Message,
            AmountInputStore.Label>,
    private val observer: AmountInputObserver,
) : AmountInputStore,
    Store<AmountInputStore.Intent, AmountInputStore.State, AmountInputStore.Label> by storeFactory.create(
        initialState = AmountInputStore.State(),
        bootstrapper = SimpleBootstrapper(),
        reducer = AmountInputReducer,
        executorFactory = { CompositeExecutor(executors) },
        observers = setOf(observer),
    )
