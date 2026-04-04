package dev.esbi.mizan.feature.dashboard.data.mapper

import dev.esbi.mizan.data.local.entity.CategorySpendingEntity
import dev.esbi.mizan.data.local.entity.DashboardSummaryEntity
import dev.esbi.mizan.data.local.entity.WeeklySpendingEntity
import dev.esbi.mizan.data.local.entity.transaction.TransactionEntity
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.presentation.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.presentation.feature.dashboard.domain.model.CategorySpending
import dev.esbi.mizan.presentation.feature.dashboard.domain.model.DashboardSummary
import dev.esbi.mizan.presentation.feature.dashboard.domain.model.WeeklySpendingPoint

fun DashboardSummaryEntity.toDomain(
    categories: List<CategorySpending>,
    weeklySpending: List<WeeklySpendingPoint>,
    transactions: List<Transaction>
): DashboardSummary {
    return DashboardSummary(
        totalBalance = totalBalance,
        monthlyExpenses = monthlyExpenses,
        monthlySavings = monthlySavings,
        budgetLimit = budgetLimit.toDouble(),
        budgetPercentageUsed = budgetPercentageUsed.toDouble(),
        topCategories = categories,
        weeklySpending = weeklySpending,
        recentTransactions = transactions
    )
}

fun CategorySpendingEntity.toDomain(): CategorySpending {
    return CategorySpending(
        category = category,
        categoryLabel = categoryLabel,
        totalAmount = totalAmount.toDouble(),
        transactionCount = transactionCount,
        percentage = percentage.toDouble(),
        colorToken = colorToken
    )
}

fun WeeklySpendingEntity.toDomain(): WeeklySpendingPoint {
    return WeeklySpendingPoint(
        dayLabel = dayLabel,
        totalAmount = totalAmount.toDouble(),
        dayIndex = dayIndex
    )
}

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        description = "description",
        date = date,
        note = note,
        type = when (type.name.uppercase()) {
            "INCOME" -> TransactionType.INCOME
            "TRANSFER" -> TransactionType.TRANSFER
            else -> TransactionType.EXPENSE
        },
        currency = Currency.UZS,
        exchangeRate = exchangeRate,
    )
}
