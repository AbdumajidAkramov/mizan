package dev.esbi.mizan.feature.newtransaction2.store.state

import dev.esbi.mizan.domain.model.Category


data class CategorySelectBottomSheetState(
    val selectCategoryId: Long? = null,
    val selectedParentId: Long? = null,
    val parentCategory: Category? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val categories: List<Category> = emptyList()
)
