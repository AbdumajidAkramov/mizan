package dev.esbi.mizan.feature.financialmirror.presentation.ui.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.FinancialProjection
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.ProjectionData
import dev.esbi.mizan.presentation.feature.financialmirror.presentation.store.FinancialMirrorStore
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
internal fun NetWorthProjectionCard(
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
internal fun ProjectionChart(
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


// Mock Data yaratish (Preview uchun)
private val mockProjectionData = ProjectionData(
    currentNetWorth = 22450.0,
    projectionYears = 5,
    projections = listOf(
        FinancialProjection("2026", 24000.0, 26000.0, 28000.0),
        FinancialProjection("2027", 26000.0, 32000.0, 40000.0),
        FinancialProjection("2028", 29000.0, 45000.0, 65000.0),
        FinancialProjection("2029", 33000.0, 62000.0, 95000.0),
        FinancialProjection("2030", 38000.0, 85000.0, 140000.0)
    )
)

@Preview(
    name = "Net Worth Card - Dark",
    showBackground = true,
    backgroundColor = 0xFF111827 // To'q rangli fon (Glass effekt ko'rinishi uchun)
)
@Composable
private fun NetWorthProjectionCardPreview() {
    // Holatni saqlash (State hoisting for preview interactivity)
    var selectedView by remember {
        mutableStateOf(FinancialMirrorStore.ProjectionView.REALISTIC)
    }

    // MizanTheme contextida bo'lishi kerak (agar Theme faylingiz bo'lsa o'rab qo'ying)
    // MizanTheme {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFF111827)) // Orqa fon
    ) {
        NetWorthProjectionCard(
            projectionData = mockProjectionData,
            selectedView = selectedView,
            onViewSelected = { newView ->
                selectedView = newView
            }
        )
    }
    // }
}