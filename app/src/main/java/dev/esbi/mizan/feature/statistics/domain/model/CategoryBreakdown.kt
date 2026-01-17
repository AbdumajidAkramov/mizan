package dev.esbi.mizan.feature.statistics.domain.model

import androidx.compose.ui.graphics.Color

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


data class ChartDataPoint(
    val label: String,
    val value: Float
)

data class CategoryData(
    val name: String,
    val amount: Float,
    val percentage: Float,
    val color: Color
)

