package dev.esbi.mizan.ui.components

import android.R
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.PremiumColors

data class AIInsight(
    val id: String,
    val type: AIInsightType,
    val title: String,
    val description: String,
    val impact: String? = null
)

enum class AIInsightType {
    SUCCESS,
    WARNING,
    INFO,
    GOAL
}

@Composable
fun PremiumAIInsights(
    insights: List<AIInsight>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.star_on),
                contentDescription = null,
                tint = Color(0xFF667EEA),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "AI Insights",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = PremiumColors.TextPrimary
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                        )
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Smart",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            insights.forEach { insight ->
                AIInsightCard(insight = insight)
            }
        }
    }
}

@Composable
private fun AIInsightCard(
    insight: AIInsight,
    modifier: Modifier = Modifier
) {
    val (iconRes, color) = when (insight.type) {
        AIInsightType.SUCCESS -> R.drawable.arrow_up_float to Color(0xFF00F2A0)
        AIInsightType.WARNING -> R.drawable.ic_dialog_alert to Color(0xFFFFA34D)
        AIInsightType.GOAL -> R.drawable.ic_menu_compass to Color(0xFF4FACFE)
        AIInsightType.INFO -> R.drawable.ic_menu_info_details to Color(0xFF667EEA)
    }

    PremiumCard(
        variant = PremiumCardVariant.Glass,
        modifier = modifier.width(280.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(18.dp)
                    )
                }

                insight.impact?.let { impact ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(color.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = impact,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = color
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = insight.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PremiumColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = insight.description,
                fontSize = 12.sp,
                color = PremiumColors.TextTertiary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
