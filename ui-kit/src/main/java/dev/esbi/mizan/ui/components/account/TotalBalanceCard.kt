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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// Matching #10b981 from Next.js implementation
val PremiumEmerald = Color(0xFF10B981)

@Composable
fun TotalBalanceCard(
    totalBalance: BigDecimal,
    baseCurrencyCode: String,
    modifier: Modifier = Modifier,
    monthlyChange: BigDecimal = BigDecimal("2450000"),
    monthlyChangePercent: Double = 12.5
) {
    val formatter = DecimalFormat("#,###.00", DecimalFormatSymbols(Locale.US)).apply {
        val symbols = this.decimalFormatSymbols
        symbols.groupingSeparator = ' '
        this.decimalFormatSymbols = symbols
    }

    val formatted = formatter.format(totalBalance)
    val parts = formatted.split(".")
    val integer = parts[0]
    val decimal = if (parts.size > 1) parts[1] else "00"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MizanTheme.premium.background.primary.copy(alpha = 0.1f),
                        MizanTheme.premium.background.primary.copy(alpha = 0.05f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = MizanTheme.premium.background.primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(24.dp)
            )
            // Emerald Glow simulation
            .drawBehind {
                drawCircle(
                    color = PremiumEmerald.copy(alpha = 0.2f),
                    radius = 300f,
                    center = Offset(size.width * 0.9f, 0f)
                )
            }
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "Total Balance",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = integer,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = ".$decimal",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = baseCurrencyCode,
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Monthly Change Chip
            Row(
                modifier = Modifier
                    .background(
                        color = PremiumEmerald.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = PremiumEmerald.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Using standard text for arrow as placeholder since we don't know the exact Drawable reference. 
                // Alternatively, can use an Icon here if 'ic_trending_up' is available
                Text(
                    text = "↗",
                    color = PremiumEmerald,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))

                val changeStr = DecimalFormat("#,###", DecimalFormatSymbols(Locale.US)).apply {
                    val symbols = this.decimalFormatSymbols
                    symbols.groupingSeparator = ' '
                    this.decimalFormatSymbols = symbols
                }.format(monthlyChange.toDouble())

                Text(
                    text = "+$changeStr $baseCurrencyCode",
                    color = PremiumEmerald,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "• +$monthlyChangePercent% this month",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TotalBalanceCardPreview() {
    MizanTheme() {
        Box(modifier = Modifier.padding(16.dp)) {
            TotalBalanceCard(
                totalBalance = BigDecimal("123456789"),
                baseCurrencyCode = "USD",
                monthlyChange = BigDecimal("2450000"),
                monthlyChangePercent = 12.5
            )
        }
    }
}
