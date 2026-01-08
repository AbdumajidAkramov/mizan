package dev.esbi.mizan.feature.budget.domain.usecase

import dev.esbi.mizan.feature.budget.domain.repository.BudgetRepository
import javax.inject.Inject

/**
 * Use case to update category budget amount
 */
class UpdateCategoryBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(categoryId: String, budgetAmount: Double) {
        repository.updateCategoryBudget(categoryId, budgetAmount)
    }
}
