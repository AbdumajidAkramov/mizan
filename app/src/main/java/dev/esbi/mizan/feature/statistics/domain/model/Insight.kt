package dev.esbi.mizan.feature.statistics.domain.model

/**
 * AI-generated insight about spending patterns
 */
data class Insight(
    val id: String,
    val title: String,
    val description: String,
    val type: InsightType
)

enum class InsightType {
    POSITIVE,
    WARNING,
    INFO
}
