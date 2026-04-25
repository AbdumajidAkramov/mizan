package dev.esbi.mizan.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.utils.Strings
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PremiumNetWorthCard(
    netWorth: Double,
    changeAmount: Double,
    changePercentage: Double,
    trendData: List<Double>,
    modifier: Modifier = Modifier
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }

    val isPositive = changeAmount >= 0
    val trendColor = if (isPositive) Color(0xFF00F2FE) else Color(0xFFFF6B6B)

    PremiumCard(
        variant = PremiumCardVariant.Glass,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(Strings.dashboard_net_worth),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = numberFormat.format(netWorth),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "${if (isPositive) "+" else ""}${numberFormat.format(changeAmount)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = trendColor
                    )
                    Text(
                        text = "${if (isPositive) "+" else ""}${"%.1f".format(changePercentage)}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                if (trendData.size < 2) return@Canvas

                val width = size.width
                val height = size.height
                val maxValue = trendData.maxOrNull() ?: 1.0
                val minValue = trendData.minOrNull() ?: 0.0
                val range = maxValue - minValue

                val stepX = width / (trendData.size - 1)

                val path = Path().apply {
                    trendData.forEachIndexed { index, value ->
                        val x = index * stepX
                        val normalizedValue = if (range > 0) ((value - minValue) / range) else 0.5
                        val y = height - (normalizedValue * height * 0.8f).toFloat() - height * 0.1f

                        if (index == 0) {
                            moveTo(x, y)
                        } else {
                            lineTo(x, y)
                        }
                    }
                }

                drawPath(
                    path = path,
                    color = trendColor,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )

                trendData.forEachIndexed { index, value ->
                    val x = index * stepX
                    val normalizedValue = if (range > 0) ((value - minValue) / range) else 0.5
                    val y = height - (normalizedValue * height * 0.8f).toFloat() - height * 0.1f

                    drawCircle(
                        color = trendColor,
                        radius = 4.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
}
