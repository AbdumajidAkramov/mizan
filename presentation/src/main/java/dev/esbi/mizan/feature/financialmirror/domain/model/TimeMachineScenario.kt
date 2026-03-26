package dev.esbi.mizan.feature.financialmirror.domain.model

/**
 * Time Machine "What If" Scenario
 * Represents a hypothetical financial scenario with projected impact
 */
data class TimeMachineScenario(
    val id: String,
    val title: String,
    val timeline: String,
    val impact: String,
    val description: String,
    val iconType: ScenarioIconType
)

enum class ScenarioIconType {
    TARGET,
    TRENDING_UP,
    SPARKLES,
    DOLLAR_SIGN,
    PIGGY_BANK
}
