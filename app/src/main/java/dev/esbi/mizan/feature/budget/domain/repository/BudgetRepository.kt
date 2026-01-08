package dev.esbi.mizan.feature.budget.domain.repository

import dev.esbi.mizan.feature.budget.domain.model.BudgetSummary
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Budget feature
 * Provides access to budget data and operations
 */
interface BudgetRepository {
    
    /**
     * Observe complete budget summary
     */
    fun observeBudgetSummary(): Flow<BudgetSummary>
    
    /**
     * Update budget amount for a category
     */
    suspend fun updateCategoryBudget(categoryId: String, budgetAmount: Double)
    
    /**
     * Create new budget for a category
     */
    suspend fun createCategoryBudget(categoryId: String, categoryName: String, budgetAmount: Double)
    
    /**
     * Delete budget for a category
     */
    suspend fun deleteCategoryBudget(categoryId: String)
    
    /**
     * Refresh budget data
     */
    suspend fun refreshBudgets()
}
