package dev.esbi.mizan.feature.budget.domain.usecase

import dev.esbi.mizan.feature.budget.domain.repository.BudgetRepository
import javax.inject.Inject

/**
 * Use case to refresh budget data
 */
class RefreshBudgetsUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke() {
        repository.refreshBudgets()
    }
}
