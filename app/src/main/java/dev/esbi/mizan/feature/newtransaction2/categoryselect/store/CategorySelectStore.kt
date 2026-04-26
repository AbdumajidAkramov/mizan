package dev.esbi.mizan.feature.newtransaction2.categoryselect.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.presentation.feature.addtransaction.model.TransactionType

interface CategorySelectStore :
    Store<CategorySelectStore.Intent, CategorySelectStore.State, CategorySelectStore.Label> {

    data class State(
        val categories: List<Category> = emptyList(),
        val selectedParentId: Long? = null,
        val transactionType: TransactionType = TransactionType.EXPENSE,
        val isLoading: Boolean = false,
        val error: String? = null,
        val selectedCategory: Category? = null
    ) {
        val isSubcategoryView: Boolean
            get() = selectedParentId != null

        val currentCategories: List<Category>
            get() = if (selectedParentId != null) {
                categories.filter { it.parentId == selectedParentId }
            } else {
                categories.filter { it.parentId == null }
            }

        val parentCategory: Category?
            get() = selectedParentId?.let { parentId ->
                categories.find { it.id == parentId }
            }

        val hasSubcategories: Boolean
            get() = currentCategories.any { category ->
                categories.any { it.parentId == category.id }
            }
    }

    sealed interface Intent {
        object LoadCategories : Intent
        class SelectParentCategory(val category: Category) : Intent
        class SelectSubCategory(val category: Category) : Intent
        object NavigateBack : Intent
        data object RetryLoad : Intent
        object ManageCategories : Intent
    }


    sealed interface Label {
        object NavigateBack : Label
        class CategorySelected(val category: Category) : Label
        object NavigateToManageCategories : Label
        class ShowError(val message: String) : Label
    }
}
