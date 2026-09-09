package dev.esbi.mizan.presentation.feature.financialmirror.domain.model

/**
 * Financial Risk Factor
 * Represents a specific risk category with score and status
 */
data class RiskFactor(
    val category: String,
    val score: Int,
    val status: RiskStatus,
    val description: String
)

enum class RiskStatus {
    GOOD,
    FAIR,
    WARNING
}

/**
 * Complete risk analysis with overall score
 */
data class RiskAnalysis(
    val riskFactors: List<RiskFactor>,
    val overallScore: Int,
    val overallStatus: RiskStatus
)
