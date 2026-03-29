package dev.esbi.mizan.feature.newtransaction.categoryselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.newtransaction.categoryselect.store.CategorySelectStore
import dev.esbi.mizan.feature.newtransaction.categoryselect.store.CategorySelectStoreFactory
import dev.esbi.mizan.presentation.feature.addtransaction.presentation.models.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class CategorySelectViewModel @Inject constructor(
    private val storeFactory: CategorySelectStoreFactory
) : ViewModel() {

    private val transactionType: TransactionType = TransactionType.EXPENSE

    private val store: CategorySelectStore by lazy {
        storeFactory.create(transactionType)
    }

    val state: Flow<CategorySelectStore.State> = store.states

    val labels: Flow<CategorySelectStore.Label> = store.labels

    init {
        // Load categories immediately after initialization
        viewModelScope.launch {
            store.accept(CategorySelectStore.Intent.LoadCategories)
        }
    }

    fun onIntent(intent: CategorySelectStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        if (store.isDisposed.not()) {
            store.dispose()
        }
    }
}
