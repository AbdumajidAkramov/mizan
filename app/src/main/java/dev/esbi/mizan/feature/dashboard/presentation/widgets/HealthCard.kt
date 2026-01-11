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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.Cyan
import dev.esbi.mizan.ui.theme.Purple
import dev.esbi.mizan.ui.theme.TextMuted
import dev.esbi.mizan.ui.theme.TextWhite
import dev.esbi.mizan.ui.utils.Icons

@Composable
fun HealthCard(score: Int, trend: Int) {
    GlassCard {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        icon = IconValue(Icons.ic_shield),
                        modifier = Modifier.size(24.dp),
                        tint = Purple
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Financial Health",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                }
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Cyan.copy(0.2f))
                        .padding(8.dp, 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            icon = IconValue(Icons.ic_trend_up),
                            modifier = Modifier.size(14.dp),
                            tint = Cyan
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "$trend%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Cyan
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            GaugeView(score)
            Spacer(Modifier.height(16.dp))
            Text(
                "Based on spending habits, savings rate, and budget adherence",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

