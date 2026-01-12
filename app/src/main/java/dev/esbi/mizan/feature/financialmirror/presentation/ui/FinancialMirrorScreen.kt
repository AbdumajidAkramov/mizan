package dev.esbi.mizan.feature.financialmirror.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import dev.esbi.mizan.feature.financialmirror.presentation.FinancialMirrorViewModel
import dev.esbi.mizan.feature.financialmirror.presentation.FinancialMirrorViewModelFactory
import dev.esbi.mizan.feature.financialmirror.presentation.store.FinancialMirrorStore
import dev.esbi.mizan.ui.components.ErrorState
import dev.esbi.mizan.ui.components.LoadingSkeleton
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

private const val ANIM_DELAY_MS = 50

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
                is FinancialMirrorStore.Label.ShowError -> {}
                is FinancialMirrorStore.Label.NavigateToInvestment -> {}
            }
        }
    }
    when {
        state.isLoading && state.financialMirrorData == null -> LoadingContent()
        state.error != null && state.financialMirrorData == null -> ErrorState(
            message = state.error ?: "Unknown error",
            onRetry = { viewModel.onIntent(FinancialMirrorStore.Intent.Retry) },
            modifier = Modifier.fillMaxSize()
        )

        state.financialMirrorData != null -> FinancialMirrorScrollContent(
            modifier = modifier,
            data = state.financialMirrorData!!,
            selectedView = state.selectedProjectionView,
            onViewSelected = {
                viewModel.onIntent(
                    FinancialMirrorStore.Intent.SelectProjectionView(
                        it
                    )
                )
            }
        )
    }

}

@Composable
private fun FinancialMirrorScrollContent(
    data: FinancialMirrorSummary,
    selectedView: FinancialMirrorStore.ProjectionView,
    modifier: Modifier = Modifier,
    onViewSelected: (FinancialMirrorStore.ProjectionView) -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 120.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { AnimSection(visible, 0) { HeaderSection() } }
        item {
            AnimSection(visible, ANIM_DELAY_MS) {
                NetWorthProjectionCard(
                    data.projectionData,
                    selectedView,
                    onViewSelected
                )
            }
        }
        item { AnimSection(visible, ANIM_DELAY_MS * 2) { RiskAnalysisCard(data.riskAnalysis) } }
        item {
            AnimSection(
                visible,
                ANIM_DELAY_MS * 3
            ) { TimeMachineSection(data.timeMachineScenarios) }
        }
        item {
            AnimSection(
                visible,
                ANIM_DELAY_MS * 4
            ) { InvestmentOpportunitiesSection(data.investmentOpportunities) }
        }
        item {
            AnimSection(
                visible,
                ANIM_DELAY_MS * 5
            ) { AIRecommendationCard(data.aiRecommendation) }
        }
    }
}

@Composable
private fun AnimSection(visible: Boolean, delayMs: Int, content: @Composable () -> Unit) {
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(visible) {
        if (visible) {
            delay(delayMs.toLong())
            show = true
        }
    }
    AnimatedVisibility(
        visible = show,
        enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 4 }
    ) { content() }
}

@Composable
private fun PressCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, tween(150), label = "scale")
    Box(
        modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { pressed = true; tryAwaitRelease(); pressed = false },
                    onTap = { onClick?.invoke() }
                )
            }
    ) { content() }
}

@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    PressCard(modifier, onClick) {
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
                .background(MizanTheme.premium.background.secondary.copy(0.95f))
                .border(
                    1.dp,
                    MizanTheme.premium.glass.border,
                    RoundedCornerShape(MizanTheme.premium.radius.xl)
                )
        ) { content() }
    }
}

@Composable
private fun HeaderSection() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("✨", fontSize = 28.sp)
            Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
            Text(
                "Financial Mirror",
                style = MizanTheme.premium.typography.headingXl,
                color = MizanTheme.premium.text.primary
            )
        }
        Spacer(Modifier.height(MizanTheme.premium.spacing.xs))
        Text(
            "AI-powered insights into your financial future",
            style = MizanTheme.premium.typography.bodyMd,
            color = MizanTheme.premium.text.tertiary
        )
    }
}

