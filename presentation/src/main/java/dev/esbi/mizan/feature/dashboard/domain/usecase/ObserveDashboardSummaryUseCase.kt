package dev.esbi.mizan.feature.dashboard.domain.usecase

import dev.esbi.mizan.feature.dashboard.domain.model.DashboardSummary
import dev.esbi.mizan.feature.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDashboardSummaryUseCase @Inject constructor(
    private val repository: DashboardRepository
) {
    operator fun invoke(): Flow<DashboardSummary?> {
        return repository.observeDashboardSummary()
    }
}
