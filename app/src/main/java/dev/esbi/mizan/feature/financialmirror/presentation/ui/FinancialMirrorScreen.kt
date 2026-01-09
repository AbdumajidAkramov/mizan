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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
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
import dev.esbi.mizan.ui.utils.Icons
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

// Premium Dark Theme Colors
private val DarkBg = Color(0xFF0D0D1A)
private val CardBg = Color(0xFF1A1A2E)
private val CardBorderColor = Color.White.copy(alpha = 0.08f)
private val Purple = Color(0xFF667EEA)
private val Purple2 = Color(0xFF764BA2)
private val Pink = Color(0xFFF5576C)
private val Cyan = Color(0xFF00F2FE)
private val Orange = Color(0xFFFFA34D)
private val Yellow = Color(0xFFFEE140)
private val Red = Color(0xFFFF6B6B)
private val TextWhite = Color.White
private val TextGray = Color(0xFFB8B8D1)
private val TextMuted = Color(0xFF718096)
private val Blue = Color(0xFF4FACFE)
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        when {
            state.isLoading && state.financialMirrorData == null -> LoadingContent()
            state.error != null && state.financialMirrorData == null -> ErrorState(
                message = state.error ?: "Unknown error",
                onRetry = { viewModel.onIntent(FinancialMirrorStore.Intent.Retry) },
                modifier = Modifier.fillMaxSize()
            )

            state.financialMirrorData != null -> FinancialMirrorScrollContent(
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
}

@Composable
private fun FinancialMirrorScrollContent(
    data: FinancialMirrorSummary,
    selectedView: FinancialMirrorStore.ProjectionView,
    onViewSelected: (FinancialMirrorStore.ProjectionView) -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    LazyColumn(
        modifier = Modifier
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
                .clip(RoundedCornerShape(24.dp))
                .background(CardBg.copy(0.95f))
                .border(1.dp, CardBorderColor, RoundedCornerShape(24.dp))
        ) { content() }
    }
}

@Composable
private fun HeaderSection() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("✨", fontSize = 28.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                "Financial Mirror",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            "AI-powered insights into your financial future",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted
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
                .padding(24.dp)
        ) {
            Text(
                "Net Worth Projection",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Your potential wealth growth over 5 years",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )

            Spacer(Modifier.height(20.dp))

            // Projection Toggle Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(Purple, Purple2))
                                else Brush.horizontalGradient(listOf(CardBg, CardBg))
                            )
                            .clickable { onViewSelected(view) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) Color.White else TextGray
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
                    Text("Starting", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "$${(projectionData.currentNetWorth / 1000).toInt()}k",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "5-Year Target",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "$${(targetValue / 1000).toInt()}k",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Purple
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Total Growth",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "+$growthPercent%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Cyan
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
            Canvas(Modifier.fillMaxSize()) {
                val padLeft = 50.dp.toPx()
                val padBottom = 30.dp.toPx()
                val chartWidth = size.width - padLeft
                val chartHeight = size.height - padBottom

                val maxVal = values.maxOrNull() ?: 1.0
                val minVal = 0.0
                val range = maxVal - minVal

                // Grid lines
                val gridColor = CardBorderColor
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
                            colors = listOf(Purple.copy(alpha = 0.3f), Purple.copy(alpha = 0f)),
                            startY = 0f,
                            endY = chartHeight
                        )
                    )

                    // Draw line
                    drawPath(
                        path = linePath,
                        color = Purple,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }

            // Y-axis labels
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .height(170.dp)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                val maxVal = values.maxOrNull() ?: 1.0
                listOf(maxVal, maxVal * 0.75, maxVal * 0.5, maxVal * 0.25, 0.0).forEach { v ->
                    Text(
                        "$${(v / 1000).toInt()}k",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        fontSize = 10.sp
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
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
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
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Yellow.copy(0.2f)),
                    Alignment.Center
                ) {
                    Icon(
                        icon = IconValue(Icons.ic_shield),
                        modifier = Modifier.size(16.dp),
                        tint = Yellow
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "Financial Risk Assessment",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
            }

            Spacer(Modifier.height(20.dp))

            riskAnalysis.riskFactors.forEach { risk ->
                RiskFactorItem(risk)
                Spacer(Modifier.height(16.dp))
            }

            // Overall Risk Score
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBg)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Overall Risk Score",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "${riskAnalysis.overallScore}/100",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    val statusColor = when (riskAnalysis.overallStatus) {
                        RiskStatus.GOOD -> Cyan
                        RiskStatus.FAIR -> Blue
                        RiskStatus.WARNING -> Yellow
                    }
                    val statusLabel = when (riskAnalysis.overallStatus) {
                        RiskStatus.GOOD -> "Excellent"
                        RiskStatus.FAIR -> "Moderate"
                        RiskStatus.WARNING -> "Needs Work"
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(statusColor.copy(alpha = 0.2f))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            statusLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
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
        RiskStatus.GOOD -> Cyan
        RiskStatus.FAIR -> Blue
        RiskStatus.WARNING -> Yellow
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
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = TextWhite
                )
                Text(
                    risk.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    risk.score.toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    "/ 100",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(CardBg)
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
            Spacer(Modifier.width(8.dp))
            Text(
                "Time Machine",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
            )
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.horizontalGradient(listOf(Purple, Purple2)))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    "What If?",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        scenarios.forEach { scenario ->
            ScenarioCard(scenario)
            Spacer(Modifier.height(12.dp))
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
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(Purple, Purple2))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon = IconValue(iconRes),
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    scenario.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    scenario.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Cyan.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            scenario.impact,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = Cyan
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "in ${scenario.timeline}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }

            Icon(
                icon = IconValue(Icons.ic_trend_up),
                modifier = Modifier.size(20.dp),
                tint = TextMuted
            )
        }
    }
}

@Composable
private fun InvestmentOpportunitiesSection(opportunities: List<InvestmentOpportunity>) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("⚡", fontSize = 20.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                "Investment Opportunities",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
            )
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Yellow.copy(alpha = 0.3f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    "AI Curated",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = Yellow
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        opportunities.forEach { opportunity ->
            InvestmentCard(opportunity)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun InvestmentCard(opportunity: InvestmentOpportunity) {
    val typeColor = when (opportunity.type) {
        InvestmentType.LOW_RISK -> Cyan
        InvestmentType.MEDIUM_RISK -> Blue
        InvestmentType.HIGH_RISK -> Orange
        InvestmentType.LONG_TERM -> Purple
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
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        opportunity.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(typeColor.copy(alpha = 0.2f))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                typeLabel,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = typeColor
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Min: ${opportunity.minAmount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        opportunity.apy,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = typeColor
                    )
                    Text(
                        "APY",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                opportunity.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )

            Spacer(Modifier.height(16.dp))

            // Gradient Learn More Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.horizontalGradient(listOf(Purple, Purple2)))
                    .clickable { }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Learn More",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
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
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Purple.copy(alpha = 0.15f), Purple2.copy(alpha = 0.15f))
                )
            )
            .border(1.dp, CardBorderColor, RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text("🤖", fontSize = 32.sp)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    "AI Recommendation",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Based on your spending patterns and goals, we recommend:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray
                )
                Spacer(Modifier.height(12.dp))
                recommendation.recommendations.forEach { rec ->
                    Text(
                        "• $rec",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    Spacer(Modifier.height(4.dp))
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        LoadingSkeleton(height = 200)
        LoadingSkeleton(height = 300)
        LoadingSkeleton(height = 200)
        LoadingSkeleton(height = 200)
    }
}
