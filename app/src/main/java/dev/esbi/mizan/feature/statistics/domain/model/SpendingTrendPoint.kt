package dev.esbi.mizan.feature.statistics.domain.model

/**
 * Single data point for spending trend chart
 */
data class SpendingTrendPoint(
    val label: String,
    val amount: Double,
    val timestamp: Long
)
