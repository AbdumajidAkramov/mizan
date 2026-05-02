package dev.esbi.mizan.feature.newtransaction.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Ultra-compact exchange rate display combining rate and equivalent amount in a single row.
 * Saves significant vertical space compared to separate components.
 * 
 * Layout: [1 USD = 12,210.1234 UZS] | [≈ 1,230,000 UZS]
 * 
 * Features:
 * - Minimal vertical padding (4dp)
 * - Single row layout
 * - Clickable to open bottom sheet
 * - Animated equivalent amount updates
 * - Only visible for sub-currencies
 */
@Composable
fun CompactExchangeRateRow(
    selectedCurrency: Currency?,
    mainCurrency: Currency?,
    manualExchangeRate: BigDecimal?,
    equivalentAmount: BigDecimal,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Only show for sub-currencies
    if (selectedCurrency == null || mainCurrency == null || selectedCurrency.isMainCurrency) {
        return
    }

    val currentRate = manualExchangeRate ?: selectedCurrency.exchangeRate

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MizanTheme.premium.colors.surface1.copy(alpha = 0.12f),
                        MizanTheme.premium.colors.surface1.copy(alpha = 0.06f)
                    )
                )
            )
            .border(
                width = 0.5.dp,
                color = MizanTheme.premium.colors.surface1.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Exchange Rate
        Text(
            text = "1 ${selectedCurrency.code} = ${formatRate(currentRate)} ${mainCurrency.code}",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MizanTheme.premium.colors.emerald,
            modifier = Modifier.weight(1f, fill = false)
        )

        // Right: Equivalent Amount (animated)
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "≈ ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.5f)
            )

            AnimatedContent(
                targetState = formatAmount(equivalentAmount),
                transitionSpec = {
                    fadeIn(tween(200)).togetherWith(fadeOut(tween(200)))
                },
                label = "equivalent_amount"
            ) { formattedAmount ->
                Text(
                    text = formattedAmount,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            Text(
                text = " ${mainCurrency.code}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

/**
 * Format exchange rate with up to 8 decimal places.
 * Strips trailing zeros for cleaner display.
 */
private fun formatRate(rate: BigDecimal): String {
    val rounded = rate.setScale(8, RoundingMode.HALF_UP)
    return rounded.stripTrailingZeros().toPlainString()
}

/**
 * Format amount with thousand separators and 2 decimal places.
 */
private fun formatAmount(amount: BigDecimal): String {
    val rounded = amount.setScale(2, RoundingMode.HALF_UP)
    return String.format("%,.2f", rounded.toDouble())
        .replace(",", " ")
}

@Preview(name = "Fully Populated", showBackground = true)
@Composable
private fun CompactExchangeRateRowPreview() {
    val mainCurrency = Currency.TMP_USD
    val selectedCurrency = Currency.UZS

    dev.esbi.mizan.ui.theme.MizanTheme {
        Surface {
            CompactExchangeRateRow(
                selectedCurrency = selectedCurrency,
                mainCurrency = mainCurrency,
                manualExchangeRate = BigDecimal("0.92"),
                equivalentAmount = BigDecimal("92.00"),
                onClick = { }
            )
        }
    }
}

@Preview(name = "No Manual Rate (Null)", showBackground = true)
@Composable
private fun CompactExchangeRateRowNoManualRatePreview() {

    dev.esbi.mizan.ui.theme.MizanTheme {
        Surface {
            CompactExchangeRateRow(
                selectedCurrency = Currency.TMP_USD,
                mainCurrency = Currency.UZS,
                manualExchangeRate = null,
                equivalentAmount = BigDecimal("79.50"),
                onClick = { }
            )
        }
    }
}

@Preview(name = "Missing Currencies (Null)", showBackground = true)
@Composable
private fun CompactExchangeRateRowNullCurrenciesPreview() {
    dev.esbi.mizan.ui.theme.MizanTheme  {
        Surface {
            CompactExchangeRateRow(
                selectedCurrency = null,
                mainCurrency = null,
                manualExchangeRate = BigDecimal("1.00"),
                equivalentAmount = BigDecimal("0.00"),
                onClick = { }
            )
        }
    }
}