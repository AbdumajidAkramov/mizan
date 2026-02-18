package dev.esbi.mizan.feature.premiumaddtransaction.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.mvikotlin.executor.CompositeExecutor
import dev.esbi.mizan.mvikotlin.executor.ExecutorsSet
import dev.esbi.mizan.mvikotlin.utils.create
import javax.inject.Inject

class AddNewTransactionStoreImpl @Inject constructor(
    private val executors: ExecutorsSet<
            AddNewTransactionStore.Intent,
            AddNewTransactionStore.Action,
            AddNewTransactionStore.State,
            AddNewTransactionStore.Message,
            AddNewTransactionStore.Label>,
    private val observer: AddNewTransactionStoreObserver,
    private val storeFactory: StoreFactory,
) : AddNewTransactionStore,
    Store<AddNewTransactionStore.Intent, AddNewTransactionStore.State, AddNewTransactionStore.Label> by storeFactory.create(
        initialState = AddNewTransactionStore.State(),
        bootstrapper = SimpleBootstrapper(
            AddNewTransactionStore.Action.InitPad,
            AddNewTransactionStore.Action.InitAccounts,
            AddNewTransactionStore.Action.InitCategories,
            AddNewTransactionStore.Action.CheckAndConfirm
        ),
        executorFactory = { CompositeExecutor(executors) },
        reducer = AddNewTransactionReducer,
        observers = setOf(observer)
    )
