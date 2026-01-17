package dev.esbi.mizan.feature.dashboard.presentation.widgets.premium

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.components.charts.AreaChart
import dev.esbi.mizan.ui.components.charts.MizanAreaChart
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.theme.colors.MizanTheme

data class SpendingPoint(
    val dayLabel: String,
    val totalAmount: Float
)

@Composable
fun PremiumSpendingChart(
    data: List<SpendingPoint>,
    modifier: Modifier = Modifier,
    onDetailsClick: () -> Unit = {}
) {

    GlassCard {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // --- HEADER ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Spending Overview",
                    style = MizanTheme.typography.headingMd,
                    color = MizanTheme.premium.text.primary
                )

                Text(
                    text = "See all",
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.colors.primary,
                    modifier = Modifier.clickable(onClick = onDetailsClick)
                )
            }
            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            Spacer(modifier = Modifier.height(16.dp))

            MizanAreaChart(
                spendingData = data.map {
                    AreaChart(
                        dayLabel = it.dayLabel,
                        totalAmount = it.totalAmount
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }
    }

}

// --- PREVIEW ---
@Preview(showBackground = true, backgroundColor = 0xFF181829) // Rasmga o'xshash to'q fon
@Composable
fun FixedChartPreview() {
    // Rasmga o'xshash ma'lumotlar
    val mockData = listOf(
        SpendingPoint(
            "Mon",
            185f
        ),
        SpendingPoint(
            "Tue",
            120f
        ),
        SpendingPoint(
            "Wed",
            120f
        ), // Flat qism
        SpendingPoint(
            "Thu",
            250f
        ), // Cho'qqi
        SpendingPoint(
            "Fri",
            20f
        ),  // Past
        SpendingPoint(
            "Sat",
            60f
        ),
        SpendingPoint(
            "Sun",
            170f
        )
    )

    Box(Modifier.padding(16.dp)) {
        PremiumSpendingChart(data = mockData)
    }
}