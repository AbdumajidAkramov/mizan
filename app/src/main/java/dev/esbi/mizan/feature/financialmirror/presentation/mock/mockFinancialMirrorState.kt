package dev.esbi.mizan.feature.financialmirror.presentation.mock

import dev.esbi.mizan.feature.financialmirror.domain.model.AIRecommendation
import dev.esbi.mizan.feature.financialmirror.domain.model.FinancialMirrorSummary
import dev.esbi.mizan.feature.financialmirror.domain.model.FinancialProjection
import dev.esbi.mizan.feature.financialmirror.domain.model.InvestmentOpportunity
import dev.esbi.mizan.feature.financialmirror.domain.model.InvestmentType
import dev.esbi.mizan.feature.financialmirror.domain.model.ProjectionData
import dev.esbi.mizan.feature.financialmirror.domain.model.RiskAnalysis
import dev.esbi.mizan.feature.financialmirror.domain.model.RiskFactor
import dev.esbi.mizan.feature.financialmirror.domain.model.RiskStatus
import dev.esbi.mizan.feature.financialmirror.domain.model.ScenarioIconType
import dev.esbi.mizan.feature.financialmirror.domain.model.TimeMachineScenario
import dev.esbi.mizan.feature.financialmirror.presentation.store.FinancialMirrorStore

val mockFinancialMirrorState = FinancialMirrorStore.State(
    isLoading = false,
    financialMirrorData = FinancialMirrorSummary(
        projectionData = ProjectionData(
            currentNetWorth = 125_000.0,
            projectionYears = 5,
            projections = listOf(
                FinancialProjection(
                    year = "2025",
                    conservative = 135_000.0,
                    realistic = 150_000.0,
                    optimistic = 170_000.0
                ),
                FinancialProjection(
                    year = "2026",
                    conservative = 150_000.0,
                    realistic = 180_000.0,
                    optimistic = 215_000.0
                ),
                FinancialProjection(
                    year = "2027",
                    conservative = 165_000.0,
                    realistic = 215_000.0,
                    optimistic = 265_000.0
                ),
                FinancialProjection(
                    year = "2028",
                    conservative = 185_000.0,
                    realistic = 255_000.0,
                    optimistic = 330_000.0
                ),
                FinancialProjection(
                    year = "2029",
                    conservative = 205_000.0,
                    realistic = 300_000.0,
                    optimistic = 410_000.0
                )
            )
        ),

        riskAnalysis = RiskAnalysis(
            overallScore = 72,
            overallStatus = RiskStatus.FAIR,
            riskFactors = listOf(
                RiskFactor(
                    category = "Emergency Fund",
                    score = 85,
                    status = RiskStatus.GOOD,
                    description = "Emergency fund covers 6 months of expenses."
                ),
                RiskFactor(
                    category = "Income Stability",
                    score = 70,
                    status = RiskStatus.FAIR,
                    description = "Primary income is stable, secondary income is inconsistent."
                ),
                RiskFactor(
                    category = "Debt Load",
                    score = 55,
                    status = RiskStatus.WARNING,
                    description = "Debt-to-income ratio is higher than recommended."
                ),
                RiskFactor(
                    category = "Investment Diversification",
                    score = 78,
                    status = RiskStatus.GOOD,
                    description = "Portfolio is diversified across multiple asset classes."
                )
            )
        ),

        timeMachineScenarios = listOf(
            TimeMachineScenario(
                id = "tm_1",
                title = "Buy Your First Home",
                timeline = "3–5 years",
                impact = "+ Stability, + Net Worth",
                description = "Saving consistently could allow you to make a down payment on your first home.",
                iconType = ScenarioIconType.PIGGY_BANK
            ),
            TimeMachineScenario(
                id = "tm_2",
                title = "Reach $1M Net Worth",
                timeline = "8–10 years",
                impact = "+ Financial Freedom",
                description = "Aggressive investing and controlled expenses may help you reach millionaire status.",
                iconType = ScenarioIconType.TARGET
            ),
            TimeMachineScenario(
                id = "tm_3",
                title = "Launch Side Business",
                timeline = "1–2 years",
                impact = "+ Income Growth",
                description = "Launching a side business could significantly increase monthly cash flow.",
                iconType = ScenarioIconType.TRENDING_UP
            )
        ),

        investmentOpportunities = listOf(
            InvestmentOpportunity(
                id = "inv_1",
                title = "Government Bonds",
                type = InvestmentType.LOW_RISK,
                apy = "6.5%",
                minAmount = "$500",
                description = "Stable returns with minimal risk, suitable for capital preservation.",
                colorHex = "#4CAF50"
            ),
            InvestmentOpportunity(
                id = "inv_2",
                title = "Index Funds",
                type = InvestmentType.MEDIUM_RISK,
                apy = "9–11%",
                minAmount = "$1,000",
                description = "Balanced growth through diversified stock market exposure.",
                colorHex = "#2196F3"
            ),
            InvestmentOpportunity(
                id = "inv_3",
                title = "Tech Stocks",
                type = InvestmentType.HIGH_RISK,
                apy = "15%+",
                minAmount = "$2,000",
                description = "High growth potential with increased volatility.",
                colorHex = "#FF9800"
            ),
            InvestmentOpportunity(
                id = "inv_4",
                title = "Retirement Fund",
                type = InvestmentType.LONG_TERM,
                apy = "10–12%",
                minAmount = "$3,000",
                description = "Long-term investment focused on retirement security.",
                colorHex = "#9C27B0"
            )
        ),

        aiRecommendation = AIRecommendation(
            title = "AI Financial Insight",
            description = "Your financial health is improving, but there are key areas to optimize.",
            recommendations = listOf(
                "Increase emergency fund to cover at least 9 months of expenses.",
                "Reduce high-interest debt before increasing high-risk investments.",
                "Allocate more funds toward long-term retirement assets.",
                "Maintain consistent monthly investments to benefit from compound growth."
            )
        )
    ),
    error = null,
    selectedProjectionView = FinancialMirrorStore.ProjectionView.REALISTIC
)