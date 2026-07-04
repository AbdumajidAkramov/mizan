package dev.esbi.mizan.feature.statistics.presentation.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.kit.glass.GlassCard
import dev.esbi.mizan.design.theme.colors.MizanTheme

@Composable
internal fun InsightsCard(modifier: Modifier = Modifier) {
    GlassCard {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💡",
                    fontSize = 20.sp
                )
                Text(
                    text = "Insights",
                    style = MizanTheme.typography.headingMd,
                    color = MizanTheme.premium.text.primary
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            InsightItem(
                title = "Your spending decreased by 8.3% this month",
                description = "Great job managing your expenses!",
                barColor = Brush.verticalGradient(
                    colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            InsightItem(
                title = "Bills category is 30% of total spending",
                description = "Consider reviewing your subscriptions",
                barColor = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF5576C), Color(0xFFFFA34D))
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            InsightItem(
                title = "You saved $1,245 this month",
                description = "You're on track to meet your savings goal",
                barColor = Brush.verticalGradient(
                    colors = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE))
                )
            )
        }

    }
}

@Composable
internal fun InsightItem(
    title: String,
    description: String,
    barColor: Brush,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min), // Row balandligini ichidagi elementlarning eng kichik (lekin yetarli) balandligiga moslaydi
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight() // Endi bu Box Row-ning jami balandligini to'liq egallaydi
                .clip(RoundedCornerShape(2.dp))
                .background(barColor)
        )
        Column(
            modifier = Modifier.padding(vertical = 4.dp) // Chiziq matnga yopishib qolmasligi uchun ozgina padding
        ) {
            Text(
                text = title,
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
        }
    }}
