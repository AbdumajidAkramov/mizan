package dev.esbi.mizan.feature.dashboard.presentation.widgets

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.Cyan
import dev.esbi.mizan.ui.theme.Purple2
import dev.esbi.mizan.ui.theme.TextGray
import dev.esbi.mizan.ui.theme.TextMuted
import dev.esbi.mizan.ui.theme.TextWhite
import dev.esbi.mizan.ui.utils.Icons
import java.text.NumberFormat
import java.util.Locale

@Composable
fun NetWorthCard(worth: Double, change: Double, pct: Double) {
    val fmt = NumberFormat.getCurrencyInstance(Locale.US)
    GlassCard() {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Purple2), Alignment.Center
                ) {
                    Text("$", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    "Total Net Worth",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                fmt.format(worth),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Cyan.copy(0.2f))
                        .padding(8.dp, 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        MizanIcon(
                            icon = IconValue(Icons.ic_trend_up),
                            modifier = Modifier.size(14.dp),
                            tint = Cyan
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "+${fmt.format(change).replace("$", "")}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Cyan
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "(+$pct%) this month",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
            Spacer(Modifier.height(16.dp))
            Sparkline(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            )
        }
    }
}
