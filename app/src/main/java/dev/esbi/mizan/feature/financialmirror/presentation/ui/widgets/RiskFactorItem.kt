package dev.esbi.mizan.feature.financialmirror.presentation.ui.widgets

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.RiskFactor
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.RiskStatus
import dev.esbi.mizan.design.theme.colors.MizanTheme

@Composable
internal fun RiskFactorItem(risk: RiskFactor) {
    val color = when (risk.status) {
        RiskStatus.GOOD -> MizanTheme.premium.colors.success
        RiskStatus.FAIR -> MizanTheme.premium.colors.primary
        RiskStatus.WARNING -> MizanTheme.premium.colors.warning
    }

    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(risk.score) {
        animProgress.animateTo(risk.score / 100f, tween(800, easing = FastOutSlowInEasing))
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = risk.category,
                    style = MizanTheme.premium.typography.labelMd,
                    color = MizanTheme.premium.text.primary
                )
                Text(
                    text = risk.description,
                    style = MizanTheme.premium.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    risk.score.toString(),
                    style = MizanTheme.premium.typography.headingMd,
                    color = color
                )
                Text(
                    "/ 100",
                    style = MizanTheme.premium.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }
        }

        Spacer(Modifier.height(MizanTheme.premium.spacing.sm))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MizanTheme.premium.background.secondary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = animProgress.value)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
    }
}

@Preview(
    name = "Risk Factors List",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun RiskFactorItemPreview() {
    // Preview uchun saxta ma'lumotlar
    val riskFactors = listOf(
        RiskFactor(
            category = "Emergency Fund",
            score = 85,
            status = RiskStatus.GOOD,
            description = "Strong 6-month coverage"
        ),
        RiskFactor(
            category = "Debt-to-Income",
            score = 72,
            status = RiskStatus.FAIR,
            description = "Manageable debt levels"
        ),
        RiskFactor(
            category = "Diversification",
            score = 45,
            status = RiskStatus.WARNING,
            description = "Needs improvement"
        )
    )

    // Agar sizda Theme bo'lsa, uni shu yerda o'rab qo'yishingiz mumkin.
    // Masalan: MizanTheme { ... }
    dev.esbi.mizan.design.theme.MizanTheme() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column {
                riskFactors.forEachIndexed { index, risk ->
                    RiskFactorItem(risk = risk)

                    if (index < riskFactors.lastIndex) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}