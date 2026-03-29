package dev.esbi.mizan.presentation.feature.dashboard.domain.usecase

import dev.esbi.mizan.presentation.feature.dashboard.domain.repository.DashboardRepository
import javax.inject.Inject

class RefreshDashboardUseCase @Inject constructor(
    private val repository: DashboardRepository
) {
    suspend operator fun invoke() {
        repository.refreshDashboard()
    }
}
