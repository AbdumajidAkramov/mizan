package dev.esbi.mizan.feature.budget.data.mapper

import dev.esbi.mizan.data.local.entity.CategoryBudgetEntity
import dev.esbi.mizan.feature.budget.domain.model.CategoryBudget
import dev.esbi.mizan.feature.budget.domain.model.calculateBudgetStatus

fun CategoryBudgetEntity.toDomain(): CategoryBudget {
    val percentage = if (budgetAmount > java.math.BigDecimal.ZERO) {
        ((spentAmount.toDouble() / budgetAmount.toDouble()) * 100).toInt()
    } else {
        0
    }

    return CategoryBudget(
        categoryId = categoryId,
        categoryName = categoryName,
        budgetAmount = budgetAmount.toDouble(),
        spentAmount = spentAmount.toDouble(),
        percentage = percentage,
        status = calculateBudgetStatus(percentage)
    )
}

fun CategoryBudget.toEntity(): CategoryBudgetEntity {
    return CategoryBudgetEntity(
        categoryId = categoryId,
        categoryName = categoryName,
        budgetAmount = java.math.BigDecimal.valueOf(budgetAmount),
        spentAmount = java.math.BigDecimal.valueOf(spentAmount)
    )
}
