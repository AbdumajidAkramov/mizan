package dev.esbi.mizan.feature.statistics.domain.model

/**
 * Comparison between current and previous period
 */
data class PeriodComparison(
    val currentIncome: Double,
    val previousIncome: Double,
    val incomeChangePercentage: Float,
    val currentExpense: Double,
    val previousExpense: Double,
    val expenseChangePercentage: Float
)
