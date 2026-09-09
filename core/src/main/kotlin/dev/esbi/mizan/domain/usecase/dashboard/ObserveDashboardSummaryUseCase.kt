package dev.esbi.mizan.domain.usecase.dashboard

import dev.esbi.mizan.domain.model.dashboard.DashboardSummary
import dev.esbi.mizan.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDashboardSummaryUseCase @Inject constructor(
    private val repository: DashboardRepository
) {
    operator fun invoke(): Flow<DashboardSummary?> {
        return repository.observeDashboardSummary()
    }
}
