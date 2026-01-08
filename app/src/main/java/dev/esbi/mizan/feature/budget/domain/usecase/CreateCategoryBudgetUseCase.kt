package dev.esbi.mizan.feature.budget.domain.usecase

import dev.esbi.mizan.feature.budget.domain.repository.BudgetRepository
import javax.inject.Inject

/**
 * Use case to create new category budget
 */
class CreateCategoryBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(categoryId: String, categoryName: String, budgetAmount: Double) {
        repository.createCategoryBudget(categoryId, categoryName, budgetAmount)
    }
}
