package dev.esbi.mizan.design.kit.balance

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.theme.MizanTheme
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import dev.esbi.mizan.design.theme.colors.MizanTheme as PremiumTheme

@Composable
fun BalanceAmount(
    balance: BigDecimal,
    currency: String,
    modifier: Modifier = Modifier,
    excludeFromTotal: Boolean = false,
    isAbbreviated: Boolean = false,
    color: Color = PremiumTheme.premium.colors.emerald,
    typography: TextStyle = PremiumTheme.premium.typography.bodyLg
) {
    val amountColor: Color = when {
        excludeFromTotal -> PremiumTheme.premium.text.muted
        balance < BigDecimal.ZERO -> PremiumTheme.premium.colors.error
        else -> color
    }

    Box(modifier = modifier) {
        Text(
            text = formatBalanceAmount(
                amount = balance,
                currency = currency,
                isAbbreviated = isAbbreviated
            ),
            style = typography,
            color = amountColor,
            modifier = Modifier.padding(vertical = PremiumTheme.premium.spacing.sm)
        )
    }
}

fun formatBalanceAmount(
    amount: BigDecimal,
    currency: String? = null,
    amountSize: TextUnit = 24.sp,
    isAbbreviated: Boolean = false
): AnnotatedString {
    // Handle abbreviation for large amounts
    if (isAbbreviated) {
        return formatAbbreviatedAmount(amount, currency, amountSize)
    }
    // 1. Formatni sozlash
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val formatter = DecimalFormat("#,##0.00", symbols)
    val formattedString = formatter.format(amount)
    val fractionFontSize: TextUnit = amountSize.times(0.7)
    // 2. AnnotatedString qurish
    return buildAnnotatedString {
        val parts = formattedString.split(".")
        val integerPart = parts[0]
        val fractionalPart = if (parts.size > 1) parts[1] else "00"

        // Butun qismini oddiy (katta) yozamiz
        append(integerPart)

        // Nuqta, tiyin va valyutani kichikroq style bilan yozamiz
        withStyle(style = SpanStyle(fontSize = fractionFontSize)) {
            append(".$fractionalPart")

            // Agar valyuta berilgan bo'lsa, uni ham shu yerga qo'shamiz
            if (currency != null) {
                append(" $currency")
            }
        }
    }
}

/**
 * Format amount with K/M abbreviation for large numbers.
 */
fun formatAbbreviatedAmount(
    amount: BigDecimal,
    currency: String? = null,
    amountSize: TextUnit = 24.sp
): AnnotatedString {
    val absAmount = amount.abs()
    val million = BigDecimal("1000000")
    val thousand = BigDecimal("1000")

    val (value, suffix) = when {
        absAmount >= million -> {
            val scaled = amount.divide(million, 1, java.math.RoundingMode.HALF_EVEN)
            Pair(scaled, "M")
        }

        absAmount >= thousand -> {
            val scaled = amount.divide(thousand, 1, java.math.RoundingMode.HALF_EVEN)
            Pair(scaled, "K")
        }

        else -> {
            return formatBalanceAmount(amount, currency, amountSize, isAbbreviated = false)
        }
    }

    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val formatter = DecimalFormat("#,##0.0", symbols)
    val formattedValue = formatter.format(value)
    val fractionFontSize: TextUnit = amountSize.times(0.5)

    return buildAnnotatedString {
        append(formattedValue)
        withStyle(style = SpanStyle(fontSize = fractionFontSize)) {
            append(" $suffix")
            if (currency != null) {
                append(" $currency")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormattableAmountPreview() {
    MizanTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BalanceAmount(
                balance = BigDecimal("124120.23"),
                currency = "UZS",
                excludeFromTotal = true
            )
        }
    }
}
