package dev.esbi.mizan.feature.statistics.domain.usecase

import dev.esbi.mizan.feature.statistics.domain.model.StatisticsSummary
import dev.esbi.mizan.feature.statistics.domain.model.TimePeriod
import dev.esbi.mizan.feature.statistics.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to observe statistics for a given period
 */
class ObserveStatisticsUseCase @Inject constructor(
    private val repository: StatisticsRepository
) {
    operator fun invoke(period: TimePeriod): Flow<StatisticsSummary> {
        return repository.observeStatistics(period)
    }
}
