package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.CardBg
import dev.esbi.mizan.ui.theme.Cyan
import dev.esbi.mizan.ui.theme.Orange
import dev.esbi.mizan.ui.theme.Red
import dev.esbi.mizan.ui.theme.TextGray
import dev.esbi.mizan.ui.theme.TextMuted
import dev.esbi.mizan.ui.theme.TextWhite
import dev.esbi.mizan.ui.utils.Icons
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StatsRow(budget: Double, spent: Double, limit: Double, savings: Double) {
    val fmt = NumberFormat.getCurrencyInstance(Locale.US)
    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
        GlassCard(Modifier.weight(1f)) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Orange, Red))), Alignment.Center
                    ) {
                        MizanIcon(
                            icon = IconValue(Icons.ic_trend_up),
                            modifier = Modifier.size(16.dp),
                            tint = TextWhite
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Budget Used",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "${budget.toInt()}%",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    "${fmt.format(spent)} of ${fmt.format(limit)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(CardBg)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth((budget / 100).toFloat().coerceIn(0f, 1f))
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Brush.horizontalGradient(listOf(Orange, Red)))
                    )
                }
            }
        }
        GlassCard(Modifier.weight(1f)) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Cyan), Alignment.Center
                    ) {
                        MizanIcon(
                            icon = IconValue(Icons.ic_calendar_month),
                            modifier = Modifier.size(16.dp),
                            tint = TextWhite
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("This Month", style = MaterialTheme.typography.bodySmall, color = TextGray)
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    fmt.format(savings),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    "+12.5% from last month",
                    style = MaterialTheme.typography.bodySmall,
                    color = Cyan
                )
                Spacer(Modifier.height(14.dp))
            }
        }
    }
}
