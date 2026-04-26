package dev.esbi.mizan.feature.newtransaction.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.presentation.feature.addtransaction.model.CLEAR_KEY
import dev.esbi.mizan.presentation.feature.addtransaction.model.DEL_KEY
import dev.esbi.mizan.presentation.feature.addtransaction.model.DIVIDE
import dev.esbi.mizan.presentation.feature.addtransaction.model.DOT
import dev.esbi.mizan.presentation.feature.addtransaction.model.EQUAL_KEY
import dev.esbi.mizan.presentation.feature.addtransaction.model.Keypad
import dev.esbi.mizan.presentation.feature.addtransaction.model.MINUS
import dev.esbi.mizan.presentation.feature.addtransaction.model.MULTIPLY
import dev.esbi.mizan.presentation.feature.addtransaction.model.PLUS
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@Composable
fun PremiumCalculatorKeypad(
    modifier: Modifier = Modifier,
    onNumberClick: (Keypad) -> Unit,
) {
    val keys = listOf(
        CLEAR_KEY, MULTIPLY, DIVIDE, DEL_KEY,
        "7", "8", "9", MINUS,
        "4", "5", "6", PLUS,
        "1", "2", "3", DOT,
        "00", "0", "000", EQUAL_KEY
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs)
    ) {
        // Grid layout manually using Rows for simplicity or LazyVerticalGrid
        // Simple manual grid for strict 4-column layout like React code
        val rows = keys.chunked(4)

        rows.forEach { rowKeys ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowKeys.forEach { key ->
                    // Determine styling
                    val isOperator = listOf(PLUS, MINUS, MULTIPLY, DIVIDE, EQUAL_KEY).contains(key)
                    val isDelete = key == DEL_KEY || key == CLEAR_KEY
                    val isEquals = key == EQUAL_KEY
                    val isZero = key == "0"

                    val bgColor = when {
                        isEquals -> MizanTheme.premium.colors.emerald.copy(alpha = 0.2f) // Emerald
                        isOperator -> MizanTheme.premium.colors.emerald.copy(alpha = 0.2f)
                        isDelete -> MizanTheme.premium.colors.error.copy(alpha = 0.2f)
                        else -> MizanTheme.premium.colors.surface2
                    }

                    val textColor = when {
                        isEquals -> MizanTheme.premium.colors.emerald
                        isOperator -> MizanTheme.premium.colors.emerald
                        isDelete -> MizanTheme.premium.colors.error
                        else -> MizanTheme.premium.text.primary
                    }

                    Box(
                        modifier = Modifier
                            .weight(if (isZero && rowKeys.size < 4) 2f else 1f) // Span logic mockup
                            .height(48.dp)
                            .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
                            .background(bgColor)
                            .clickable {
                                onNumberClick(Keypad.key(key))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (key == DEL_KEY) {
                            MizanIcon(
                                icon = IconValue(Icons.ic_backspace),
                                modifier = Modifier,
                                tint = textColor
                            )
                        } else {
                            Text(key, style = MizanTheme.typography.headingMd, color = textColor)
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun PremiumCalculatorKeypadPreview() {
    dev.esbi.mizan.ui.theme.MizanTheme() {
        PremiumCalculatorKeypad(
            onNumberClick = {},
        )
    }
}
