package dev.esbi.mizan.feature.statistics.domain.usecase

import dev.esbi.mizan.feature.statistics.domain.repository.StatisticsRepository
import javax.inject.Inject

/**
 * Use case to refresh statistics data
 */
class RefreshStatisticsUseCase @Inject constructor(
    private val repository: StatisticsRepository
) {
    suspend operator fun invoke() {
        repository.refreshStatistics()
    }
}
