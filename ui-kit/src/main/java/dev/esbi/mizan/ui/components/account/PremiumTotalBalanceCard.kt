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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
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
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PremiumTotalBalanceCard(
    balance: Double,
    monthlyChange: Double,
    monthlyChangePercent: Double,
    currency: String,
    modifier: Modifier = Modifier
) {
    val formatter = NumberFormat.getNumberInstance(Locale("uz", "UZ")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    val formatted = formatter.format(kotlin.math.abs(balance)).replace(",", ".")
    val parts = formatted.split(".")
    val integerPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1] else "00"

    val changeFormatter = NumberFormat.getNumberInstance(Locale("uz", "UZ")).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 2
    }
    val formattedChange = changeFormatter.format(kotlin.math.abs(monthlyChange)).replace(",", ".")
    val isPositive = monthlyChange >= 0

    Box(
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
            .border(
                width = 0.5.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(24.dp)
            )
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
                color = Color.White.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(8.dp))
            BalanceAmount(
                balance = BigDecimal(balance),
                currency = currency,
                typography = MizanTheme.typography.displaySm
            )
            /*
                        // Balance Display
                        Row(verticalAlignment = Alignment.Bottom) {
                            if (balance < 0) {
                                Text(
                                    text = "-",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = integerPart,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = ".$decimalPart $currency",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
            */

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
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
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
