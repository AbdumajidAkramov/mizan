package dev.esbi.mizan.feature.dashboard.presentation.widgets


import android.content.res.Configuration
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.kit.glass.CardVariant
import dev.esbi.mizan.design.kit.glass.PremiumCard
import dev.esbi.mizan.design.kit.icon.IconValue
import dev.esbi.mizan.design.kit.icon.MizanIcon
import dev.esbi.mizan.design.theme.Cyan
import dev.esbi.mizan.design.theme.MizanTheme
import dev.esbi.mizan.design.theme.Purple
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.design.utils.IconRes
import dev.esbi.mizan.design.utils.Strings

@Composable
fun HealthCard(score: Int, trend: Int, modifier: Modifier = Modifier) {

    PremiumCard(
        variant = CardVariant.Glass,
        modifier = modifier // Clip kerak emas, PremiumCard o'zi clip qiladi
    ) {
        Column(
            Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MizanIcon(
                        icon = IconValue(IconRes.ic_shield),
                        modifier = Modifier.size(24.dp),
                        tint = Purple
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(Strings.dashboard_financial_health),
                        style = MizanTheme.typography.headingMd,
                        color = MizanTheme.premium.text.primary
                    )
                }
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Cyan.copy(0.2f))
                        .padding(8.dp, 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        MizanIcon(
                            icon = IconValue(IconRes.ic_trend_up),
                            modifier = Modifier.size(14.dp),
                            tint = Cyan
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "$trend%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MizanTheme.premium.colors.success
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            GaugeView(score)
            Spacer(Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(color = MizanTheme.premium.text.muted)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Based on spending habits, savings rate, and budget adherence",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun HealthCardPreviewDark() {
    MizanTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            HealthCard(87, 10)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
internal fun HealthCardPreviewLight() {
    MizanTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            HealthCard(89, 10)
        }
    }
}
