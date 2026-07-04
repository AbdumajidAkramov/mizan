package dev.esbi.mizan.transactions.add.mvikotlin

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.presentation.mvikotlin.executor.CompositeExecutor
import dev.esbi.mizan.presentation.mvikotlin.executor.ExecutorsSet
import dev.esbi.mizan.presentation.mvikotlin.utils.create
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
    private val transactionId: Long? = null,
) : AddNewTransactionStore,
    Store<AddNewTransactionStore.Intent, AddNewTransactionStore.State, AddNewTransactionStore.Label> by storeFactory.create(
        initialState = AddNewTransactionStore.State(
            editingTransactionId = transactionId,
            isEditMode = transactionId != null
        ),
        bootstrapper = SimpleBootstrapper(
            AddNewTransactionStore.Action.InitAccounts,
            AddNewTransactionStore.Action.InitCategories,
            AddNewTransactionStore.Action.InitCurrencies,
            AddNewTransactionStore.Action.LoadTransaction,
            AddNewTransactionStore.Action.CheckAndConfirm

        ),
        executorFactory = { CompositeExecutor(executors) },
        reducer = AddNewTransactionReducer,
        observers = setOf(observer)
    )
