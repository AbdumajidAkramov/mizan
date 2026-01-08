package dev.esbi.mizan.feature.budget.domain.usecase

import dev.esbi.mizan.feature.budget.domain.repository.BudgetRepository
import javax.inject.Inject

/**
 * Use case to delete category budget
 */
class DeleteCategoryBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(categoryId: String) {
        repository.deleteCategoryBudget(categoryId)
    }
}
