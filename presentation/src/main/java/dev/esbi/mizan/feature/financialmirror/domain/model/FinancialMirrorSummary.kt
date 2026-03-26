package dev.esbi.mizan.feature.financialmirror.domain.model

/**
 * Complete Financial Mirror Data
 * Aggregates all financial mirror components
 */
data class FinancialMirrorSummary(
    val projectionData: ProjectionData,
    val riskAnalysis: RiskAnalysis,
    val timeMachineScenarios: List<TimeMachineScenario>,
    val investmentOpportunities: List<InvestmentOpportunity>,
    val aiRecommendation: AIRecommendation
)
