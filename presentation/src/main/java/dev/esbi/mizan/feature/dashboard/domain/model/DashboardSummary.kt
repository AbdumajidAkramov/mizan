package dev.esbi.mizan.feature.dashboard.domain.model

data class DashboardSummary(
    val totalBalance: Double,
    val monthlyExpenses: Double,
    val monthlySavings: Double,
    val budgetLimit: Double,
    val budgetPercentageUsed: Double,
    val topCategories: List<CategorySpending>,
    val weeklySpending: List<WeeklySpendingPoint>,
    val recentTransactions: List<Transaction>
)
