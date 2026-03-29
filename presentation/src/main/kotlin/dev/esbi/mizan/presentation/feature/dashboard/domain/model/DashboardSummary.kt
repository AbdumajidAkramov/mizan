package dev.esbi.mizan.presentation.feature.dashboard.domain.model

import dev.esbi.mizan.domain.model.Transaction

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
