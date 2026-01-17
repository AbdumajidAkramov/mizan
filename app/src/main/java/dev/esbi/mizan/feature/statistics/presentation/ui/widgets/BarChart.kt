package dev.esbi.mizan.feature.statistics.presentation.ui.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas
import dev.esbi.mizan.feature.statistics.domain.model.CategoryData
import androidx.core.graphics.toColorInt

@Composable
internal fun BarChart(
    data: List<CategoryData>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val padding = 40f
        val chartHeight = height - 2 * padding
        val barWidth = (width - 2 * padding) / (data.size * 2)
        val spacing = barWidth

        val maxValue = data.maxOfOrNull { it.amount } ?: 1f

        data.forEachIndexed { index, category ->
            val barHeight = (category.amount / maxValue) * chartHeight
            val x = padding + index * (barWidth + spacing)
            val y = height - padding - barHeight

            drawRoundRect(
                color = category.color,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    category.name.split(" ").first(),
                    x + barWidth / 2,
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
