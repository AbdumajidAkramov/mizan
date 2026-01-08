package dev.esbi.mizan.feature.budget.domain.model

/**
 * Category Budget
 * Represents budget allocation and spending for a specific category
 */
data class CategoryBudget(
    val categoryId: String,
    val categoryName: String,
    val budgetAmount: Double,
    val spentAmount: Double,
    val percentage: Int,
    val status: BudgetStatus
)

enum class BudgetStatus {
    NORMAL,
    NEAR_LIMIT,
    OVER_BUDGET
}

/**
 * Helper to determine budget status based on percentage
 */
fun calculateBudgetStatus(percentage: Int): BudgetStatus {
    return when {
        percentage >= 100 -> BudgetStatus.OVER_BUDGET
        percentage >= 80 -> BudgetStatus.NEAR_LIMIT
        else -> BudgetStatus.NORMAL
    }
}
