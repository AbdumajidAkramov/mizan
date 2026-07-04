package dev.esbi.mizan.feature.calc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.kit.text.MizanResizableAmount
import dev.esbi.mizan.design.theme.colors.MizanTheme
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun MizanCalculator(
    modifier: Modifier = Modifier,
    onAmountChange: (BigDecimal) -> Unit
) {
    var expression by remember { mutableStateOf("") } // "90+8"
    var currentValue by remember { mutableStateOf("0") } // "98"
    var isResultShown by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MizanTheme.premium.spacing.lg),
        horizontalAlignment = Alignment.End
    ) {
        // 1. Ekspressiya ko'rinishi (90+8)
        Text(
            text = expression,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MizanTheme.premium.text.tertiary,
                textAlign = TextAlign.End
            ),
            maxLines = 1
        )

        // 2. Joriy son ko'rinishi (Avvalgi darsdagi Resizable text)
        MizanResizableAmount(
            amount = currentValue.toBigDecimalOrNull() ?: BigDecimal.ZERO,
            modifier = Modifier.padding(vertical = MizanTheme.premium.spacing.sm)
        )

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))

        // 3. NumberPad Grid
        val buttons = listOf(
            listOf("C", "÷", "×", "<"),
            listOf("7", "8", "9", "-"),
            listOf("4", "5", "6", "+"),
            listOf("1", "2", "3", "="),
            listOf("0", "00", "000", ".")
        )

        Column(verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)) {
            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
                ) {
                    row.forEach { label ->
                        CalcButton(
                            label = label,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                handleInput(
                                    label,
                                    currentValue,
                                    expression,
                                    isResultShown
                                ) { newVal, newExp, resetFlag ->
                                    currentValue = newVal
                                    expression = newExp
                                    isResultShown = resetFlag
                                    // Har gal son o'zgarganda asosiy summaga jo'natamiz
                                    currentValue.toBigDecimalOrNull()?.let { onAmountChange(it) }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

fun handleInput(
    input: String,
    current: String,
    exp: String,
    isResult: Boolean,
    onUpdate: (String, String, Boolean) -> Unit
) {
    when (input) {
        "C" -> onUpdate("0", "", false)
        "<" -> {
            val next = if (current.length <= 1) "0" else current.dropLast(1)
            onUpdate(next, exp, false)
        }

        "=" -> {
            // Oddiy hisoblash (Murakkab amallar uchun Expression Parser kutubxonasi tavsiya etiladi)
            val result = evaluateSimpleExpression(exp + current)
            onUpdate(result, "", true)
        }

        "+", "-", "×", "÷" -> {
            val operator = input.replace("×", "*").replace("÷", "/")
            onUpdate("0", exp + current + operator, false)
        }

        "00", "000" -> {
            if (current != "0") onUpdate(current + input, exp, false)
        }

        "." -> {
            if (!current.contains(".")) onUpdate(current + ".", exp, false)
        }

        else -> { // Raqamlar 0-9
            val next = if (current == "0" || isResult) input else current + input
            onUpdate(next, exp, false)
        }
    }
}

@Composable
fun CalcButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isOperator = label in listOf("+", "-", "×", "÷", "=", "C", "<")

    Surface(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(MizanTheme.premium.spacing.lg),
        color = if (label == "=") MizanTheme.premium.colors.emerald
        else if (isOperator) MizanTheme.premium.colors.emerald.copy(alpha = 0.1f)
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (label == "=") Color.White else MizanTheme.premium.text.primary
                )
            )
        }
    }
}

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
                // Bo'lishda 2 ta xona aniqligida yaxlitlaymiz
                left.divide(right, 2, RoundingMode.HALF_UP)
            } else {
                BigDecimal.ZERO // Nolga bo'lish holati
            }
        }

        else -> left
    }

    // .00 qismini keraksiz bo'lsa olib tashlash (stripTrailingZeros)
    return result.stripTrailingZeros().toPlainString()
}

@Preview
@Composable
fun MizanCalculatorPreview(){
    dev.esbi.mizan.design.theme.MizanTheme() {
        MizanCalculator(
            modifier = Modifier,
            onAmountChange = {
                println("New Amount: $it")
            }
        )
    }
}