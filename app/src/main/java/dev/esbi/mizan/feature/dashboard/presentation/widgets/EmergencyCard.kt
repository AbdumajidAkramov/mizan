package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.CardBg
import dev.esbi.mizan.ui.theme.CardBorderColor
import dev.esbi.mizan.ui.theme.Cyan
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.Purple
import dev.esbi.mizan.ui.theme.TextGray
import dev.esbi.mizan.ui.theme.TextMuted
import dev.esbi.mizan.ui.theme.TextWhite
import dev.esbi.mizan.ui.utils.Icons
import java.text.NumberFormat
import java.util.Locale


@Composable
fun EmergencyCard(current: Double, goal: Double, months: Int) {
    val prog = (current / goal).coerceIn(0.0, 1.0)
    val fmt = NumberFormat.getCurrencyInstance(Locale.US)
    val rem = goal - current;
    val monthly = rem / 12
    GlassCard() {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MizanIcon(
                        icon = IconValue(Icons.ic_shield),
                        modifier = Modifier.size(24.dp),
                        tint = Purple
                    )

                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Emergency Fund",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                }
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, TextMuted.copy(0.3f), RoundedCornerShape(12.dp))
                        .padding(12.dp, 6.dp)
                ) {
                    Text("$months months", fontSize = 12.sp, color = TextGray)
                }
            }
            Spacer(Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Column {
                    Text(
                        fmt.format(current),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        "of ${fmt.format(goal)} goal",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "${(prog * 100).toInt()}%",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Cyan
                    )
                    Text("complete", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }
            }
            Spacer(Modifier.height(16.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(CardBorderColor)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(prog.toFloat())
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Brush.horizontalGradient(listOf(Purple, Cyan)))
                )
            }
            Spacer(Modifier.height(16.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBg)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MizanIcon(
                        icon = IconValue(Icons.ic_trend_up),
                        modifier = Modifier.size(20.dp),
                        tint = Purple
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "${fmt.format(rem)} remaining",
                            fontWeight = FontWeight.Medium,
                            color = TextWhite
                        )
                        Text(
                            "Save ~${fmt.format(monthly)}/month to reach goal in 1 year",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EmergencyCardPreview() {
    MizanTheme {
        EmergencyCard(
            current = 8500.0,
            goal = 12000.0,
            months = 6
        )
    }
}

