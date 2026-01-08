package dev.esbi.mizan.feature.statistics.domain.model

/**
 * Complete statistics summary for a given period
 */
data class StatisticsSummary(
    val period: TimePeriod,
    val periodLabel: String,
    val comparison: PeriodComparison,
    val spendingTrend: List<SpendingTrendPoint>,
    val categoryBreakdown: List<CategoryBreakdown>,
    val insights: List<Insight>
)
