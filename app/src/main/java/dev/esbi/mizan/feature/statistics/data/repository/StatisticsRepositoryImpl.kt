package dev.esbi.mizan.feature.statistics.data.repository

import dev.esbi.mizan.feature.statistics.domain.model.CategoryBreakdown
import dev.esbi.mizan.feature.statistics.domain.model.Insight
import dev.esbi.mizan.feature.statistics.domain.model.InsightType
import dev.esbi.mizan.feature.statistics.domain.model.PeriodComparison
import dev.esbi.mizan.feature.statistics.domain.model.SpendingTrendPoint
import dev.esbi.mizan.feature.statistics.domain.model.StatisticsSummary
import dev.esbi.mizan.feature.statistics.domain.model.TimePeriod
import dev.esbi.mizan.feature.statistics.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

/**
 * Implementation of StatisticsRepository
 * Generates mock statistics data based on selected period
 */
class StatisticsRepositoryImpl @Inject constructor() : StatisticsRepository {

    override fun observeStatistics(period: TimePeriod): Flow<StatisticsSummary> = flow {
        emit(generateStatistics(period))
    }

    override suspend fun refreshStatistics() {
        // In a real app, this would fetch from API
    }

    private fun generateStatistics(period: TimePeriod): StatisticsSummary {
        return when (period) {
            TimePeriod.WEEK -> generateWeeklyStatistics()
            TimePeriod.MONTH -> generateMonthlyStatistics()
            TimePeriod.YEAR -> generateYearlyStatistics()
        }
    }

    private fun generateWeeklyStatistics(): StatisticsSummary {
        val calendar = Calendar.getInstance()
        val trendPoints = (0..6).map { dayOffset ->
            calendar.add(Calendar.DAY_OF_YEAR, -dayOffset)
            val label = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, dayOffset)
            
            SpendingTrendPoint(
                label = label,
                amount = (50..200).random().toDouble(),
                timestamp = calendar.timeInMillis - (dayOffset * 24 * 60 * 60 * 1000L)
            )
        }.reversed()

        return StatisticsSummary(
            period = TimePeriod.WEEK,
            periodLabel = "This Week",
            comparison = PeriodComparison(
                currentIncome = 850.0,
                previousIncome = 780.0,
                incomeChangePercentage = 8.97f,
                currentExpense = 687.0,
                previousExpense = 745.0,
                expenseChangePercentage = -7.79f
            ),
            spendingTrend = trendPoints,
            categoryBreakdown = generateCategoryBreakdown(687.0),
            insights = generateInsights()
        )
    }

    private fun generateMonthlyStatistics(): StatisticsSummary {
        val calendar = Calendar.getInstance()
        val trendPoints = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun").mapIndexed { index, month ->
            SpendingTrendPoint(
                label = month,
                amount = listOf(1450.0, 1820.0, 1650.0, 2100.0, 1880.0, 2350.0)[index],
                timestamp = calendar.timeInMillis - ((5 - index) * 30 * 24 * 60 * 60 * 1000L)
            )
        }

        return StatisticsSummary(
            period = TimePeriod.MONTH,
            periodLabel = "January 2026",
            comparison = PeriodComparison(
                currentIncome = 3850.0,
                previousIncome = 3420.0,
                incomeChangePercentage = 12.57f,
                currentExpense = 2350.0,
                previousExpense = 2565.0,
                expenseChangePercentage = -8.38f
            ),
            spendingTrend = trendPoints,
            categoryBreakdown = generateCategoryBreakdown(2350.0),
            insights = generateInsights()
        )
    }

    private fun generateYearlyStatistics(): StatisticsSummary {
        val calendar = Calendar.getInstance()
        val trendPoints = listOf("Q1", "Q2", "Q3", "Q4").mapIndexed { index, quarter ->
            SpendingTrendPoint(
                label = quarter,
                amount = listOf(5920.0, 6450.0, 6180.0, 7100.0)[index],
                timestamp = calendar.timeInMillis - ((3 - index) * 90 * 24 * 60 * 60 * 1000L)
            )
        }

        return StatisticsSummary(
            period = TimePeriod.YEAR,
            periodLabel = "2026",
            comparison = PeriodComparison(
                currentIncome = 45600.0,
                previousIncome = 42300.0,
                incomeChangePercentage = 7.80f,
                currentExpense = 25650.0,
                previousExpense = 28100.0,
                expenseChangePercentage = -8.72f
            ),
            spendingTrend = trendPoints,
            categoryBreakdown = generateCategoryBreakdown(25650.0),
            insights = generateInsights()
        )
    }

    private fun generateCategoryBreakdown(totalAmount: Double): List<CategoryBreakdown> {
        val categories = listOf(
            Triple("food", "Food & Dining", "#FF6B9D"),
            Triple("transport", "Transport", "#4FACFE"),
            Triple("shopping", "Shopping", "#FFA34D"),
            Triple("bills", "Bills & Utilities", "#00D2FF"),
            Triple("entertainment", "Entertainment", "#C471F5"),
            Triple("health", "Health & Fitness", "#FF6B6B")
        )

        val percentages = listOf(28f, 18f, 22f, 15f, 10f, 7f)
        
        return categories.mapIndexed { index, (id, name, color) ->
            val percentage = percentages[index]
            val amount = totalAmount * (percentage / 100f)
            
            CategoryBreakdown(
                categoryId = id,
                categoryName = name,
                amount = amount,
                percentage = percentage,
                color = color
            )
        }
    }

    private fun generateInsights(): List<Insight> {
        return listOf(
            Insight(
                id = UUID.randomUUID().toString(),
                title = "Your spending decreased by 8.3% this month",
                description = "Great job managing your expenses!",
                type = InsightType.POSITIVE
            ),
            Insight(
                id = UUID.randomUUID().toString(),
                title = "Bills category is 30% of total spending",
                description = "Consider reviewing your subscriptions",
                type = InsightType.WARNING
            ),
            Insight(
                id = UUID.randomUUID().toString(),
                title = "You saved $1,245 this month",
                description = "You're on track to meet your savings goal",
                type = InsightType.INFO
            )
        )
    }
}
