package dev.esbi.mizan.ui.components.account

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.kit.balance.BalanceAmount
import dev.esbi.mizan.ui.kit.balance.BalanceAmountWithPrecision
import dev.esbi.mizan.ui.kit.glass.GlassCard
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
    modifier: Modifier = Modifier,
    formattedBalance: String? = null,
    abbreviatedBalance: String? = null,
    enableLongPressPrecision: Boolean = false
) {
    val changeFormatter = NumberFormat.getNumberInstance(Locale("uz", "UZ")).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 2
    }
    val formattedChange = changeFormatter.format(kotlin.math.abs(monthlyChange)).replace(",", ".")
    val isPositive = monthlyChange >= 0

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.1f),
                        Color.White.copy(alpha = 0.05f)
                    )
                )
            )
            .padding(
                horizontal = MizanTheme.premium.spacing.xl,
                vertical = MizanTheme.premium.spacing.md
            )

    ) {
        // Emerald Glow Effect
        Box(
            modifier = Modifier
                .size(150.dp)
                .graphicsLayer { alpha = 0.3f }
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MizanTheme.premium.colors.emerald.copy(alpha = 0.6f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(modifier = Modifier) {
            Text(
                text = "Total Balance",
                style = MizanTheme.typography.bodySm,
                color = Color.White.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(8.dp))
            
            when {
                // Long-press precision mode with abbreviated display
                enableLongPressPrecision && abbreviatedBalance != null && formattedBalance != null -> {
                    BalanceAmountWithPrecision(
                        balance = balance,
                        currency = currency,
                        fullPrecisionText = formattedBalance,
                        abbreviatedText = abbreviatedBalance,
                        typography = MizanTheme.typography.displaySm
                    )
                }
                // Pre-formatted balance (legacy support)
                formattedBalance != null -> {
                    Text(
                        text = formattedBalance,
                        style = MizanTheme.typography.displaySm,
                        color = MizanTheme.premium.colors.emerald
                    )
                }
                // Default balance display
                else -> {
                    BalanceAmount(
                        balance = balance,
                        currency = currency,
                        typography = MizanTheme.typography.displaySm
                    )
                }
            }

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
                    color = MizanTheme.premium.colors.emerald.copy(alpha = 0.7f)
                )
            }
        }
    }
}
