package dev.esbi.mizan.presentation.feature.financialmirror.domain.model

/**
 * AI-Curated Investment Opportunity
 * Represents an investment option with risk level and returns
 */
data class InvestmentOpportunity(
    val id: String,
    val title: String,
    val type: InvestmentType,
    val apy: String,
    val minAmount: String,
    val description: String,
    val colorHex: String
)

enum class InvestmentType {
    LOW_RISK,
    MEDIUM_RISK,
    HIGH_RISK,
    LONG_TERM
}
