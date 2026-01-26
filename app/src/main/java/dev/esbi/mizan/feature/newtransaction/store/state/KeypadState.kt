package dev.esbi.mizan.feature.newtransaction.store.state

import androidx.compose.ui.text.AnnotatedString
import dev.esbi.mizan.utils.annotatedString

data class KeypadState(
    val operator: String = "",
    val leftNumber: String = "",
    val rightNumber: String = "",
    val currency: String = "UZS"
) {

    val isLeftNumberActive: Boolean = operator.isEmpty()

    val displayText: String
        get() {
            return if (operator.isEmpty()) {
                leftNumber
            } else {
                "$leftNumber $operator $rightNumber"
            }
        }

    val amountText: String
        get() {
            return if (operator.isEmpty()) {
                leftNumber
            } else {
                rightNumber
            }
        }

    val amount: Double
        get() = leftNumber.toDoubleOrNull() ?: 0.0

    val canSubmit: Boolean get() = amount > 0.0

    val annotatedString: AnnotatedString get() = amountText.annotatedString(currency = currency)

}