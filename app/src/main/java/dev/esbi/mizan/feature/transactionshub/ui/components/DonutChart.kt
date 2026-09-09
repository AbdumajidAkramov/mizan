package dev.esbi.mizan.feature.transactionshub.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.presentation.feature.transactionshub.store.TransactionsHubStore
import dev.esbi.mizan.design.theme.colors.MizanTheme

/**
 * Custom Donut Chart composable using Canvas
 * Draws arcs based on category percentages with animation
 */
@Composable
fun DonutChart(
    categorySummaries: List<TransactionsHubStore.CategorySummary>,
    totalAmount: Double,
    centerLabel: String,
    centerColor: Color,
    modifier: Modifier = Modifier,
    chartSize: Dp = 200.dp,
    strokeWidth: Dp = 32.dp,
    gapAngle: Float = 3f
) {
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(categorySummaries) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
    }

    Box(
        modifier = modifier.size(chartSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(chartSize)) {
            val canvasSize = size.minDimension
            val radius = (canvasSize - strokeWidth.toPx()) / 2
            val centerOffset = Offset(size.width / 2, size.height / 2)

            val arcSize = Size(radius * 2, radius * 2)
            val topLeft = Offset(
                centerOffset.x - radius,
                centerOffset.y - radius
            )

            if (categorySummaries.isEmpty()) {
                // Draw empty circle
                drawArc(
                    color = Color(0xFF2D3748),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            } else {
                var startAngle = -90f // Start from top

                categorySummaries.forEach { summary ->
                    val sweepAngle = (summary.percentage / 100f) * 360f * animationProgress.value

                    // Parse color
                    val color = try {
                        Color(android.graphics.Color.parseColor(summary.categoryColor))
                    } catch (e: Exception) {
                        Color(0xFFFF6B9D)
                    }

                    // Draw arc with gap
                    val adjustedSweep = (sweepAngle - gapAngle).coerceAtLeast(0f)
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = adjustedSweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                    )

                    startAngle += sweepAngle
                }
            }
        }

        // Center Text
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Total",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
            Text(
                text = centerLabel,
                style = MizanTheme.typography.headingLg,
                color = centerColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
