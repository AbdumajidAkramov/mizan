package dev.esbi.mizan.feature.budget.domain.model

/**
 * Budget Summary
 * Aggregates all category budgets with overall statistics
 */
data class BudgetSummary(
    val categoryBudgets: List<CategoryBudget>,
    val totalBudget: Double,
    val totalSpent: Double,
    val overallPercentage: Int,
    val remaining: Double
)
