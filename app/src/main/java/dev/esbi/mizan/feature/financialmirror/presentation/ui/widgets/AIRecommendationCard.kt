package dev.esbi.mizan.feature.financialmirror.presentation.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.financialmirror.domain.model.AIRecommendation
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
internal fun AIRecommendationCard(recommendation: AIRecommendation) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MizanTheme.premium.colors.primary.copy(alpha = 0.15f),
                        MizanTheme.premium.colors.secondary.copy(alpha = 0.15f)
                    )
                )
            )
            .border(
                1.dp,
                MizanTheme.premium.glass.border,
                RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MizanTheme.premium.spacing.xl)
        ) {
            Text("🤖", fontSize = 32.sp)
            Spacer(Modifier.width(MizanTheme.premium.spacing.md))
            Column {
                Text(
                    "AI Recommendation",
                    style = MizanTheme.premium.typography.headingSm,
                    color = MizanTheme.premium.text.primary
                )
                Spacer(Modifier.height(MizanTheme.premium.spacing.sm))
                Text(
                    "Based on your spending patterns and goals, we recommend:",
                    style = MizanTheme.premium.typography.bodyMd,
                    color = MizanTheme.premium.text.secondary
                )
                Spacer(Modifier.height(MizanTheme.premium.spacing.sm))
                recommendation.recommendations.forEach { rec ->
                    Text(
                        "• $rec",
                        style = MizanTheme.premium.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                    Spacer(Modifier.height(MizanTheme.premium.spacing.xs))
                }
            }
        }
    }
}
