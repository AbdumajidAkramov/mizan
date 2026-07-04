package dev.esbi.mizan.feature.transactionshub.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore
import dev.esbi.mizan.design.theme.colors.MizanTheme

/**
 * Custom Bar Chart for Weekly Trends using Canvas
 * Shows income and expense bars for each week
 */
@Composable
fun WeeklyTrendsChart(
    weeklySummaries: List<TransactionsHubStore.WeeklySummary>,
    modifier: Modifier = Modifier,
    chartHeight: Dp = 200.dp,
    barWidth: Dp = 24.dp,
    barSpacing: Dp = 8.dp,
    incomeColor: Color = MizanTheme.premium.colors.emerald,
    expenseColor: Color = Color(0xFFF5576C)
) {
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(weeklySummaries) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
    }

    if (weeklySummaries.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(chartHeight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No data available",
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.tertiary
            )
        }
        return
    }

    // Find max value for scaling
    val maxValue = weeklySummaries.maxOf { maxOf(it.income, it.expense) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Chart area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val chartBottom = canvasHeight - 30.dp.toPx()

                // Draw horizontal grid lines
                val gridLineCount = 4
                val gridSpacing = chartBottom / gridLineCount

                for (i in 0..gridLineCount) {
                    val y = chartBottom - (i * gridSpacing)
                    drawLine(
                        color = Color(0xFF2D3748),
                        start = Offset(0f, y),
                        end = Offset(canvasWidth, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Calculate bar positions
                val totalBarsWidth =
                    weeklySummaries.size * (barWidth.toPx() * 2 + barSpacing.toPx())
                val startX = (canvasWidth - totalBarsWidth) / 2
                val barWidthPx = barWidth.toPx()
                val barSpacingPx = barSpacing.toPx()
                val cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())

                weeklySummaries.forEachIndexed { index, week ->
                    val groupX = startX + index * (barWidthPx * 2 + barSpacingPx + 16.dp.toPx())

                    // Draw income bar
                    val incomeHeight = if (maxValue > java.math.BigDecimal.ZERO) {
                        (week.income.toFloat() / maxValue.toFloat() * chartBottom * animationProgress.value).toFloat()
                    } else 0f

                    if (incomeHeight > 0) {
                        drawRoundRect(
                            color = incomeColor,
                            topLeft = Offset(groupX, chartBottom - incomeHeight),
                            size = Size(barWidthPx, incomeHeight),
                            cornerRadius = cornerRadius
                        )
                    }

                    // Draw expense bar
                    val expenseHeight = if (maxValue > java.math.BigDecimal.ZERO) {
                        (week.expense.toFloat() / maxValue.toFloat() * chartBottom * animationProgress.value).toFloat()
                    } else 0f

                    if (expenseHeight > 0) {
                        drawRoundRect(
                            color = expenseColor,
                            topLeft = Offset(
                                groupX + barWidthPx + barSpacingPx,
                                chartBottom - expenseHeight
                            ),
                            size = Size(barWidthPx, expenseHeight),
                            cornerRadius = cornerRadius
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Week labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weeklySummaries.forEach { week ->
                Text(
                    text = "W${week.weekNumber}",
                    style = MizanTheme.typography.labelSm,
                    color = MizanTheme.premium.text.tertiary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Income legend
            Canvas(modifier = Modifier.size(12.dp)) {
                drawCircle(color = incomeColor)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Income",
                style = MizanTheme.typography.labelSm,
                color = MizanTheme.premium.text.secondary
            )

            Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.lg))

            // Expense legend
            Canvas(modifier = Modifier.size(12.dp)) {
                drawCircle(color = expenseColor)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Expense",
                style = MizanTheme.typography.labelSm,
                color = MizanTheme.premium.text.secondary
            )
        }
    }
}
