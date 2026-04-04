package dev.esbi.mizan.feature.budget.data.repository

import dev.esbi.mizan.data.local.dao.BudgetDao
import dev.esbi.mizan.data.local.entity.CategoryBudgetEntity
import dev.esbi.mizan.feature.budget.data.mapper.toDomain
import dev.esbi.mizan.feature.budget.domain.model.BudgetSummary
import dev.esbi.mizan.feature.budget.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of BudgetRepository
 * Manages category budgets with Room database
 */
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {

    override fun observeBudgetSummary(): Flow<BudgetSummary> {
        return budgetDao.observeAllBudgets().map { entities ->
            if (entities.isEmpty()) {
                // Return mock data if no budgets exist
                val mockBudgets = generateMockBudgets()
                budgetDao.insertBudgets(mockBudgets)
                calculateSummary(mockBudgets.map { it.toDomain() })
            } else {
                val categoryBudgets = entities.map { it.toDomain() }
                calculateSummary(categoryBudgets)
            }
        }
    }

    override suspend fun updateCategoryBudget(categoryId: String, budgetAmount: Double) {
        budgetDao.updateBudgetAmount(categoryId, java.math.BigDecimal.valueOf(budgetAmount))
    }

    override suspend fun createCategoryBudget(
        categoryId: String,
        categoryName: String,
        budgetAmount: Double
    ) {
        val budget = CategoryBudgetEntity(
            categoryId = categoryId,
            categoryName = categoryName,
            budgetAmount = java.math.BigDecimal.valueOf(budgetAmount),
            spentAmount = java.math.BigDecimal.ZERO
        )
        budgetDao.insertBudget(budget)
    }

    override suspend fun deleteCategoryBudget(categoryId: String) {
        budgetDao.deleteBudget(categoryId)
    }

    override suspend fun refreshBudgets() {
        // In a real app, this would fetch updated spent amounts from transactions
        // For now, we'll keep the existing data
    }

    private fun calculateSummary(categoryBudgets: List<dev.esbi.mizan.feature.budget.domain.model.CategoryBudget>): BudgetSummary {
        val totalBudget = categoryBudgets.sumOf { it.budgetAmount }
        val totalSpent = categoryBudgets.sumOf { it.spentAmount }
        val overallPercentage = if (totalBudget > 0) {
            ((totalSpent / totalBudget) * 100).toInt()
        } else {
            0
        }
        val remaining = totalBudget - totalSpent

        return BudgetSummary(
            categoryBudgets = categoryBudgets,
            totalBudget = totalBudget,
            totalSpent = totalSpent,
            overallPercentage = overallPercentage,
            remaining = remaining
        )
    }

    private fun generateMockBudgets(): List<CategoryBudgetEntity> {
        return listOf(
            CategoryBudgetEntity("food", "Food & Dining", java.math.BigDecimal("500.0"), java.math.BigDecimal("387.0")),
            CategoryBudgetEntity("transport", "Transport", java.math.BigDecimal("300.0"), java.math.BigDecimal("245.0")),
            CategoryBudgetEntity("shopping", "Shopping", java.math.BigDecimal("400.0"), java.math.BigDecimal("299.0")),
            CategoryBudgetEntity("bills", "Bills & Utilities", java.math.BigDecimal("250.0"), java.math.BigDecimal("204.0")),
            CategoryBudgetEntity("entertainment", "Entertainment", java.math.BigDecimal("200.0"), java.math.BigDecimal("156.0")),
            CategoryBudgetEntity("health", "Health & Fitness", java.math.BigDecimal("150.0"), java.math.BigDecimal("89.0"))
        )
    }
}
