package dev.esbi.mizan.presentation.feature.addtransaction.store

import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.presentation.mvikotlin.executor.ExecutorsSet
import javax.inject.Inject
import javax.inject.Provider

class AddNewTransactionStoreFactory @Inject constructor(
    private val storeFactory: Provider<StoreFactory>,
    private val executors: ExecutorsSet<
            AddNewTransactionStore.Intent,
            AddNewTransactionStore.Action,
            AddNewTransactionStore.State,
            AddNewTransactionStore.Message,
            AddNewTransactionStore.Label>,
    private val observer: AddNewTransactionStoreObserver,
) {
    fun create(transactionId: Long? = null): AddNewTransactionStore {
        return AddNewTransactionStoreImpl(
            executors = executors,
            observer = observer,
            storeFactory = storeFactory.get(),
            transactionId = transactionId
        )
    }
}