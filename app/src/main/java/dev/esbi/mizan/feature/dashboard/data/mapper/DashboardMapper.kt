package dev.esbi.mizan.feature.dashboard.data.mapper

import dev.esbi.mizan.data.local.entity.CategorySpendingEntity
import dev.esbi.mizan.data.local.entity.DashboardSummaryEntity
import dev.esbi.mizan.data.local.entity.TransactionEntity
import dev.esbi.mizan.data.local.entity.WeeklySpendingEntity
import dev.esbi.mizan.feature.dashboard.domain.model.CategorySpending
import dev.esbi.mizan.feature.dashboard.domain.model.DashboardSummary
import dev.esbi.mizan.feature.dashboard.domain.model.Transaction
import dev.esbi.mizan.feature.dashboard.domain.model.TransactionType
import dev.esbi.mizan.feature.dashboard.domain.model.WeeklySpendingPoint

fun DashboardSummaryEntity.toDomain(
    categories: List<CategorySpending>,
    weeklySpending: List<WeeklySpendingPoint>,
    transactions: List<Transaction>
): DashboardSummary {
    return DashboardSummary(
        totalBalance = totalBalance,
        monthlyExpenses = monthlyExpenses,
        monthlySavings = monthlySavings,
        budgetLimit = budgetLimit,
        budgetPercentageUsed = budgetPercentageUsed,
        topCategories = categories,
        weeklySpending = weeklySpending,
        recentTransactions = transactions
    )
}

fun CategorySpendingEntity.toDomain(): CategorySpending {
    return CategorySpending(
        category = category,
        categoryLabel = categoryLabel,
        totalAmount = totalAmount,
        transactionCount = transactionCount,
        percentage = percentage,
        colorToken = colorToken
    )
}

fun WeeklySpendingEntity.toDomain(): WeeklySpendingPoint {
    return WeeklySpendingPoint(
        dayLabel = dayLabel,
        totalAmount = totalAmount,
        dayIndex = dayIndex
    )
}

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        category = category,
        categoryLabel = categoryLabel,
        description = description,
        date = date,
        type = when (type) {
            "INCOME" -> TransactionType.INCOME
            "TRANSFER" -> TransactionType.TRANSFER
            else -> TransactionType.EXPENSE
        },
        colorToken = colorToken
    )
}
