package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.dashboard.domain.model.WeeklySpendingPoint
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.Purple
import dev.esbi.mizan.ui.theme.TextWhite

@Preview
@Composable
private fun ChartCardPreview() {
    MizanTheme {
        ChartCard(data = emptyList())
    }
}

@Composable
fun ChartCard(data: List<WeeklySpendingPoint>) {
    GlassCard {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text(
                    "Spending Overview",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Details", style = MaterialTheme.typography.bodySmall, color = Purple)
                    Spacer(Modifier.width(4.dp))
                    Text("→", color = Purple)
                }
            }
            Spacer(Modifier.height(24.dp))
            SpendingChart(
                data, Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }
    }
}
