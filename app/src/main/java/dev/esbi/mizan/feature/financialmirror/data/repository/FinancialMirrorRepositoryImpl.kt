package dev.esbi.mizan.feature.financialmirror.data.repository

import dev.esbi.mizan.data.local.dao.FinancialMirrorDao
import dev.esbi.mizan.feature.financialmirror.data.mapper.toDomain
import dev.esbi.mizan.feature.financialmirror.data.mapper.toEntity
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.AIRecommendation
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.FinancialMirrorSummary
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.FinancialProjection
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.InvestmentOpportunity
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.InvestmentType
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.ProjectionData
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.RiskAnalysis
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.RiskFactor
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.RiskStatus
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.ScenarioIconType
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.TimeMachineScenario
import dev.esbi.mizan.presentation.feature.financialmirror.domain.repository.FinancialMirrorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Implementation of FinancialMirrorRepository
 * Manages financial projections, risk analysis, scenarios, and AI recommendations
 * Data source: Room database with mock data generation
 */
class FinancialMirrorRepositoryImpl @Inject constructor(
    private val dao: FinancialMirrorDao
) : FinancialMirrorRepository {

    override fun observeFinancialMirror(): Flow<FinancialMirrorSummary> {
        return combine(
            dao.observeProjections(),
            dao.observeRiskFactors(),
            dao.observeScenarios(),
            dao.observeOpportunities()
        ) { projections, riskFactors, scenarios, opportunities ->

            val projectionData = if (projections.isEmpty()) {
                generateMockProjectionData()
            } else {
                ProjectionData(
                    projections = projections.map { it.toDomain() },
                    currentNetWorth = 22450.0,
                    projectionYears = 5
                )
            }

            val riskAnalysis = if (riskFactors.isEmpty()) {
                generateMockRiskAnalysis()
            } else {
                val domainRiskFactors = riskFactors.map { it.toDomain() }
                val avgScore = domainRiskFactors.map { it.score }.average().toInt()
                RiskAnalysis(
                    riskFactors = domainRiskFactors,
                    overallScore = avgScore,
                    overallStatus = when {
                        avgScore >= 80 -> RiskStatus.GOOD
                        avgScore >= 60 -> RiskStatus.FAIR
                        else -> RiskStatus.WARNING
                    }
                )
            }

            val timeMachineScenarios = if (scenarios.isEmpty()) {
                generateMockScenarios()
            } else {
                scenarios.map { it.toDomain() }
            }

            val investmentOpportunities = if (opportunities.isEmpty()) {
                generateMockOpportunities()
            } else {
                opportunities.map { it.toDomain() }
            }

            FinancialMirrorSummary(
                projectionData = projectionData,
                riskAnalysis = riskAnalysis,
                timeMachineScenarios = timeMachineScenarios,
                investmentOpportunities = investmentOpportunities,
                aiRecommendation = generateAIRecommendation()
            )
        }
    }

    override suspend fun refreshFinancialMirror() {
        dao.clearProjections()
        dao.clearRiskFactors()
        dao.clearScenarios()
        dao.clearOpportunities()

        val projections = generateMockProjectionData().projections.map { it.toEntity() }
        val riskFactors = generateMockRiskAnalysis().riskFactors.map { it.toEntity() }
        val scenarios = generateMockScenarios().map { it.toEntity() }
        val opportunities = generateMockOpportunities().map { it.toEntity() }

        dao.insertProjections(projections)
        dao.insertRiskFactors(riskFactors)
        dao.insertScenarios(scenarios)
        dao.insertOpportunities(opportunities)
    }

    private fun generateMockProjectionData(): ProjectionData {
        return ProjectionData(
            projections = listOf(
                FinancialProjection("2026", 22450.0, 22450.0, 22450.0),
                FinancialProjection("2027", 28500.0, 32400.0, 38200.0),
                FinancialProjection("2028", 34200.0, 45800.0, 62500.0),
                FinancialProjection("2029", 39800.0, 62100.0, 95800.0),
                FinancialProjection("2030", 45200.0, 82500.0, 142000.0),
                FinancialProjection("2031", 50400.0, 108200.0, 208500.0)
            ),
            currentNetWorth = 22450.0,
            projectionYears = 5
        )
    }

    private fun generateMockRiskAnalysis(): RiskAnalysis {
        val riskFactors = listOf(
            RiskFactor(
                category = "Emergency Fund",
                score = 85,
                status = RiskStatus.GOOD,
                description = "Strong 6-month coverage"
            ),
            RiskFactor(
                category = "Debt-to-Income",
                score = 72,
                status = RiskStatus.FAIR,
                description = "Manageable debt levels"
            ),
            RiskFactor(
                category = "Diversification",
                score = 45,
                status = RiskStatus.WARNING,
                description = "Needs improvement"
            ),
            RiskFactor(
                category = "Insurance Coverage",
                score = 90,
                status = RiskStatus.GOOD,
                description = "Well protected"
            )
        )

        val avgScore = riskFactors.map { it.score }.average().toInt()

        return RiskAnalysis(
            riskFactors = riskFactors,
            overallScore = avgScore,
            overallStatus = RiskStatus.FAIR
        )
    }

    private fun generateMockScenarios(): List<TimeMachineScenario> {
        return listOf(
            TimeMachineScenario(
                id = "1",
                title = "If you saved $500/month",
                timeline = "5 years",
                impact = "+$38,250",
                description = "With 5% annual return",
                iconType = ScenarioIconType.TARGET
            ),
            TimeMachineScenario(
                id = "2",
                title = "Cut subscriptions by 50%",
                timeline = "1 year",
                impact = "+$282",
                description = "Save $23.50 monthly",
                iconType = ScenarioIconType.TRENDING_UP
            ),
            TimeMachineScenario(
                id = "3",
                title = "Invest $200/month in index",
                timeline = "10 years",
                impact = "+$32,840",
                description = "Assuming 7% return",
                iconType = ScenarioIconType.SPARKLES
            )
        )
    }

    private fun generateMockOpportunities(): List<InvestmentOpportunity> {
        return listOf(
            InvestmentOpportunity(
                id = "1",
                title = "High-Yield Savings",
                type = InvestmentType.LOW_RISK,
                apy = "4.5%",
                minAmount = "$100",
                description = "FDIC insured, instant access",
                colorHex = "#00F2FE"
            ),
            InvestmentOpportunity(
                id = "2",
                title = "Index Fund ETF",
                type = InvestmentType.MEDIUM_RISK,
                apy = "7-10%",
                minAmount = "$500",
                description = "Diversified market exposure",
                colorHex = "#4FACFE"
            ),
            InvestmentOpportunity(
                id = "3",
                title = "Retirement 401(k)",
                type = InvestmentType.LONG_TERM,
                apy = "8-12%",
                minAmount = "$50",
                description = "Employer match available",
                colorHex = "#667EEA"
            )
        )
    }

    private fun generateAIRecommendation(): AIRecommendation {
        return AIRecommendation(
            title = "AI Recommendation",
            description = "Based on your spending patterns and goals, we recommend:",
            recommendations = listOf(
                "Increase emergency fund by $200/month",
                "Start investing in index funds with $150/month",
                "Review and cancel unused subscriptions"
            )
        )
    }
}
