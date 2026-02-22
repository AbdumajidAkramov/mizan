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
    fractionFontSize: TextUnit = 24.sp // Tiyinlar o'lchami
): AnnotatedString {
    // 1. Formatni sozlash (12 312.12 ko'rinishi uchun)
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' ' // Mingliklarni bo'shliq bilan ajratish
        decimalSeparator = '.'  // Nuqta bilan tiyinlarni ajratish
    }
    
    // ".00" qismi doim chiqishi uchun "#,##0.00" ishlatamiz
    val formatter = DecimalFormat("#,##0.00", symbols)
    val formattedString = formatter.format(amount) // Masalan: "12 312.12"

    // 2. AnnotatedString qurish
    return buildAnnotatedString {
        val parts = formattedString.split(".")
        val integerPart = parts[0]
        val fractionalPart = if (parts.size > 1) parts[1] else "00"

        // Butun qismini oddiy (katta) yozamiz
        append(integerPart)

        // Nuqta va tiyin qismini kichikroq style bilan yozamiz
        withStyle(style = SpanStyle(fontSize = fractionFontSize)) {
            append(".$fractionalPart")
        }
    }
}