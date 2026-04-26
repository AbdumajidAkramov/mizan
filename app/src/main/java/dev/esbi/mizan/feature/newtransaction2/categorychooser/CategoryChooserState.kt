package dev.esbi.mizan.feature.newtransaction2.categorychooser

import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction

data class CategoryChooserState(
    val transactionType: Transaction.Type,
    val categories: List<Category> = emptyList(),
    val selectedParentId: Long? = null,
    val selectedChildId: Long? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategory: Category? = null
) {
    val mainCategories: List<Category>
        get() = categories
            .filter { it.parentId == null }
            .sortedBy { it.orderIndex }
}