@Composable
private fun NetWorthProjectionCard(
    projectionData: ProjectionData,
    selectedView: FinancialMirrorStore.ProjectionView,
    onViewSelected: (FinancialMirrorStore.ProjectionView) -> Unit
) {
    val fmt = NumberFormat.getCurrencyInstance(Locale.US).apply { maximumFractionDigits = 0 }

    GlassCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MizanTheme.premium.spacing.lg)
        ) {
            Text(
                "Net Worth Projection",
                style = MizanTheme.premium.typography.headingMd,
                color = MizanTheme.premium.text.primary
            )
            Spacer(Modifier.height(MizanTheme.premium.spacing.xs))
            Text(
                "Your potential wealth growth over 5 years",
                style = MizanTheme.premium.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )

            Spacer(Modifier.height(20.dp))

            // Projection Toggle Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
            ) {
                listOf(
                    FinancialMirrorStore.ProjectionView.CONSERVATIVE to "Conservative",
                    FinancialMirrorStore.ProjectionView.REALISTIC to "Realistic",
                    FinancialMirrorStore.ProjectionView.OPTIMISTIC to "Optimistic"
                ).forEach { (view, label) ->
                    val isSelected = selectedView == view
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                            .background(
                                if (isSelected) MizanTheme.premium.gradients.primary
                                else Brush.horizontalGradient(
                                    listOf(
                                        MizanTheme.premium.background.secondary,
                                        MizanTheme.premium.background.secondary
                                    )
                                )
                            )
                            .clickable { onViewSelected(view) }
                            .padding(vertical = MizanTheme.premium.spacing.sm),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            style = MizanTheme.premium.typography.labelMd,
                            color = if (isSelected) Color.White else MizanTheme.premium.text.secondary
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Custom Canvas Projection Chart
            ProjectionChart(
                projections = projectionData.projections,
                selectedView = selectedView,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(Modifier.height(20.dp))

            // Projection Summary
            val targetValue = when (selectedView) {
                FinancialMirrorStore.ProjectionView.CONSERVATIVE ->
                    projectionData.projections.lastOrNull()?.conservative ?: 0.0

                FinancialMirrorStore.ProjectionView.REALISTIC ->
                    projectionData.projections.lastOrNull()?.realistic ?: 0.0

                FinancialMirrorStore.ProjectionView.OPTIMISTIC ->
                    projectionData.projections.lastOrNull()?.optimistic ?: 0.0
            }
            val growthPercent = if (projectionData.currentNetWorth > 0) {
                ((targetValue - projectionData.currentNetWorth) / projectionData.currentNetWorth * 100).toInt()
            } else 0

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Starting",
                        style = MizanTheme.premium.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                    Spacer(Modifier.height(MizanTheme.premium.spacing.xs))
                    Text(
                        "$${(projectionData.currentNetWorth / 1000).toInt()}k",
                        style = MizanTheme.premium.typography.headingMd,
                        color = MizanTheme.premium.text.primary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "5-Year Target",
                        style = MizanTheme.premium.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                    Spacer(Modifier.height(MizanTheme.premium.spacing.xs))
                    Text(
                        "$${(targetValue / 1000).toInt()}k",
                        style = MizanTheme.premium.typography.headingMd,
                        color = MizanTheme.premium.colors.primary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Total Growth",
                        style = MizanTheme.premium.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                    Spacer(Modifier.height(MizanTheme.premium.spacing.xs))
                    Text(
                        "+$growthPercent%",
                        style = MizanTheme.premium.typography.headingMd,
                        color = MizanTheme.premium.colors.success
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectionChart(
    projections: List<FinancialProjection>,
    selectedView: FinancialMirrorStore.ProjectionView,
    modifier: Modifier = Modifier
) {
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(selectedView) {
        animProgress.snapTo(0f)
        animProgress.animateTo(1f, tween(1000, easing = FastOutSlowInEasing))
    }

    val data = projections.ifEmpty {
        listOf(
            FinancialProjection("2026", 22450.0, 22450.0, 22450.0),
            FinancialProjection("2027", 28500.0, 32400.0, 38200.0),
            FinancialProjection("2028", 34200.0, 45800.0, 62500.0),
            FinancialProjection("2029", 39800.0, 62100.0, 95800.0),
            FinancialProjection("2030", 45200.0, 82500.0, 142000.0),
            FinancialProjection("2031", 50400.0, 108200.0, 208500.0)
        )
    }

    val values = data.map { proj ->
        when (selectedView) {
            FinancialMirrorStore.ProjectionView.CONSERVATIVE -> proj.conservative
            FinancialMirrorStore.ProjectionView.REALISTIC -> proj.realistic
            FinancialMirrorStore.ProjectionView.OPTIMISTIC -> proj.optimistic
        }
    }

    Column(modifier) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Grid lines
            val gridColor = MizanTheme.premium.glass.border
            val colors = listOf(
                MizanTheme.premium.colors.primary.copy(alpha = 0.3f),
                MizanTheme.premium.colors.primary.copy(alpha = 0f)
            )
            val drawLineColor = MizanTheme.premium.colors.primary
            Canvas(Modifier.fillMaxSize()) {
                val padLeft = 50.dp.toPx()
                val padBottom = 30.dp.toPx()
                val chartWidth = size.width - padLeft
                val chartHeight = size.height - padBottom

                val maxVal = values.maxOrNull() ?: 1.0
                val minVal = 0.0
                val range = maxVal - minVal

                for (i in 0..4) {
                    val y = chartHeight * (1 - i / 4f)
                    drawLine(
                        color = gridColor,
                        start = Offset(padLeft, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
                    )
                    // Y-axis labels
                    val labelValue = minVal + (range * i / 4)
                    // Labels drawn separately
                }

                // Draw gradient fill under line
                if (values.isNotEmpty() && animProgress.value > 0) {
                    val fillPath = Path()
                    val linePath = Path()

                    values.forEachIndexed { i, v ->
                        val x = padLeft + (chartWidth * i / (values.size - 1).coerceAtLeast(1))
                        val normalizedY = ((v - minVal) / range).toFloat().coerceIn(0f, 1f)
                        val y = chartHeight - (normalizedY * chartHeight * animProgress.value)

                        if (i == 0) {
                            fillPath.moveTo(x, chartHeight)
                            fillPath.lineTo(x, y)
                            linePath.moveTo(x, y)
                        } else {
                            val prevX =
                                padLeft + (chartWidth * (i - 1) / (values.size - 1).coerceAtLeast(1))
                            val prevV = values[i - 1]
                            val prevNormY = ((prevV - minVal) / range).toFloat().coerceIn(0f, 1f)
                            val prevY = chartHeight - (prevNormY * chartHeight * animProgress.value)

                            // Bezier curve
                            val cx1 = prevX + (x - prevX) / 3
                            val cx2 = prevX + 2 * (x - prevX) / 3
                            fillPath.cubicTo(cx1, prevY, cx2, y, x, y)
                            linePath.cubicTo(cx1, prevY, cx2, y, x, y)
                        }
                    }

                    // Close fill path
                    fillPath.lineTo(padLeft + chartWidth, chartHeight)
                    fillPath.close()

                    // Draw gradient fill
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = colors,
                            startY = 0f,
                            endY = chartHeight
                        )
                    )

                    // Draw line
                    drawPath(
                        path = linePath,
                        color = drawLineColor,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }

            // Y-axis labels
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .height(170.dp)
                    .padding(end = MizanTheme.premium.spacing.sm),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                val maxVal = values.maxOrNull() ?: 1.0
                listOf(maxVal, maxVal * 0.75, maxVal * 0.5, maxVal * 0.25, 0.0).forEach { v ->
                    Text(
                        "$${(v / 1000).toInt()}k",
                        style = MizanTheme.premium.typography.bodyXs,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
            }
        }

        // X-axis labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEach { proj ->
                Text(
                    proj.year,
                    style = MizanTheme.premium.typography.bodyXs,
                    color = MizanTheme.premium.text.tertiary
                )
            }
        }
    }
}

@Composable
private fun RiskAnalysisCard(riskAnalysis: RiskAnalysis) {
    GlassCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MizanTheme.premium.spacing.lg)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MizanTheme.premium.colors.warning.copy(0.2f)),
                    Alignment.Center
                ) {
                    Icon(
                        icon = IconValue(Icons.ic_shield),
                        modifier = Modifier.size(16.dp),
                        tint = MizanTheme.premium.colors.warning
                    )
                }
                Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
                Text(
                    "Financial Risk Assessment",
                    style = MizanTheme.premium.typography.headingSm,
                    color = MizanTheme.premium.text.primary
                )
            }

            Spacer(Modifier.height(MizanTheme.premium.spacing.xl))

            riskAnalysis.riskFactors.forEach { risk ->
                RiskFactorItem(risk)
                Spacer(Modifier.height(MizanTheme.premium.spacing.md))
            }

            // Overall Risk Score
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                    .background(MizanTheme.premium.background.secondary)
                    .padding(MizanTheme.premium.spacing.md)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Overall Risk Score",
                            style = MizanTheme.premium.typography.bodySm,
                            color = MizanTheme.premium.text.tertiary
                        )
                        Spacer(Modifier.height(MizanTheme.premium.spacing.xs))
                        Text(
                            "${riskAnalysis.overallScore}/100",
                            style = MizanTheme.premium.typography.headingXl,
                            color = MizanTheme.premium.text.primary
                        )
                    }

                    val statusColor = when (riskAnalysis.overallStatus) {
                        RiskStatus.GOOD -> MizanTheme.premium.colors.success
                        RiskStatus.FAIR -> MizanTheme.premium.colors.primary
                        RiskStatus.WARNING -> MizanTheme.premium.colors.warning
                    }
                    val statusLabel = when (riskAnalysis.overallStatus) {
                        RiskStatus.GOOD -> "Excellent"
                        RiskStatus.FAIR -> "Moderate"
                        RiskStatus.WARNING -> "Needs Work"
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                            .background(statusColor.copy(alpha = 0.2f))
                            .padding(
                                horizontal = MizanTheme.premium.spacing.md,
                                vertical = MizanTheme.premium.spacing.sm
                            )
                    ) {
                        Text(
                            statusLabel,
                            style = MizanTheme.premium.typography.labelMd,
                            color = statusColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RiskFactorItem(risk: RiskFactor) {
    val color = when (risk.status) {
        RiskStatus.GOOD -> MizanTheme.premium.colors.success
        RiskStatus.FAIR -> MizanTheme.premium.colors.primary
        RiskStatus.WARNING -> MizanTheme.premium.colors.warning
    }

    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(risk.score) {
        animProgress.animateTo(risk.score / 100f, tween(800, easing = FastOutSlowInEasing))
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    risk.category,
                    style = MizanTheme.premium.typography.labelMd,
                    color = MizanTheme.premium.text.primary
                )
                Text(
                    risk.description,
                    style = MizanTheme.premium.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    risk.score.toString(),
                    style = MizanTheme.premium.typography.headingMd,
                    color = color
                )
                Text(
                    "/ 100",
                    style = MizanTheme.premium.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }
        }

        Spacer(Modifier.height(MizanTheme.premium.spacing.sm))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MizanTheme.premium.background.secondary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = animProgress.value)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
    }
}

