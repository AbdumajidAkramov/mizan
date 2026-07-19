package dev.esbi.mizan.domain.model.dashboard

data class CategorySpending(
    val category: String,
    val categoryLabel: String,
    val totalAmount: Double,
    val transactionCount: Int,
    val percentage: Double,
    val colorToken: String
)
