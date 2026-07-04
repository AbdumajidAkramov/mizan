package dev.esbi.mizan.domain.model.dashboard

import dev.esbi.mizan.domain.model.Transaction
import java.math.BigDecimal

data class DashboardSummary(
    val totalBalance: BigDecimal,
    val monthlyExpenses: BigDecimal,
    val monthlySavings: BigDecimal,
    val budgetLimit: Double,
    val budgetPercentageUsed: Double,
    val topCategories: List<CategorySpending>,
    val weeklySpending: List<WeeklySpendingPoint>,
    val recentTransactions: List<Transaction>
)
