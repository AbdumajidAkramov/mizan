package dev.esbi.mizan.presentation.feature.dashboard.domain.usecase

import dev.esbi.mizan.presentation.feature.dashboard.domain.model.DashboardSummary
import dev.esbi.mizan.presentation.feature.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDashboardSummaryUseCase @Inject constructor(
    private val repository: DashboardRepository
) {
    operator fun invoke(): Flow<DashboardSummary?> {
        return repository.observeDashboardSummary()
    }
}
