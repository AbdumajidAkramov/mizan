package dev.esbi.mizan.feature.budget.data.mapper

import dev.esbi.mizan.data.local.entity.CategoryBudgetEntity
import dev.esbi.mizan.feature.budget.domain.model.CategoryBudget
import dev.esbi.mizan.feature.budget.domain.model.calculateBudgetStatus

fun CategoryBudgetEntity.toDomain(): CategoryBudget {
    val percentage = if (budgetAmount > 0) {
        ((spentAmount / budgetAmount) * 100).toInt()
    } else {
        0
    }

    return CategoryBudget(
        categoryId = categoryId,
        categoryName = categoryName,
        budgetAmount = budgetAmount,
        spentAmount = spentAmount,
        percentage = percentage,
        status = calculateBudgetStatus(percentage)
    )
}

fun CategoryBudget.toEntity(): CategoryBudgetEntity {
    return CategoryBudgetEntity(
        categoryId = categoryId,
        categoryName = categoryName,
        budgetAmount = budgetAmount,
        spentAmount = spentAmount
    )
}
