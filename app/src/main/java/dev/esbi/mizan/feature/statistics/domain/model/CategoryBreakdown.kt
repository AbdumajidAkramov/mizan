package dev.esbi.mizan.feature.statistics.domain.model

/**
 * Category spending breakdown with percentage
 */
data class CategoryBreakdown(
    val categoryId: String,
    val categoryName: String,
    val amount: Double,
    val percentage: Float,
    val color: String
)
