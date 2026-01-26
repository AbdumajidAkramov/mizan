package dev.esbi.mizan.feature.financialmirror.presentation.ui.widgets

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.financialmirror.domain.model.InvestmentOpportunity
import dev.esbi.mizan.feature.financialmirror.domain.model.InvestmentType
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@Composable
internal fun InvestmentOpportunitiesSection(opportunities: List<InvestmentOpportunity>) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("⚡", fontSize = 20.sp)
            Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
            Text(
                "Investment Opportunities",
                style = MizanTheme.premium.typography.headingSm,
                color = MizanTheme.premium.text.primary
            )
            Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                    .background(MizanTheme.premium.colors.warning.copy(alpha = 0.3f))
                    .padding(
                        horizontal = MizanTheme.premium.spacing.sm,
                        vertical = MizanTheme.premium.spacing.xs
                    )
            ) {
                Text(
                    "AI Curated",
                    style = MizanTheme.premium.typography.labelSm,
                    color = MizanTheme.premium.colors.warning
                )
            }
        }

        Spacer(Modifier.height(MizanTheme.premium.spacing.md))

        opportunities.forEach { opportunity ->
            InvestmentCard(opportunity)
            Spacer(Modifier.height(MizanTheme.premium.spacing.sm))
        }
    }
}

@Composable
internal fun InvestmentCard(opportunity: InvestmentOpportunity) {
    val typeColor = when (opportunity.type) {
        InvestmentType.LOW_RISK -> MizanTheme.premium.colors.success
        InvestmentType.MEDIUM_RISK -> MizanTheme.premium.colors.primary
        InvestmentType.HIGH_RISK -> MizanTheme.premium.categories.shopping
        InvestmentType.LONG_TERM -> MizanTheme.premium.colors.primary
    }
    val typeLabel = when (opportunity.type) {
        InvestmentType.LOW_RISK -> "Low Risk"
        InvestmentType.MEDIUM_RISK -> "Medium Risk"
        InvestmentType.HIGH_RISK -> "High Risk"
        InvestmentType.LONG_TERM -> "Long-term"
    }

    GlassCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MizanTheme.premium.spacing.xl)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        opportunity.title,
                        style = MizanTheme.premium.typography.headingSm,
                        color = MizanTheme.premium.text.primary
                    )
                    Spacer(Modifier.height(MizanTheme.premium.spacing.sm))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                                .background(typeColor.copy(alpha = 0.2f))
                                .padding(
                                    horizontal = MizanTheme.premium.spacing.sm,
                                    vertical = MizanTheme.premium.spacing.xs
                                )
                        ) {
                            Text(
                                typeLabel,
                                style = MizanTheme.premium.typography.labelSm,
                                color = typeColor
                            )
                        }
                        Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
                        Text(
                            "Min: ${opportunity.minAmount}",
                            style = MizanTheme.premium.typography.bodySm,
                            color = MizanTheme.premium.text.tertiary
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        opportunity.apy,
                        style = MizanTheme.premium.typography.headingLg,
                        color = typeColor
                    )
                    Text(
                        "APY",
                        style = MizanTheme.premium.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
            }

            Spacer(Modifier.height(MizanTheme.premium.spacing.sm))

            Text(
                opportunity.description,
                style = MizanTheme.premium.typography.bodyMd,
                color = MizanTheme.premium.text.tertiary
            )

            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            // Gradient Learn More Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                    .background(MizanTheme.premium.gradients.primary)
                    .clickable { }
                    .padding(vertical = MizanTheme.premium.spacing.sm),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Learn More",
                        style = MizanTheme.premium.typography.labelMd,
                        color = Color.White
                    )
                    Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
                    MizanIcon(
                        icon = IconValue(Icons.ic_trend_up),
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}
