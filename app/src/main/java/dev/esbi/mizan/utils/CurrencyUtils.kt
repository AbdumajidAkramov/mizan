package dev.esbi.mizan.utils // O'z package nomingizni qo'ying

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em

/**
 * Double ni valyuta formatiga o'tkazib, kasr qismini kichraytirib beradi.
 * * @param separator O'nlik ajratuvchi (nuqta yoki vergul)
 * @param decimalScale Kasr qismining o'lchami (0.7f = asosiy matnning 70% hajmi)
 */

fun String.annotatedString(
    separator: Char = '.',
    decimalScale: Float = 0.6f,
    currency: String = "UZS"
): AnnotatedString {
    // 1. Oldin oddiy string qilib olamiz: "$12 345.88"
    if (this.isBlank()) return buildAnnotatedString {
        append("0")
        withStyle(style = SpanStyle(fontSize = decimalScale.em)) {
            append(" $currency")
        }
    }

    val fullText = formatGroupedNumber(this)
    // 2. Ajratuvchi belgi (nuqta) qayerda ekanligini topamiz
    val separatorIndex = fullText.lastIndexOf(separator)

    // Agar nuqta topilmasa, oddiy matn qaytaramiz
    if (separatorIndex == -1) return buildAnnotatedString {
        append(fullText)
        withStyle(style = SpanStyle(fontSize = decimalScale.em)) {
            append(" $currency")
        }
    }
    // 3. AnnotatedString yig'amiz
    return buildAnnotatedString {
        // Butun qism (Boshidan nuqtagacha): "$12 345"
        append(fullText.take(separatorIndex))
        // Kasr qism (Nuqtadan oxirigacha): ".88"
        // Style qo'llaymiz
        withStyle(style = SpanStyle(fontSize = decimalScale.em)) {
            append(fullText.substring(separatorIndex))
            append(" $currency")
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

    // Group integer part from the right: 31212 -> 31 212
    val intGrouped = intDigits
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()

    // Group fraction part from the left (first 3, then the rest): 12342 -> 123 42
    val fracGrouped = fracPartRaw
        ?.chunked(3)
        ?.joinToString(" ")
        ?.take(FRAC_LENGTH)

    val sign = if (isNegative) "-" else ""
    return when {
        fracGrouped != null && (fracPartRaw.toIntOrNull() ?: 0) > 0
            -> "$sign$intGrouped.$fracGrouped"

        input.last() == '.' -> "$sign$intGrouped."
        else -> "$sign$intGrouped"
    }
}
