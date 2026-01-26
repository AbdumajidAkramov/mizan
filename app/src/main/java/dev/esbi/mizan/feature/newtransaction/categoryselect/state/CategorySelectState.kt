package dev.esbi.mizan.feature.newtransaction.categoryselect.state

import dev.esbi.mizan.domain.model.Category

data class CategorySelectState(
    val selectedCategory: Category? = null,
    val categories: List<Category> = emptyList(),
    val isExpanded: Boolean = false
)
