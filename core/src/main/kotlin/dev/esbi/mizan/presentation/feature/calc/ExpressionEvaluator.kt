package dev.esbi.mizan.presentation.feature.calc

import java.math.BigDecimal
import java.math.RoundingMode

fun evaluateSimpleExpression(expression: String): String {
    if (expression.isEmpty()) return "0"

    // Operatorni aniqlash (+, -, *, /)
    val operators = listOf("+", "-", "*", "/")
    val operator = operators.find { expression.contains(it) } ?: return expression

    // Ifodani ikkita songa ajratish
    val parts = expression.split(operator)
    if (parts.size < 2 || parts[1].isEmpty()) return parts[0]

    val left = parts[0].toBigDecimalOrNull() ?: BigDecimal.ZERO
    val right = parts[1].toBigDecimalOrNull() ?: BigDecimal.ZERO

    val result = when (operator) {
        "+" -> left.add(right)
        "-" -> left.subtract(right)
        "*" -> left.multiply(right)
        "/" -> {
            if (right != BigDecimal.ZERO) {
                left.divide(right, 2, RoundingMode.HALF_UP)
            } else {
                BigDecimal.ZERO
            }
        }

        else -> left
    }

    return result.stripTrailingZeros().toPlainString()
}
