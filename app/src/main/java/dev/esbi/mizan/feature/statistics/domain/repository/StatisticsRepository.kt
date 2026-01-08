package dev.esbi.mizan.feature.statistics.domain.repository

import dev.esbi.mizan.feature.statistics.domain.model.StatisticsSummary
import dev.esbi.mizan.feature.statistics.domain.model.TimePeriod
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Statistics feature
 */
interface StatisticsRepository {
    
    /**
     * Observe statistics for a given period
     */
    fun observeStatistics(period: TimePeriod): Flow<StatisticsSummary>
    
    /**
     * Refresh statistics data
     */
    suspend fun refreshStatistics()
}