@Composable
private fun TimeMachineSection(scenarios: List<TimeMachineScenario>) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("⏰", fontSize = 20.sp)
            Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
            Text(
                "Time Machine",
                style = MizanTheme.premium.typography.headingSm,
                color = MizanTheme.premium.text.primary
            )
            Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                    .background(MizanTheme.premium.gradients.primary)
                    .padding(
                        horizontal = MizanTheme.premium.spacing.sm,
                        vertical = MizanTheme.premium.spacing.xs
                    )
            ) {
                Text(
                    "What If?",
                    style = MizanTheme.premium.typography.labelSm,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(MizanTheme.premium.spacing.md))

        scenarios.forEach { scenario ->
            ScenarioCard(scenario)
            Spacer(Modifier.height(MizanTheme.premium.spacing.sm))
        }
    }
}

@Composable
private fun ScenarioCard(scenario: TimeMachineScenario) {
    val iconRes = when (scenario.iconType) {
        ScenarioIconType.TARGET -> Icons.ic_ai_insight
        ScenarioIconType.TRENDING_UP -> Icons.ic_trend_up
        ScenarioIconType.SPARKLES -> Icons.ic_ai_insight
        ScenarioIconType.DOLLAR_SIGN -> Icons.ic_wallet
        ScenarioIconType.PIGGY_BANK -> Icons.ic_wallet
    }

    GlassCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MizanTheme.premium.spacing.xl),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                    .background(MizanTheme.premium.gradients.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon = IconValue(iconRes),
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }

            Spacer(Modifier.width(MizanTheme.premium.spacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    scenario.title,
                    style = MizanTheme.premium.typography.labelLg,
                    color = MizanTheme.premium.text.primary
                )
                Spacer(Modifier.height(MizanTheme.premium.spacing.xs))
                Text(
                    scenario.description,
                    style = MizanTheme.premium.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
                Spacer(Modifier.height(MizanTheme.premium.spacing.sm))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                            .background(MizanTheme.premium.colors.success.copy(alpha = 0.2f))
                            .padding(
                                horizontal = MizanTheme.premium.spacing.sm,
                                vertical = MizanTheme.premium.spacing.xs
                            )
                    ) {
                        Text(
                            scenario.impact,
                            style = MizanTheme.premium.typography.labelSm,
                            color = MizanTheme.premium.colors.success
                        )
                    }
                    Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
                    Text(
                        "in ${scenario.timeline}",
                        style = MizanTheme.premium.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
            }

            Icon(
                icon = IconValue(Icons.ic_trend_up),
                modifier = Modifier.size(20.dp),
                tint = MizanTheme.premium.text.tertiary
            )
        }
    }
}

