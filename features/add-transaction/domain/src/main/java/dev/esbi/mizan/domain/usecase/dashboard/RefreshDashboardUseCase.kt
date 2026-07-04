package dev.esbi.mizan.domain.usecase.dashboard

import dev.esbi.mizan.domain.repository.DashboardRepository
import javax.inject.Inject

class RefreshDashboardUseCase @Inject constructor(
    private val repository: DashboardRepository
) {
    suspend operator fun invoke() {
        repository.refreshDashboard()
    }
}
