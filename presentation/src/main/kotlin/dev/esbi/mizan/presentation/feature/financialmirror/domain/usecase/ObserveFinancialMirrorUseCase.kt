package dev.esbi.mizan.presentation.feature.financialmirror.domain.usecase

import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.FinancialMirrorSummary
import dev.esbi.mizan.presentation.feature.financialmirror.domain.repository.FinancialMirrorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to observe financial mirror data
 * Returns Flow of complete financial mirror summary
 */
class ObserveFinancialMirrorUseCase @Inject constructor(
    private val repository: FinancialMirrorRepository
) {
    operator fun invoke(): Flow<FinancialMirrorSummary> {
        return repository.observeFinancialMirror()
    }
}
