package dev.esbi.mizan.ui.kit.balance

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
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun BalanceAmount(
    balance: BigDecimal,
    currency: String,
    modifier: Modifier = Modifier,
    typography: TextStyle = MizanTheme.premium.typography.bodyLg
) {
    val amountColor: Color = if (balance < BigDecimal.ZERO)
        MizanTheme.premium.colors.error
    else
        MizanTheme.premium.colors.emerald

    Box(modifier = modifier) {
        Text(
            text = amountFormat(
                amount = balance,
                currency = currency
            ),
            style = typography,
            color = amountColor,
            modifier = Modifier.padding(vertical = MizanTheme.premium.spacing.sm)
        )
    }
}

fun amountFormat(
    amount: BigDecimal,
    currency: String? = null, // Yangi parametr
    amountSize: TextUnit = 24.sp
): AnnotatedString {
    // 1. Formatni sozlash
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val formatter = DecimalFormat("#,##0.00", symbols)
    val formattedString = formatter.format(amount)
    val fractionFontSize: TextUnit = amountSize.times(0.5)
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

@Preview(showBackground = true)
@Composable
fun FormattableAmountPreview() {
    MizanTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BalanceAmount(
                balance = BigDecimal("124120.23"),
                currency = "UZS"
            )
        }
    }
}