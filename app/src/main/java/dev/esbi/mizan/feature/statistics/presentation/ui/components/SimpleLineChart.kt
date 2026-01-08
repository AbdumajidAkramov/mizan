package dev.esbi.mizan.feature.statistics.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.statistics.domain.model.SpendingTrendPoint

/**
 * Simple line chart for spending trends
 * Displays gradient line with smooth curves
 */
@Composable
fun SimpleLineChart(
    data: List<SpendingTrendPoint>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val width = size.width
        val height = size.height
        val padding = 40f

        val maxValue = data.maxOfOrNull { it.amount } ?: 1.0
        val minValue = data.minOfOrNull { it.amount } ?: 0.0
        val range = maxValue - minValue

        val stepX = (width - 2 * padding) / (data.size - 1).coerceAtLeast(1)

        // Draw line
        val path = Path()
        data.forEachIndexed { index, point ->
            val x = padding + index * stepX
            val normalizedValue = if (range > 0) {
                ((point.amount - minValue) / range).toFloat()
            } else {
                0.5f
            }
            val y = height - padding - (normalizedValue * (height - 2 * padding))

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF667EEA),
                    Color(0xFF764BA2)
                )
            ),
            style = Stroke(
                width = 6f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Draw dots
        data.forEachIndexed { index, point ->
            val x = padding + index * stepX
            val normalizedValue = if (range > 0) {
                ((point.amount - minValue) / range).toFloat()
            } else {
                0.5f
            }
            val y = height - padding - (normalizedValue * (height - 2 * padding))

            drawCircle(
                color = Color(0xFF667EEA),
                radius = 8f,
                center = Offset(x, y)
            )
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = Offset(x, y)
            )
        }
    }
}
