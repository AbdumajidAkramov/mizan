package dev.esbi.mizan.feature.newtransaction.categoryselect.store

import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
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
