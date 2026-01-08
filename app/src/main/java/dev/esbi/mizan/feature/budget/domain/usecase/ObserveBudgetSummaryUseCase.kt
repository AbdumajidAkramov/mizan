package dev.esbi.mizan.feature.budget.domain.usecase

import dev.esbi.mizan.feature.budget.domain.model.BudgetSummary
import dev.esbi.mizan.feature.budget.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to observe budget summary
 * Returns Flow of complete budget data with all categories
 */
class ObserveBudgetSummaryUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    operator fun invoke(): Flow<BudgetSummary> {
        return repository.observeBudgetSummary()
    }
}
