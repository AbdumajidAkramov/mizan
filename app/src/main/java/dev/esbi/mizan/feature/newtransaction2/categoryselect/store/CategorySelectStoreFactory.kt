package dev.esbi.mizan.feature.newtransaction2.categoryselect.store

import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.presentation.feature.addtransaction.model.TransactionType
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class CategorySelectStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) {

    fun create(
        transactionType: TransactionType
    ): CategorySelectStore {
        return DefaultCategorySelectStore(
            storeFactory = storeFactory,
            initialState = CategorySelectStore.State(transactionType = transactionType),
            mainDispatcher = mainDispatcher
        )
    }
}
