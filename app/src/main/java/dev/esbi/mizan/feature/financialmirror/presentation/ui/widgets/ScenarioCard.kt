package dev.esbi.mizan.feature.financialmirror.presentation.ui.widgets

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.ScenarioIconType
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.TimeMachineScenario
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@Composable
internal fun ScenarioCard(scenario: TimeMachineScenario) {
    val iconRes = when (scenario.iconType) {
        ScenarioIconType.TARGET -> Icons.ic_ai_insight
        ScenarioIconType.TRENDING_UP -> Icons.ic_trend_up
        ScenarioIconType.SPARKLES -> Icons.ic_ai_insight
        ScenarioIconType.DOLLAR_SIGN -> Icons.ic_wallet
        ScenarioIconType.PIGGY_BANK -> Icons.ic_wallet
    }

    GlassCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MizanTheme.premium.spacing.xl),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                    .background(MizanTheme.premium.gradients.primary),
                contentAlignment = Alignment.Center
            ) {
                MizanIcon(
                    icon = IconValue(iconRes),
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }

            Spacer(Modifier.width(MizanTheme.premium.spacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = scenario.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MizanTheme.premium.typography.labelLg,
                    color = MizanTheme.premium.text.primary
                )
                Spacer(Modifier.height(MizanTheme.premium.spacing.xs))
                Text(
                    scenario.description,
                    style = MizanTheme.premium.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(MizanTheme.premium.spacing.sm))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                            .background(MizanTheme.premium.colors.success.copy(alpha = 0.2f))
                            .padding(
                                horizontal = MizanTheme.premium.spacing.sm,
                                vertical = MizanTheme.premium.spacing.xs
                            )
                    ) {
                        Text(
                            scenario.impact,
                            style = MizanTheme.premium.typography.labelSm,
                            color = MizanTheme.premium.colors.success
                        )
                    }
                    Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
                    Text(
                        "in ${scenario.timeline}",
                        style = MizanTheme.premium.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
            }

            MizanIcon(
                icon = IconValue(Icons.ic_arrow_right),
                modifier = Modifier.size(20.dp),
                tint = MizanTheme.premium.text.tertiary
            )
        }
    }
}

