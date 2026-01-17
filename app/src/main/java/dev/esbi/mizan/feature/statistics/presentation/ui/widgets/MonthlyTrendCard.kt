package dev.esbi.mizan.feature.statistics.presentation.ui.widgets


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.components.charts.AreaChart
import dev.esbi.mizan.ui.components.charts.MizanAreaChart
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.theme.colors.MizanTheme

// 1. Data Model
data class ChartData(
    val month: String,
    val amount: Float
)

@Composable
fun MonthlyTrendChart(
    data: List<ChartData>,
    modifier: Modifier = Modifier
) {
    GlassCard {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Sarlavha
            Text(
                text = "Monthly Trend",
                color = MizanTheme.premium.text.primary,
                style = MizanTheme.typography.headingMd,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Last 6 months spending pattern",
                color = MizanTheme.premium.text.tertiary,
                style = MizanTheme.typography.bodySm
            )
            Spacer(modifier = Modifier.height(32.dp))

            MizanAreaChart(
                spendingData = data.map {
                    AreaChart(
                        dayLabel = it.month,
                        totalAmount = it.amount
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }
    }
}

// Preview
@Preview
@Composable
fun MonthlyTrendChartPreview() {
    val data = listOf(
        ChartData("Jan", 1400f),
        ChartData("Feb", 1820f),
        ChartData("Mar", 1600f),
        ChartData("Apr", 2100f),
        ChartData("May", 1900f),
        ChartData("Jun", 2400f)
    )
    MonthlyTrendChart(data = data)
}