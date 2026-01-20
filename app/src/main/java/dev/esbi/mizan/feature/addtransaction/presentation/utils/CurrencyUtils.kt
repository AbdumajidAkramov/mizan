package dev.esbi.mizan.feature.addtransaction.presentation.utils // O'z package nomingizni qo'ying

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CurrencyUtils {

    fun format(amount: Double, separator: Char = '.'): String {
        // Maxsus belgilarni sozlash
        val symbols = DecimalFormatSymbols(Locale.US).apply {
            groupingSeparator = ' ' // Mingliklarni ajratuvchi (Space)
            decimalSeparator = separator  // O'nliklarni ajratuvchi (Dot)
        }

        // Format shabloni: #,##0.00 (Mecburiy 2 ta kasr raqam)
        val decimalFormat = DecimalFormat("#,##0.00", symbols)

        return "$" + decimalFormat.format(amount)
    }

}

// Yoki Extension funksiya sifatida (qulayroq bo'lishi mumkin)
fun Double.toCurrencyString(): String {
    return CurrencyUtils.format(this)
}

//object CurrencyUtils {
//    fun format(amount: Double, separator: Char = '.'): String {
//        val symbols = DecimalFormatSymbols(Locale.US).apply {
//            groupingSeparator = ' '
//            decimalSeparator = separator
//        }
//        val decimalFormat = DecimalFormat("#,##0.00", symbols)
//        return "$" + decimalFormat.format(amount)
//    }
//}

/**
 * Double ni valyuta formatiga o'tkazib, kasr qismini kichraytirib beradi.
 * * @param separator O'nlik ajratuvchi (nuqta yoki vergul)
 * @param decimalScale Kasr qismining o'lchami (0.7f = asosiy matnning 70% hajmi)
 */
fun Double.toCurrencyAnnotatedString(
    separator: Char = '.',
    decimalScale: Float = 0.7f
): AnnotatedString {
    // 1. Oldin oddiy string qilib olamiz: "$12 345.88"
    val fullText = CurrencyUtils.format(this, separator)

    // 2. Ajratuvchi belgi (nuqta) qayerda ekanligini topamiz
    val separatorIndex = fullText.lastIndexOf(separator)

    // Agar nuqta topilmasa, oddiy matn qaytaramiz
    if (separatorIndex == -1) return AnnotatedString(fullText)

    // 3. AnnotatedString yig'amiz
    return buildAnnotatedString {
        // Butun qism (Boshidan nuqtagacha): "$12 345"
        append(fullText.substring(0, separatorIndex))

        // Kasr qism (Nuqtadan oxirigacha): ".88"
        // Style qo'llaymiz
        withStyle(
            style = SpanStyle(
                fontSize = decimalScale.em // Asosiy fontning 70% i
                // Xohlasangiz baselineShift ham qo'shish mumkin:
                // baselineShift = BaselineShift.Superscript
            )
        ) {
            append(fullText.substring(separatorIndex))
        }
    }
}