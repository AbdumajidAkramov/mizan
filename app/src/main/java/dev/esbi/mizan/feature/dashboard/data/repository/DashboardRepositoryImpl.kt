package dev.esbi.mizan.feature.dashboard.data.repository

import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.data.local.dao.DashboardDao
import dev.esbi.mizan.data.local.entity.DashboardSummaryEntity
import dev.esbi.mizan.feature.dashboard.data.mapper.toDomain
import dev.esbi.mizan.presentation.di.IoDispatcher
import dev.esbi.mizan.presentation.feature.dashboard.domain.model.DashboardSummary
import dev.esbi.mizan.presentation.feature.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val dashboardDao: DashboardDao,
    private val accountDao: AccountDao,
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
        val accounts = accountDao.getAllAccountsWithCurrency().first()

        val totalBalanceBase = accounts
            .filter { !it.account.excludeFromTotal }
            .sumOf { it.account.balance * it.currency.rateToBase }

        val existing = dashboardDao.observeDashboardSummary().first()

        dashboardDao.insertDashboardSummary(
            DashboardSummaryEntity(
                id = 1,
                totalBalance = totalBalanceBase,
                monthlyExpenses = existing?.monthlyExpenses ?: 0.0,
                monthlySavings = existing?.monthlySavings ?: 0.0,
                budgetLimit = existing?.budgetLimit ?: 0.0,
                budgetPercentageUsed = existing?.budgetPercentageUsed ?: 0.0,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }
}
