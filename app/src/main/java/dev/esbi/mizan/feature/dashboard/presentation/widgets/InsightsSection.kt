package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.Cyan
import dev.esbi.mizan.ui.theme.Orange
import dev.esbi.mizan.ui.theme.Purple2
import dev.esbi.mizan.ui.theme.TextWhite

@Composable
fun InsightsSection() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("✨", fontSize = 20.sp); Spacer(Modifier.width(8.dp))
            Text(
                "AI Insights",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
            )
            Spacer(Modifier.width(8.dp))
            Box(
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Purple2)
                    .padding(8.dp, 4.dp)
            ) {
                Text("Smart", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.White)
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            Arrangement.spacedBy(12.dp)
        ) {
            InsightItem(
                "📈",
                "Great savings momentum!",
                "You saved 15% more than last month. Keep it up!",
                "+\$245",
                Cyan
            )
            InsightItem(
                "⚠️",
                "Subscription alert",
                "You have 3 unused subscriptions totaling \$47/mo.",
                null,
                Orange
            )
        }
    }
}

