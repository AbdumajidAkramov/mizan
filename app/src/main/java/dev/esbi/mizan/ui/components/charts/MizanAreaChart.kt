package dev.esbi.mizan.ui.components.charts

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.components.tooltip.PremiumTooltipContent
import kotlin.math.ceil
import kotlin.math.roundToInt

data class AreaChart(
    val dayLabel: String,
    val totalAmount: Float
)

@Composable
fun MizanAreaChart(
    spendingData: List<AreaChart>,
    modifier: Modifier = Modifier
) {
    var selectedPoint by remember { mutableStateOf<AreaChart?>(null) }
    var tooltipOffset by remember { mutableStateOf(Offset.Zero) }

    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    // Figma ranglari va stillari
    val labelColor = Color(0xFF94A3B8) // var(--premium-text-tertiary)
    val strokeColor = Color(0xFF667EEA) // Asosiy binafsha chiziq
    val gridLineColor = labelColor.copy(alpha = 0.2f)

    val textStyle = TextStyle(
        fontSize = 12.sp,
        color = labelColor
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(16.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val leftPadding = 45.dp.toPx()
                        val chartWidth = size.width - leftPadding
                        val xStep = chartWidth / (spendingData.size - 1)

                        // Eng yaqin nuqtani aniqlash
                        val index = ((offset.x - leftPadding) / xStep).roundToInt()
                        if (index in spendingData.indices) {
                            selectedPoint = spendingData[index]
                        }
                    }
                }
        ) {
            val leftPadding = 45.dp.toPx()
            val bottomPadding = 30.dp.toPx()
            val chartWidth = size.width - leftPadding
            val chartHeight = size.height - bottomPadding

            // 1. Hisob-kitoblar (Scaling)
            val rawMax = spendingData.maxOfOrNull { it.totalAmount } ?: 1f
            val maxY = (ceil(rawMax / 100) * 100).toFloat().coerceAtLeast(100f)
            val xStep = chartWidth / (spendingData.size - 1)

            // 2. Y-Axis Labellari va Gorizontal chiziqlar
            val steps = 4
            for (i in 0..steps) {
                val value = (maxY / steps) * i
                val y = chartHeight - (i * (chartHeight / steps))

                // Raqamlar
                val textLayout = textMeasurer.measure(value.toInt().toString(), textStyle)
                drawText(
                    textLayoutResult = textLayout,
                    topLeft = Offset(
                        leftPadding - textLayout.size.width - 12.dp.toPx(),
                        y - textLayout.size.height / 2
                    )
                )
            }

            // Nuqtalar koordinatasini yig'ish
            val points = spendingData.mapIndexed { index, point ->
                Offset(
                    x = leftPadding + (index * xStep),
                    y = chartHeight - ((point.totalAmount / maxY) * chartHeight)
                )
            }

            // 3. X-Axis Labellari (Jan, Feb...)
            spendingData.forEachIndexed { index, point ->
                val x = points[index].x
                val textLayout = textMeasurer.measure(point.dayLabel, textStyle)
                drawText(
                    textLayoutResult = textLayout,
                    topLeft = Offset(
                        x - textLayout.size.width / 2,
                        size.height - textLayout.size.height
                    )
                )
            }

            // 4. Grafik chizish (Area + Stroke)
            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 0 until points.size - 1) {
                    val p0 = points[i]
                    val p1 = points[i + 1]
                    cubicTo(
                        p0.x + (p1.x - p0.x) / 2,
                        p0.y,
                        p0.x + (p1.x - p0.x) / 2,
                        p1.y,
                        p1.x,
                        p1.y
                    )
                }
            }

            // Gradient Fill
            drawPath(
                path = Path().apply {
                    addPath(path)
                    lineTo(points.last().x, chartHeight)
                    lineTo(points.first().x, chartHeight)
                    close()
                },
                brush = Brush.verticalGradient(
                    colors = listOf(strokeColor.copy(alpha = 0.3f), Color.Transparent),
                    endY = chartHeight
                )
            )

            // Asosiy chiziq
            drawPath(path = path, color = strokeColor, style = Stroke(width = 3.dp.toPx()))

            // 5. TANLANGAN NUQTA: Circle va Vertical Line
            selectedPoint?.let { sp ->
                val index = spendingData.indexOf(sp)
                val activePoint = points[index]
                tooltipOffset = activePoint

                // A) Vertical Dashboard Line (Punktir chiziq)
                drawLine(
                    color = strokeColor.copy(alpha = 0.5f),
                    start = Offset(activePoint.x, 0f),
                    end = Offset(activePoint.x, chartHeight),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )

                // B) Tashqi yorug'lik (Glow effect)
                drawCircle(
                    color = strokeColor.copy(alpha = 0.2f),
                    radius = 12.dp.toPx(),
                    center = activePoint
                )

                // C) Oq hoshiyali Circle
                drawCircle(
                    color = Color.White,
                    radius = 6.dp.toPx(),
                    center = activePoint
                )
                drawCircle(
                    color = strokeColor,
                    radius = 4.dp.toPx(),
                    center = activePoint
                )
            }
        }

        // 6. Tooltip Overlay
        selectedPoint?.let { sp ->
            val xDp = with(density) { tooltipOffset.x.toDp() }
            val yDp = with(density) { tooltipOffset.y.toDp() }

            Box(
                modifier = Modifier
                    .offset(x = xDp - 60.dp, y = yDp - 85.dp)
                    .animateContentSize()
            ) {
                PremiumTooltipContent(
                    title = sp.dayLabel,
                    value = "${sp.totalAmount}"
                )
            }
        }
    }
}