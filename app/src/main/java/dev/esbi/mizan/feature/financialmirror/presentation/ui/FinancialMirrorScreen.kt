package dev.esbi.mizan.feature.financialmirror.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.financialmirror.domain.model.InvestmentType
import dev.esbi.mizan.feature.financialmirror.domain.model.RiskStatus
import dev.esbi.mizan.feature.financialmirror.domain.model.ScenarioIconType
import dev.esbi.mizan.feature.financialmirror.presentation.FinancialMirrorViewModel
import dev.esbi.mizan.feature.financialmirror.presentation.FinancialMirrorViewModelFactory
import dev.esbi.mizan.feature.financialmirror.presentation.store.FinancialMirrorStore
import dev.esbi.mizan.ui.animation.FadeInUpAnimation
import dev.esbi.mizan.ui.animation.StaggeredFadeInUp
import dev.esbi.mizan.ui.animation.animateProgressAsState
import dev.esbi.mizan.ui.components.ErrorState
import dev.esbi.mizan.ui.components.LoadingSkeleton
import dev.esbi.mizan.ui.components.PremiumButton
import dev.esbi.mizan.ui.components.PremiumButtonSize
import dev.esbi.mizan.ui.components.PremiumButtonVariant
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FinancialMirrorScreen(
    viewModelFactory: FinancialMirrorViewModelFactory,
    modifier: Modifier = Modifier
) {
    val viewModel: FinancialMirrorViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsState(initial = FinancialMirrorStore.State())

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is FinancialMirrorStore.Label.ShowError -> {
                    // Handle error display
                }
                is FinancialMirrorStore.Label.NavigateToInvestment -> {
                    // Handle navigation
                }
            }
        }
    }

    FinancialMirrorContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
private fun FinancialMirrorContent(
    state: FinancialMirrorStore.State,
    onIntent: (FinancialMirrorStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading && state.financialMirrorData == null -> {
            LoadingContent(modifier = modifier)
        }
        state.error != null && state.financialMirrorData == null -> {
            FadeInUpAnimation {
                ErrorState(
                    message = state.error,
                    onRetry = { onIntent(FinancialMirrorStore.Intent.Retry) },
                    modifier = modifier.fillMaxSize()
                )
            }
        }
        state.financialMirrorData != null -> {
            val data = state.financialMirrorData
            val numberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
                maximumFractionDigits = 0
            }

            FadeInUpAnimation {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Header
                    StaggeredFadeInUp(index = 0) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "✨",
                                    fontSize = 24.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.financial_mirror_title),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.financial_mirror_subtitle),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Net Worth Projection
                    StaggeredFadeInUp(index = 1) {
                        NetWorthProjectionCard(
                            projectionData = data.projectionData,
                            selectedView = state.selectedProjectionView,
                            onViewSelected = { view ->
                                onIntent(FinancialMirrorStore.Intent.SelectProjectionView(view))
                            },
                            numberFormat = numberFormat
                        )
                    }

                    // Risk Analysis
                    StaggeredFadeInUp(index = 2) {
                        RiskAnalysisCard(
                            riskAnalysis = data.riskAnalysis
                        )
                    }

                    // Time Machine
                    StaggeredFadeInUp(index = 3) {
                        TimeMachineSection(
                            scenarios = data.timeMachineScenarios
                        )
                    }

                    // Investment Opportunities
                    StaggeredFadeInUp(index = 4) {
                        InvestmentOpportunitiesSection(
                            opportunities = data.investmentOpportunities,
                            numberFormat = numberFormat
                        )
                    }

                    // AI Recommendation
                    StaggeredFadeInUp(index = 5) {
                        AIRecommendationCard(
                            recommendation = data.aiRecommendation
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NetWorthProjectionCard(
    projectionData: dev.esbi.mizan.feature.financialmirror.domain.model.ProjectionData,
    selectedView: FinancialMirrorStore.ProjectionView,
    onViewSelected: (FinancialMirrorStore.ProjectionView) -> Unit,
    numberFormat: NumberFormat
) {
    PremiumCard(variant = PremiumCardVariant.Glass) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.financial_mirror_net_worth_projection),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.financial_mirror_projection_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Projection Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    FinancialMirrorStore.ProjectionView.CONSERVATIVE to R.string.financial_mirror_conservative,
                    FinancialMirrorStore.ProjectionView.REALISTIC to R.string.financial_mirror_realistic,
                    FinancialMirrorStore.ProjectionView.OPTIMISTIC to R.string.financial_mirror_optimistic
                ).forEach { (view, stringRes) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (selectedView == view) {
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                                    )
                                } else {
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    )
                                }
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(stringRes),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (selectedView == view) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Simplified projection visualization (placeholder for chart)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📈 Projection Chart",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Projection Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.financial_mirror_starting),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = numberFormat.format(projectionData.currentNetWorth),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.financial_mirror_five_year_target),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val targetValue = when (selectedView) {
                        FinancialMirrorStore.ProjectionView.CONSERVATIVE -> 
                            projectionData.projections.lastOrNull()?.conservative ?: 0.0
                        FinancialMirrorStore.ProjectionView.REALISTIC -> 
                            projectionData.projections.lastOrNull()?.realistic ?: 0.0
                        FinancialMirrorStore.ProjectionView.OPTIMISTIC -> 
                            projectionData.projections.lastOrNull()?.optimistic ?: 0.0
                    }
                    Text(
                        text = numberFormat.format(targetValue),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF667EEA)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.financial_mirror_total_growth),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val targetValue = when (selectedView) {
                        FinancialMirrorStore.ProjectionView.CONSERVATIVE -> 
                            projectionData.projections.lastOrNull()?.conservative ?: 0.0
                        FinancialMirrorStore.ProjectionView.REALISTIC -> 
                            projectionData.projections.lastOrNull()?.realistic ?: 0.0
                        FinancialMirrorStore.ProjectionView.OPTIMISTIC -> 
                            projectionData.projections.lastOrNull()?.optimistic ?: 0.0
                    }
                    val growthPercent = ((targetValue - projectionData.currentNetWorth) / projectionData.currentNetWorth * 100).toInt()
                    Text(
                        text = "+$growthPercent%",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00F2FE)
                    )
                }
            }
        }
    }
}

