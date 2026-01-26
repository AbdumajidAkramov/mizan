package dev.esbi.mizan.feature.newtransaction.categorychooser

import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction

data class CategoryChooserState(
    val categories: List<Category> = emptyList(),
    val selectedParentId: Long? = null,
    val transactionType: Transaction.Type = Transaction.Type.EXPENSE,
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
