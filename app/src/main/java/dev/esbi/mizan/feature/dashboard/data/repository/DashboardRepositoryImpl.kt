package dev.esbi.mizan.feature.dashboard.data.repository

import dev.esbi.mizan.data.local.dao.DashboardDao
import dev.esbi.mizan.data.local.entity.CategorySpendingEntity
import dev.esbi.mizan.data.local.entity.DashboardSummaryEntity
import dev.esbi.mizan.data.local.entity.TransactionEntity
import dev.esbi.mizan.data.local.entity.WeeklySpendingEntity
import dev.esbi.mizan.di.IoDispatcher
import dev.esbi.mizan.feature.dashboard.data.mapper.toDomain
import dev.esbi.mizan.feature.dashboard.domain.model.DashboardSummary
import dev.esbi.mizan.feature.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
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
        val mockSummary = DashboardSummaryEntity(
            id = 1,
            totalBalance = 12450.75,
            monthlyExpenses = 2340.50,
            monthlySavings = 1200.00,
            budgetLimit = 3000.00,
            budgetPercentageUsed = 78.02
        )
        
        val mockCategories = listOf(
            CategorySpendingEntity(
                category = "food",
                categoryLabel = "Food & Dining",
                totalAmount = 680.50,
                transactionCount = 24,
                percentage = 29.0,
                colorToken = "#FF6B9D"
            ),
            CategorySpendingEntity(
                category = "transport",
                categoryLabel = "Transport",
                totalAmount = 450.00,
                transactionCount = 12,
                percentage = 19.2,
                colorToken = "#4FACFE"
            ),
            CategorySpendingEntity(
                category = "shopping",
                categoryLabel = "Shopping",
                totalAmount = 520.00,
                transactionCount = 8,
                percentage = 22.2,
                colorToken = "#FFA34D"
            ),
            CategorySpendingEntity(
                category = "bills",
                categoryLabel = "Bills & Utilities",
                totalAmount = 690.00,
                transactionCount = 5,
                percentage = 29.5,
                colorToken = "#00D2FF"
            )
        )
        
        val mockWeeklySpending = listOf(
            WeeklySpendingEntity("Mon", 120.0, 0),
            WeeklySpendingEntity("Tue", 180.0, 1),
            WeeklySpendingEntity("Wed", 95.0, 2),
            WeeklySpendingEntity("Thu", 210.0, 3),
            WeeklySpendingEntity("Fri", 165.0, 4),
            WeeklySpendingEntity("Sat", 240.0, 5),
            WeeklySpendingEntity("Sun", 145.0, 6)
        )
        
        val calendar = Calendar.getInstance()
        val mockTransactions = listOf(
            TransactionEntity(
                id = "1",
                amount = 45.50,
                category = "food",
                categoryLabel = "Food & Dining",
                description = "Lunch at Cafe",
                date = calendar.time,
                type = "EXPENSE",
                colorToken = "#FF6B9D"
            ),
            TransactionEntity(
                id = "2",
                amount = 120.00,
                category = "shopping",
                categoryLabel = "Shopping",
                description = "New Shoes",
                date = Date(calendar.timeInMillis - 86400000),
                type = "EXPENSE",
                colorToken = "#FFA34D"
            ),
            TransactionEntity(
                id = "3",
                amount = 3500.00,
                category = "income",
                categoryLabel = "Income",
                description = "Monthly Salary",
                date = Date(calendar.timeInMillis - 172800000),
                type = "INCOME",
                colorToken = "#00F2FE"
            ),
            TransactionEntity(
                id = "4",
                amount = 85.00,
                category = "transport",
                categoryLabel = "Transport",
                description = "Gas Station",
                date = Date(calendar.timeInMillis - 259200000),
                type = "EXPENSE",
                colorToken = "#4FACFE"
            ),
            TransactionEntity(
                id = "5",
                amount = 250.00,
                category = "bills",
                categoryLabel = "Bills & Utilities",
                description = "Electricity Bill",
                date = Date(calendar.timeInMillis - 345600000),
                type = "EXPENSE",
                colorToken = "#00D2FF"
            )
        )
        
        dashboardDao.insertDashboardSummary(mockSummary)
        
        dashboardDao.clearCategories()
        dashboardDao.insertCategories(mockCategories)
        
        dashboardDao.clearWeeklySpending()
        dashboardDao.insertWeeklySpending(mockWeeklySpending)
        
        dashboardDao.clearTransactions()
        dashboardDao.insertTransactions(mockTransactions)
    }
}
