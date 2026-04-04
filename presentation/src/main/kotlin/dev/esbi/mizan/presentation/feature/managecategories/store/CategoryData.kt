package dev.esbi.mizan.presentation.feature.managecategories.store

import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction

/**
 * Simple implementation of the Category interface for use in presentation layer.
 */
import java.math.BigDecimal

data class CategoryData(
    override val id: Long,
    override val name: String,
    override val iconName: String,
    override val color: String,
    override val type: Transaction.Type,
    override val orderIndex: Int = 0,
    override val parentId: Long? = null,
    override val budgetLimit: BigDecimal? = null,
    override val isArchived: Boolean = false
) : Category
