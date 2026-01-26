package dev.esbi.mizan.feature.dashboard.data.repository

import dev.esbi.mizan.data.local.dao.DashboardDao
import dev.esbi.mizan.di.IoDispatcher
import dev.esbi.mizan.feature.dashboard.data.mapper.toDomain
import dev.esbi.mizan.feature.dashboard.domain.model.DashboardSummary
import dev.esbi.mizan.feature.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val dashboardDao: DashboardDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DashboardRepository {

    override fun observeDashboardSummary(): Flow<DashboardSummary?> {
        return combine(
            dashboardDao.observeDashboardSummary(),
            dashboardDao.observeTopCategories(),
            dashboardDao.observeWeeklySpending(),
            dashboardDao.observeRecentTransactions()
        ) { summary, categories, weeklySpending, transactions ->
            summary?.toDomain(
                categories = categories.map { it.toDomain() },
                weeklySpending = weeklySpending.map { it.toDomain() },
                transactions = transactions.map { it.toDomain() }
            )
        }
    }

    override suspend fun refreshDashboard() = withContext(ioDispatcher) {
    }
}
