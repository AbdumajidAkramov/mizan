package dev.esbi.mizan.feature.newtransaction.inputtypes

import androidx.compose.ui.text.AnnotatedString
import dev.esbi.mizan.utils.annotatedString
import java.math.BigDecimal

data class KeypadState(
    val operator: String = "",
    val leftNumber: BigDecimal = BigDecimal.ZERO,
    val rightNumber: BigDecimal = BigDecimal.ZERO,
    val currency: String = "UZS"
) {

    val displayText: String
        get() {
            return if (operator.isEmpty()) {
                leftNumber.toPlainString()
            } else {
                "${leftNumber.toPlainString()} $operator ${rightNumber.toPlainString()}"
            }
        }

    val amountText: String
        get() {
            return if (operator.isEmpty()) {
                leftNumber.toPlainString()
            } else {
                rightNumber.toPlainString()
            }
        }

    val amount: Double
        get() = leftNumber.toDouble()

    val canSubmit: Boolean get() = leftNumber > BigDecimal.ZERO

    val annotatedString: AnnotatedString get() = amountText.annotatedString(currency = currency)

}