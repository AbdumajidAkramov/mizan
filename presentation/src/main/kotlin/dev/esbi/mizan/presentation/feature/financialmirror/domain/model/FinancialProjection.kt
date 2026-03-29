package dev.esbi.mizan.presentation.feature.financialmirror.domain.model

/**
 * Net Worth Projection Data Point
 * Represents projected net worth for a specific year with three scenarios
 */
data class FinancialProjection(
    val year: String,
    val conservative: Double,
    val realistic: Double,
    val optimistic: Double
)

/**
 * Complete projection dataset with metadata
 */
data class ProjectionData(
    val projections: List<FinancialProjection>,
    val currentNetWorth: Double,
    val projectionYears: Int = 5
)
