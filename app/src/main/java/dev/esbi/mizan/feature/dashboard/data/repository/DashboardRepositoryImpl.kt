package dev.esbi.mizan.feature.dashboard.data.repository

import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.data.local.dao.DashboardDao
import dev.esbi.mizan.data.local.entity.DashboardSummaryEntity
import dev.esbi.mizan.domain.model.dashboard.DashboardSummary
import dev.esbi.mizan.domain.repository.DashboardRepository
import dev.esbi.mizan.feature.dashboard.data.mapper.toDomain
import dev.esbi.mizan.presentation.di.IoDispatcher
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
            .fold(java.math.BigDecimal.ZERO) { acc, it -> 
                acc.add(it.account.balance.multiply(it.currency.rateToBase))
            }

        val existing = dashboardDao.observeDashboardSummary().first()

        dashboardDao.insertDashboardSummary(
            DashboardSummaryEntity(
                id = 1,
                totalBalance =  totalBalanceBase,
                monthlyExpenses = existing?.monthlyExpenses ?: java.math.BigDecimal.ZERO,
                monthlySavings = existing?.monthlySavings ?: java.math.BigDecimal.ZERO,
                budgetLimit = existing?.budgetLimit ?: java.math.BigDecimal.ZERO,
                budgetPercentageUsed = existing?.budgetPercentageUsed ?: java.math.BigDecimal.ZERO,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }
}
