package dev.esbi.mizan.feature.newtransaction2.categoryselect.store

import com.arkivanov.mvikotlin.core.store.Reducer
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.presentation.feature.addtransaction.model.TransactionType

internal object CategorySelectReducer :
    Reducer<CategorySelectStore.State, CategorySelectReducer.Message> {

    sealed interface Message {
        class CategoriesLoaded(val categories: List<Category>) : Message
        class LoadingChanged(val isLoading: Boolean) : Message
        class ErrorChanged(val error: String?) : Message
        class ParentCategorySelected(val parentId: Long?) : Message
        object NavigateToParent : Message
        class TransactionTypeChanged(val transactionType: TransactionType) : Message
        class CategorySelected(val category: Category) : Message
    }

    override fun CategorySelectStore.State.reduce(msg: Message): CategorySelectStore.State {
        return when (msg) {
            is Message.CategoriesLoaded -> copy(
                categories = msg.categories,
                isLoading = false,
                error = null
            )

            is Message.LoadingChanged -> copy(
                isLoading = msg.isLoading,
            )

            is Message.ErrorChanged -> copy(
                error = msg.error,
                isLoading = false
            )

            is Message.ParentCategorySelected -> copy(
                selectedParentId = msg.parentId
            )

            is Message.NavigateToParent -> copy(
                selectedParentId = null
            )

            is Message.TransactionTypeChanged -> copy(
                transactionType = msg.transactionType,
                selectedParentId = null,
                selectedCategory = null
            )

            is Message.CategorySelected -> copy(
                selectedCategory = msg.category

            )
        }
    }
}
