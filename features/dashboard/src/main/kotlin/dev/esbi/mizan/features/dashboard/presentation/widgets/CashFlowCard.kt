package dev.esbi.mizan.features.dashboard.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.kit.glass.GlassCard
import dev.esbi.mizan.design.theme.Cyan
import dev.esbi.mizan.design.theme.TextGray
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CashFlowCard(income: Double, expenses: Double) {
    val flow = income - expenses
    val fmt = NumberFormat.getNumberInstance(Locale.US)
    GlassCard {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text("Monthly Cash Flow", style = MaterialTheme.typography.bodyMedium, color = TextGray)
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Cyan)
                    .padding(16.dp, 8.dp)
            ) {
                Text(
                    "+${fmt.format(flow.toInt())}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003333)
                )
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(24.dp)) {
                FlowItem(true, income)
                FlowItem(false, expenses)
            }
            Spacer(Modifier.height(16.dp))
            MiniBarChart()
        }
    }
}
