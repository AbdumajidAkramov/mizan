package dev.esbi.mizan.feature.dashboard.domain.model

data class CategorySpending(
    val category: String,
    val categoryLabel: String,
    val totalAmount: Double,
    val transactionCount: Int,
    val percentage: Double,
    val colorToken: String
)
