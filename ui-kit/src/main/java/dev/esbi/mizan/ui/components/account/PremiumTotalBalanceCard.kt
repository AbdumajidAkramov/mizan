package dev.esbi.mizan.ui.components.account

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.kit.balance.BalanceAmount
import dev.esbi.mizan.ui.kit.glass.CardVariant
import dev.esbi.mizan.ui.kit.glass.PremiumCard
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PremiumTotalBalanceCard(
    balance: BigDecimal,
    monthlyChange: Double,
    monthlyChangePercent: Double,
    currency: String,
    modifier: Modifier = Modifier
) {
    val formatter = NumberFormat.getNumberInstance(Locale("uz", "UZ")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    val formatted = formatter.format(balance.abs()).replace(",", ".")
    val parts = formatted.split(".")
    val integerPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1] else "00"

    val changeFormatter = NumberFormat.getNumberInstance(Locale("uz", "UZ")).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 2
    }
    val formattedChange = changeFormatter.format(kotlin.math.abs(monthlyChange)).replace(",", ".")
    val isPositive = monthlyChange >= 0
    val drawCircleColor = MizanTheme.premium.colors.emerald
    PremiumCard(
        variant = CardVariant.Glass,
        modifier = modifier,
        onClick = {}
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MizanTheme.premium.background.primary.copy(alpha = 0.1f),
                            MizanTheme.premium.background.primary.copy(alpha = 0.05f)
                        )
                    )
                )
                .border(
                    width = 0.5.dp,
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(24.dp)
                )
                .drawBehind {
                    drawCircle(
                        color = drawCircleColor.copy(alpha = 0.2f),
                        radius = 300f,
                        center = Offset(size.width * 0.9f, 0f)
                    )
                }
                .padding(
                    horizontal = MizanTheme.premium.spacing.xl,
                    vertical = MizanTheme.premium.spacing.md
                )
        ) {
            // Emerald Glow Effect
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(150.dp)
                    .graphicsLayer { alpha = 0.2f }
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MizanTheme.premium.colors.emerald.copy(
                                    alpha = 0.6f
                                ),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = "Total Balance",
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )

                Spacer(modifier = Modifier.height(8.dp))
                BalanceAmount(
                    balance = balance,
                    currency = currency,
                    typography = MizanTheme.typography.displaySm
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Monthly Change Chip
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.2f))
                        .border(
                            width = 1.dp,
                            color = MizanTheme.premium.colors.emerald.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MizanIcon(
                        icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_trend_up),
                        contentDescription = null,
                        tint = MizanTheme.premium.colors.emerald,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${if (isPositive) "+" else "-"}$formattedChange $currency",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.premium.colors.emerald
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${if (isPositive) "+" else ""}$monthlyChangePercent% this month",
                        style = MizanTheme.typography.bodyXs,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PremiumTotalBalanceCardPreview() {
    dev.esbi.mizan.ui.theme.MizanTheme() {
        Box(modifier = Modifier.padding(16.dp)) {
            PremiumTotalBalanceCard(
                balance = BigDecimal("123456789.0"),
                monthlyChange = 2450000.0,
                monthlyChangePercent = 12.5,
                currency = "USD"
            )
        }
    }
}
