package dev.esbi.mizan.feature.statistics.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.statistics.domain.model.Insight
import dev.esbi.mizan.feature.statistics.domain.model.InsightType
import dev.esbi.mizan.design.components.PremiumCard
import dev.esbi.mizan.design.components.PremiumCardVariant
import dev.esbi.mizan.design.theme.PremiumColors
import dev.esbi.mizan.design.utils.Strings

@Composable
fun InsightsCard(
    insights: List<Insight>,
    modifier: Modifier = Modifier
) {
    PremiumCard(
        variant = PremiumCardVariant.Glass,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "💡",
                    fontSize = 20.sp
                )
                Text(
                    text = stringResource(Strings.statistics_insights),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PremiumColors.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                insights.forEach { insight ->
                    InsightItem(insight = insight)
                }
            }
        }
    }
}

@Composable
private fun InsightItem(
    insight: Insight,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(60.dp)
                .background(
                    brush = getInsightGradient(insight.type),
                    shape = RoundedCornerShape(2.dp)
                )
        )
        Column {
            Text(
                text = insight.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PremiumColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = insight.description,
                fontSize = 12.sp,
                color = PremiumColors.TextTertiary
            )
        }
    }
}

private fun getInsightGradient(type: InsightType): Brush {
    return when (type) {
        InsightType.POSITIVE -> Brush.verticalGradient(
            colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
        )

        InsightType.WARNING -> Brush.verticalGradient(
            colors = listOf(Color(0xFFF5576C), Color(0xFFFFA34D))
        )

        InsightType.INFO -> Brush.verticalGradient(
            colors = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE))
        )
    }
}
