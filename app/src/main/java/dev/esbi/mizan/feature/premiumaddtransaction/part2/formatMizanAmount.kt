package dev.esbi.mizan.feature.premiumaddtransaction.part2

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
fun formatMizanAmount(
    amount: BigDecimal,
    currency: String? = null, // Yangi parametr
    fractionFontSize: TextUnit = 24.sp
): AnnotatedString {
    // 1. Formatni sozlash
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val formatter = DecimalFormat("#,##0.00", symbols)
    val formattedString = formatter.format(amount)

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