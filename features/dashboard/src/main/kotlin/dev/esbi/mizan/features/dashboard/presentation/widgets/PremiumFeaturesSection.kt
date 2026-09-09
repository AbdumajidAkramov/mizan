package dev.esbi.mizan.features.dashboard.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import dev.esbi.mizan.design.theme.colors.MizanTheme

@Composable
fun PremiumFeaturesSection(
    onGoalsClick: () -> Unit,
    onSubscriptionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            "Premium Features",
            style = MizanTheme.premium.typography.headingSm,
            color = MizanTheme.premium.text.primary.copy(alpha = 0.9f)
        )
        Spacer(Modifier.height(MizanTheme.premium.spacing.md))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
        ) {
            PremiumFeatureCard(
                emoji = "🎯",
                title = "Financial Goals",
                subtitle = "Track savings",
                accentColor = Color(0xFF0EA5E9),
                onClick = onGoalsClick,
                modifier = Modifier.weight(1f)
            )
            PremiumFeatureCard(
                emoji = "💳",
                title = "Subscriptions",
                subtitle = "Manage payments",
                accentColor = Color(0xFF10B981),
                onClick = onSubscriptionsClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PremiumFeatureCard(
    emoji: String,
    title: String,
    subtitle: String,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.08f),
                        Color.White.copy(alpha = 0.03f)
                    )
                )
            )
            .border(
                1.dp,
                accentColor.copy(alpha = 0.25f),
                RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .clickable { onClick() }
            .padding(MizanTheme.premium.spacing.lg)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(
                        1.dp,
                        accentColor.copy(alpha = 0.25f),
                        RoundedCornerShape(MizanTheme.premium.radius.lg)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 22.sp)
            }
            Spacer(Modifier.height(MizanTheme.premium.spacing.md))
            Text(
                title,
                style = MizanTheme.premium.typography.labelLg,
                color = Color.White
            )
            Spacer(Modifier.height(2.dp))
            Text(
                subtitle,
                style = MizanTheme.premium.typography.bodySm,
                color = Color.White.copy(alpha = 0.4f)
            )
            Spacer(Modifier.height(MizanTheme.premium.spacing.sm))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(accentColor.copy(alpha = 0.2f))
                    .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    "Open →",
                    style = MizanTheme.premium.typography.labelSm,
                    color = accentColor
                )
            }
        }
    }
}