@Composable
private fun InvestmentOpportunitiesSection(opportunities: List<InvestmentOpportunity>) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("⚡", fontSize = 20.sp)
            Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
            Text(
                "Investment Opportunities",
                style = MizanTheme.premium.typography.headingSm,
                color = MizanTheme.premium.text.primary
            )
            Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                    .background(MizanTheme.premium.colors.warning.copy(alpha = 0.3f))
                    .padding(
                        horizontal = MizanTheme.premium.spacing.sm,
                        vertical = MizanTheme.premium.spacing.xs
                    )
            ) {
                Text(
                    "AI Curated",
                    style = MizanTheme.premium.typography.labelSm,
                    color = MizanTheme.premium.colors.warning
                )
            }
        }

        Spacer(Modifier.height(MizanTheme.premium.spacing.md))

        opportunities.forEach { opportunity ->
            InvestmentCard(opportunity)
            Spacer(Modifier.height(MizanTheme.premium.spacing.sm))
        }
    }
}

@Composable
private fun InvestmentCard(opportunity: InvestmentOpportunity) {
    val typeColor = when (opportunity.type) {
        InvestmentType.LOW_RISK -> MizanTheme.premium.colors.success
        InvestmentType.MEDIUM_RISK -> MizanTheme.premium.colors.primary
        InvestmentType.HIGH_RISK -> MizanTheme.premium.categories.shopping
        InvestmentType.LONG_TERM -> MizanTheme.premium.colors.primary
    }
    val typeLabel = when (opportunity.type) {
        InvestmentType.LOW_RISK -> "Low Risk"
        InvestmentType.MEDIUM_RISK -> "Medium Risk"
        InvestmentType.HIGH_RISK -> "High Risk"
        InvestmentType.LONG_TERM -> "Long-term"
    }

    GlassCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MizanTheme.premium.spacing.xl)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        opportunity.title,
                        style = MizanTheme.premium.typography.headingSm,
                        color = MizanTheme.premium.text.primary
                    )
                    Spacer(Modifier.height(MizanTheme.premium.spacing.sm))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                                .background(typeColor.copy(alpha = 0.2f))
                                .padding(
                                    horizontal = MizanTheme.premium.spacing.sm,
                                    vertical = MizanTheme.premium.spacing.xs
                                )
                        ) {
                            Text(
                                typeLabel,
                                style = MizanTheme.premium.typography.labelSm,
                                color = typeColor
                            )
                        }
                        Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
                        Text(
                            "Min: ${opportunity.minAmount}",
                            style = MizanTheme.premium.typography.bodySm,
                            color = MizanTheme.premium.text.tertiary
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        opportunity.apy,
                        style = MizanTheme.premium.typography.headingLg,
                        color = typeColor
                    )
                    Text(
                        "APY",
                        style = MizanTheme.premium.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
            }

            Spacer(Modifier.height(MizanTheme.premium.spacing.sm))

            Text(
                opportunity.description,
                style = MizanTheme.premium.typography.bodyMd,
                color = MizanTheme.premium.text.tertiary
            )

            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            // Gradient Learn More Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                    .background(MizanTheme.premium.gradients.primary)
                    .clickable { }
                    .padding(vertical = MizanTheme.premium.spacing.sm),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Learn More",
                        style = MizanTheme.premium.typography.labelMd,
                        color = Color.White
                    )
                    Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
                    Icon(
                        icon = IconValue(Icons.ic_trend_up),
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun AIRecommendationCard(recommendation: AIRecommendation) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MizanTheme.premium.colors.primary.copy(alpha = 0.15f),
                        MizanTheme.premium.colors.secondary.copy(alpha = 0.15f)
                    )
                )
            )
            .border(
                1.dp,
                MizanTheme.premium.glass.border,
                RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MizanTheme.premium.spacing.xl)
        ) {
            Text("🤖", fontSize = 32.sp)
            Spacer(Modifier.width(MizanTheme.premium.spacing.md))
            Column {
                Text(
                    "AI Recommendation",
                    style = MizanTheme.premium.typography.headingSm,
                    color = MizanTheme.premium.text.primary
                )
                Spacer(Modifier.height(MizanTheme.premium.spacing.sm))
                Text(
                    "Based on your spending patterns and goals, we recommend:",
                    style = MizanTheme.premium.typography.bodyMd,
                    color = MizanTheme.premium.text.secondary
                )
                Spacer(Modifier.height(MizanTheme.premium.spacing.sm))
                recommendation.recommendations.forEach { rec ->
                    Text(
                        "• $rec",
                        style = MizanTheme.premium.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                    Spacer(Modifier.height(MizanTheme.premium.spacing.xs))
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MizanTheme.premium.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.lg)
    ) {
        LoadingSkeleton(height = 200)
        LoadingSkeleton(height = 300)
        LoadingSkeleton(height = 200)
        LoadingSkeleton(height = 200)
    }
}
