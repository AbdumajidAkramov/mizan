package dev.esbi.mizan.feature.financialmirror.presentation.ui.widgets

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.financialmirror.domain.model.RiskAnalysis
import dev.esbi.mizan.feature.financialmirror.domain.model.RiskStatus
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@Composable
internal fun RiskAnalysisCard(riskAnalysis: RiskAnalysis) {
    GlassCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MizanTheme.premium.spacing.lg)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MizanTheme.premium.colors.warning.copy(0.2f)),
                    Alignment.Center
                ) {
                    Icon(
                        icon = IconValue(Icons.ic_shield),
                        modifier = Modifier.size(16.dp),
                        tint = MizanTheme.premium.colors.warning
                    )
                }
                Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
                Text(
                    "Financial Risk Assessment",
                    style = MizanTheme.premium.typography.headingSm,
                    color = MizanTheme.premium.text.primary
                )
            }

            Spacer(Modifier.height(MizanTheme.premium.spacing.xl))

            riskAnalysis.riskFactors.forEach { risk ->
                RiskFactorItem(risk)
                Spacer(Modifier.height(MizanTheme.premium.spacing.md))
            }

            // Overall Risk Score
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                    .background(MizanTheme.premium.background.secondary)
                    .padding(MizanTheme.premium.spacing.md)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Overall Risk Score",
                            style = MizanTheme.premium.typography.bodySm,
                            color = MizanTheme.premium.text.tertiary
                        )
                        Spacer(Modifier.height(MizanTheme.premium.spacing.xs))
                        Text(
                            "${riskAnalysis.overallScore}/100",
                            style = MizanTheme.premium.typography.headingXl,
                            color = MizanTheme.premium.text.primary
                        )
                    }

                    val statusColor = when (riskAnalysis.overallStatus) {
                        RiskStatus.GOOD -> MizanTheme.premium.colors.success
                        RiskStatus.FAIR -> MizanTheme.premium.colors.primary
                        RiskStatus.WARNING -> MizanTheme.premium.colors.warning
                    }
                    val statusLabel = when (riskAnalysis.overallStatus) {
                        RiskStatus.GOOD -> "Excellent"
                        RiskStatus.FAIR -> "Moderate"
                        RiskStatus.WARNING -> "Needs Work"
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                            .background(statusColor.copy(alpha = 0.2f))
                            .padding(
                                horizontal = MizanTheme.premium.spacing.md,
                                vertical = MizanTheme.premium.spacing.sm
                            )
                    ) {
                        Text(
                            statusLabel,
                            style = MizanTheme.premium.typography.labelMd,
                            color = statusColor
                        )
                    }
                }
            }
        }
    }
}
