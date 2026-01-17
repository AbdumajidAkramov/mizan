package dev.esbi.mizan.feature.statistics.presentation.ui.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import dev.esbi.mizan.feature.statistics.domain.model.ChartDataPoint
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
internal fun TrendChartCard(
    data: List<ChartDataPoint>,
    modifier: Modifier = Modifier
) {
    GlassCard {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Monthly Trend",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MizanTheme.premium.text.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Last 6 months spending pattern",
                    fontSize = 12.sp,
                    color = MizanTheme.premium.text.muted
                )
                Spacer(modifier = Modifier.height(24.dp))

                AnimatedLineChart(
                    data = data,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }

}

@Composable
internal fun AnimatedLineChart(
    data: List<ChartDataPoint>,
    modifier: Modifier = Modifier
) {
    var selectedPoint by remember { mutableStateOf<Int?>(null) }
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing
            )
        )
    }


    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val padding = 40f
        val chartWidth = width - 2 * padding
        val chartHeight = height - 2 * padding

        val maxValue = data.maxOfOrNull { it.value } ?: 1f
        val minValue = data.minOfOrNull { it.value } ?: 0f
        val valueRange = maxValue - minValue

        val stepX = chartWidth / (data.size - 1)

        val points = data.mapIndexed { index, point ->
            val x = padding + index * stepX
            val normalizedValue = (point.value - minValue) / valueRange
            val y = height - padding - (normalizedValue * chartHeight)
            Offset(x, y)
        }

        if (animationProgress.value > 0f) {
            val visiblePoints = (points.size * animationProgress.value).toInt().coerceAtLeast(2)
            val animatedPoints = points.take(visiblePoints)

            val gradientPath = Path().apply {
                if (animatedPoints.isNotEmpty()) {
                    moveTo(animatedPoints.first().x, height - padding)
                    lineTo(animatedPoints.first().x, animatedPoints.first().y)

                    for (i in 0 until animatedPoints.size - 1) {
                        val current = animatedPoints[i]
                        val next = animatedPoints[i + 1]
                        val controlX1 = current.x + (next.x - current.x) / 3
                        val controlY1 = current.y
                        val controlX2 = current.x + 2 * (next.x - current.x) / 3
                        val controlY2 = next.y
                        cubicTo(controlX1, controlY1, controlX2, controlY2, next.x, next.y)
                    }

                    lineTo(animatedPoints.last().x, height - padding)
                    close()
                }
            }

            drawPath(
                path = gradientPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF667EEA).copy(alpha = 0.3f),
                        Color(0xFF764BA2).copy(alpha = 0.05f)
                    ),
                    startY = 0f,
                    endY = height
                )
            )

            val linePath = Path().apply {
                if (animatedPoints.isNotEmpty()) {
                    moveTo(animatedPoints.first().x, animatedPoints.first().y)
                    for (i in 0 until animatedPoints.size - 1) {
                        val current = animatedPoints[i]
                        val next = animatedPoints[i + 1]
                        val controlX1 = current.x + (next.x - current.x) / 3
                        val controlY1 = current.y
                        val controlX2 = current.x + 2 * (next.x - current.x) / 3
                        val controlY2 = next.y
                        cubicTo(controlX1, controlY1, controlX2, controlY2, next.x, next.y)
                    }
                }
            }

            drawPath(
                path = linePath,
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                ),
                style = Stroke(width = 4f, cap = StrokeCap.Round)
            )

            animatedPoints.forEachIndexed { index, point ->
                drawCircle(
                    color = Color(0xFF667EEA),
                    radius = 6f,
                    center = point
                )
                drawCircle(
                    color = Color.White,
                    radius = 3f,
                    center = point
                )
            }
        }

        data.forEachIndexed { index, point ->
            val x = padding + index * stepX
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    point.label,
                    x,
                    height - 10f,
                    android.graphics.Paint().apply {
                        color = "#6B6B7F".toColorInt()
                        textSize = 28f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    }
}