@Composable
private fun RiskAnalysisCard(
    riskAnalysis: dev.esbi.mizan.feature.financialmirror.domain.model.RiskAnalysis
) {
    PremiumCard(variant = PremiumCardVariant.Glass) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🛡️",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.financial_mirror_risk_assessment),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            riskAnalysis.riskFactors.forEach { risk ->
                val color = when (risk.status) {
                    RiskStatus.GOOD -> Color(0xFF00F2FE)
                    RiskStatus.FAIR -> Color(0xFF4FACFE)
                    RiskStatus.WARNING -> Color(0xFFFEE140)
                }

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = risk.category,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = risk.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = risk.score.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = color
                            )
                            Text(
                                text = stringResource(R.string.financial_mirror_out_of_100),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val animatedProgress by animateProgressAsState(
                        targetProgress = risk.score / 100f
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = animatedProgress)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(color)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Overall Risk Score
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.financial_mirror_overall_risk_score),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${riskAnalysis.overallScore}/100",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF4FACFE).copy(alpha = 0.2f))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.financial_mirror_risk_moderate),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4FACFE)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeMachineSection(
    scenarios: List<dev.esbi.mizan.feature.financialmirror.domain.model.TimeMachineScenario>
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "⏰",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.financial_mirror_time_machine),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.financial_mirror_what_if),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        scenarios.forEachIndexed { index, scenario ->
            StaggeredFadeInUp(index = 6 + index, delayMillis = 60) {
                PremiumCard(
                    variant = PremiumCardVariant.Glass,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (scenario.iconType) {
                                    ScenarioIconType.TARGET -> "🎯"
                                    ScenarioIconType.TRENDING_UP -> "📈"
                                    ScenarioIconType.SPARKLES -> "✨"
                                    ScenarioIconType.DOLLAR_SIGN -> "💵"
                                    ScenarioIconType.PIGGY_BANK -> "🐷"
                                },
                                fontSize = 24.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = scenario.title,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = scenario.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF00F2FE).copy(alpha = 0.2f))
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = scenario.impact,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF00F2FE)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.financial_mirror_in_timeline, scenario.timeline),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Text(
                            text = "→",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InvestmentOpportunitiesSection(
    opportunities: List<dev.esbi.mizan.feature.financialmirror.domain.model.InvestmentOpportunity>,
    numberFormat: NumberFormat
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "⚡",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.financial_mirror_investment_opportunities),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFEE140).copy(alpha = 0.3f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.financial_mirror_ai_curated),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFEE140)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        opportunities.forEachIndexed { index, opportunity ->
            StaggeredFadeInUp(index = 9 + index, delayMillis = 60) {
                PremiumCard(
                    variant = PremiumCardVariant.Glass,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = opportunity.title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val typeColor = Color(opportunity.colorHex.toLongOrNull(16)?.toInt() ?: 0xFF667EEA.toInt())
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(typeColor.copy(alpha = 0.2f))
                                            .padding(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = when (opportunity.type) {
                                                InvestmentType.LOW_RISK -> stringResource(R.string.financial_mirror_low_risk)
                                                InvestmentType.MEDIUM_RISK -> stringResource(R.string.financial_mirror_medium_risk)
                                                InvestmentType.HIGH_RISK -> stringResource(R.string.financial_mirror_high_risk)
                                                InvestmentType.LONG_TERM -> stringResource(R.string.financial_mirror_long_term)
                                            },
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium,
                                            color = typeColor
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(R.string.financial_mirror_min_amount, opportunity.minAmount),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = opportunity.apy,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(opportunity.colorHex.toLongOrNull(16)?.toInt() ?: 0xFF667EEA.toInt())
                                )
                                Text(
                                    text = stringResource(R.string.financial_mirror_apy),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = opportunity.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        PremiumButton(
                            variant = PremiumButtonVariant.GRADIENT_PRIMARY,
                            size = PremiumButtonSize.MD,
                            onClick = { },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.financial_mirror_learn_more))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AIRecommendationCard(
    recommendation: dev.esbi.mizan.feature.financialmirror.domain.model.AIRecommendation
) {
    PremiumCard(
        variant = PremiumCardVariant.Glass,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF667EEA).copy(alpha = 0.1f),
                        Color(0xFF764BA2).copy(alpha = 0.1f)
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "🤖",
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(R.string.financial_mirror_ai_recommendation),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.financial_mirror_recommendation_intro),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                recommendation.recommendations.forEach { rec ->
                    Text(
                        text = "• $rec",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    FadeInUpAnimation {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            repeat(4) { index ->
                StaggeredFadeInUp(index = index) {
                    LoadingSkeleton(height = 200)
                }
            }
        }
    }
}
