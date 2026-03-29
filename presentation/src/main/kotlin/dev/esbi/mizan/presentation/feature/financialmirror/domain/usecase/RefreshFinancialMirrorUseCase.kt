package dev.esbi.mizan.presentation.feature.financialmirror.domain.usecase

import dev.esbi.mizan.presentation.feature.financialmirror.domain.repository.FinancialMirrorRepository
import javax.inject.Inject

/**
 * Use case to refresh financial mirror data
 * Triggers recalculation of projections and risk analysis
 */
class RefreshFinancialMirrorUseCase @Inject constructor(
    private val repository: FinancialMirrorRepository
) {
    suspend operator fun invoke() {
        repository.refreshFinancialMirror()
    }
}
