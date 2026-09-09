package dev.esbi.mizan.features.addtransaction.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import java.math.BigDecimal

fun String.annotatedString(
    separator: Char = '.',
    decimalScale: Float = 0.6f,
    currency: String? = null
): AnnotatedString {
    val trimmed = this.trim()
    if (trimmed.isBlank()) {
        return buildAnnotatedString {
            append("0")
            withStyle(style = SpanStyle(fontSize = decimalScale.em)) {
                append(" $currency")
            }
        }
    }

    try {
        BigDecimal(trimmed)
    } catch (e: NumberFormatException) {
        if (trimmed == ".") {
            return buildAnnotatedString {
                append("0.")
                withStyle(style = SpanStyle(fontSize = decimalScale.em)) {
                    append(" $currency")
                }
            }
        }
        return buildAnnotatedString { append(trimmed) }
    }

    val fullText = formatGroupedNumber(trimmed)
    val separatorIndex = fullText.lastIndexOf(separator)

    if (separatorIndex == -1) return buildAnnotatedString {
        append(fullText)
        withStyle(style = SpanStyle(fontSize = decimalScale.em)) {
            currency?.let {
                append(" $currency")
            }
        }
    }
    return buildAnnotatedString {
        append(fullText.take(separatorIndex))
        withStyle(style = SpanStyle(fontSize = decimalScale.em)) {
            append(fullText.substring(separatorIndex))
            currency?.let {
                append(" $currency")
            }
        }
    }
}

fun formatGroupedNumber(input: String): String {
    val s = input.trim()
    if (s.isEmpty()) return s

    val parts = s.split('.', limit = 2)
    val intPartRaw = parts[0]
    val fracPartRaw = parts.getOrNull(1)

    val isNegative = intPartRaw.startsWith("-")
    val intDigits = if (isNegative) intPartRaw.drop(1) else intPartRaw

    val intGrouped = intDigits
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()

    val fracGrouped = fracPartRaw
        ?.chunked(3)
        ?.joinToString(" ")
        ?.take(6)

    val sign = if (isNegative) "-" else ""
    return when {
        fracGrouped != null && (fracPartRaw.toIntOrNull() ?: 0) > 0
            -> "$sign$intGrouped.$fracGrouped"

        input.endsWith('.') -> "$sign$intGrouped."
        else -> "$sign$intGrouped"
    }
}
