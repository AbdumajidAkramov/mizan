package dev.esbi.mizan.feature.managecategories.store

import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction

/**
 * Simple implementation of the Category interface for use in presentation layer.
 */
data class CategoryData(
    override val id: Long,
    override val name: String,
    override val iconName: String,
    override val color: String,
    override val type: Transaction.Type,
    override val orderIndex: Int,
    override val parentId: Long? = null,
    override val budgetLimit: Double? = null,
    override val isArchived: Boolean = false
) : Category
