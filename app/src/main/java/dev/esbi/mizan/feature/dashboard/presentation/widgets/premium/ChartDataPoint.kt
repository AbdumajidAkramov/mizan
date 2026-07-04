package dev.esbi.mizan.feature.dashboard.presentation.widgets.premium

// Grafik ma'lumotlari uchun oddiy data class
import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.kit.glass.CardVariant
import dev.esbi.mizan.design.kit.glass.PremiumCard
import dev.esbi.mizan.design.kit.icon.IconValue
import dev.esbi.mizan.design.kit.icon.MizanIcon
import dev.esbi.mizan.design.theme.MizanTheme
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.design.utils.IconRes
import java.text.NumberFormat
import java.util.Locale

data class ChartDataPoint(val value: Float)

@Composable
fun PremiumNetWorthCard(
    netWorth: Double,
    change: Double,
    changePercent: Double,
    chartData: List<ChartDataPoint>,
    modifier: Modifier = Modifier
) {
    val isPositive = change >= 0
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    // Design Systemdan gradientni olish (Primary Gradient)
    val brandGradient = MizanTheme.premium.gradients.primary

    PremiumCard(
        variant = CardVariant.Glass,
        modifier = modifier // Clip kerak emas, PremiumCard o'zi clip qiladi
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = MizanTheme.premium.spacing.md)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            brandGradient,
                            RoundedCornerShape(MizanTheme.premium.radius.xs)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    MizanIcon(
                        icon = IconValue(IconRes.ic_attach_money),
                        contentDescription = null,
                        tint = Color.White, // Icon doim oq bo'lishi ma'qul gradient ustida
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))
                Text(
                    text = "Total Net Worth",
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.muted
                )
            }

            // --- VALUE ---
            Text(
                text = currencyFormat.format(netWorth),
                style = MizanTheme.typography.displaySm, // 32sp Bold
                color = MizanTheme.premium.text.primary,
                modifier = Modifier.padding(bottom = MizanTheme.premium.spacing.sm)
            )

            // --- INDICATORS ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = MizanTheme.premium.spacing.md)
            ) {
                val pillColor =
                    if (isPositive) MizanTheme.premium.colors.success else MizanTheme.premium.colors.error
                val pillBg = pillColor.copy(alpha = 0.2f)

                Box(
                    modifier = Modifier
                        .background(pillBg, RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        MizanIcon(
                            icon = IconValue(if (isPositive) IconRes.ic_trend_up else IconRes.ic_down_trend),
                            contentDescription = null,
                            tint = pillColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${if (isPositive) "+" else ""}${currencyFormat.format(change)}",
                            color = pillColor,
                            style = MizanTheme.typography.bodyXs // Fontni kichraytirdim, chiroyli turishi uchun
                        )
                    }
                }

                Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))

                Text(
                    text = "(${if (isPositive) "+" else ""}$changePercent%) this month",
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }

            // --- MINI CHART ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                MiniTrendChart(
                    data = chartData,
                    brush = brandGradient, // Grafik ham o'sha gradientda chiziladi
                    modifier = Modifier.matchParentSize()
                )
            }
        }
    }
}

// MiniTrendChart o'zgarishsiz qoladi, u faqat Brush qabul qiladi
@Composable
fun MiniTrendChart(
    data: List<ChartDataPoint>,
    brush: Brush,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val minVal = data.minOf { it.value }
        val maxVal = data.maxOf { it.value }
        val range = maxVal - minVal
        val path = Path()

        data.forEachIndexed { index, point ->
            val x = (index.toFloat() / (data.size - 1)) * width
            val normalizedY = (point.value - minVal) / (if (range == 0f) 1f else range)
            val y = height - (normalizedY * height)
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(
            path = path,
            brush = brush,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Preview(
    name = "Dark Mode - Positive Trend",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PremiumNetWorthCardPreview() {
    MizanTheme {
        PremiumNetWorthCard(
            netWorth = 24500.00,
            change = 1250.00,
            changePercent = 5.4,
            chartData = listOf(
                ChartDataPoint(18500f),
                ChartDataPoint(19200f),
                ChartDataPoint(18800f),
                ChartDataPoint(22450f)
            )
        )
    }
}

@Preview(
    name = "Dark Mode - Negative Trend",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun PremiumNetWorthCardNegativePreview() {
    MizanTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            PremiumNetWorthCard(
                netWorth = 18200.00,
                change = -450.00,     // Manfiy o'zgarish
                changePercent = -2.1, // Manfiy foiz
                chartData = listOf(
                    ChartDataPoint(22000f),
                    ChartDataPoint(21000f),
                    ChartDataPoint(19500f),
                    ChartDataPoint(18200f)
                )
            )
        }
    }
}
