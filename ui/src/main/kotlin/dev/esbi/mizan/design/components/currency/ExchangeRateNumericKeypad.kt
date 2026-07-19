package dev.esbi.mizan.design.components.currency

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.kit.icon.IconValue
import dev.esbi.mizan.design.kit.icon.MizanIcon
import dev.esbi.mizan.design.theme.MizanTheme
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.design.utils.IconRes

/**
 * Custom numeric keypad for exchange rate input.
 * Features:
 * - Strict 4x4 grid layout (16 slots total)
 * - Data-driven architecture with KeypadKey sealed class
 * - Clear (C) button for instant reset
 * - Glassmorphic styling
 * - Haptic feedback on each press
 * - BigDecimal-safe input handling
 * 
 * Layout:
 * Row 1: [ 1 ] [ 2 ] [ 3 ] [ C ]
 * Row 2: [ 4 ] [ 5 ] [ 6 ] [ ⌫ ]
 * Row 3: [ 7 ] [ 8 ] [ 9 ] [   ]
 * Row 4: [ . ] [ 0 ] [   ] [   ]
 */

private const val DEL_KEY = "DEL"
private const val CLEAR_KEY = "C"
private const val EQUAL_KEY = "="
private const val DIVIDE = "÷"
private const val MULTIPLY = "×"
private const val MINUS = "-"
private const val PLUS = "+"
private const val DOT = "."
private const val EMPTY_KEY = ""

@Composable
fun ExchangeRateNumericKeypad(
    onNumberClick: (String) -> Unit,
    onDecimalClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keys = listOf(
        "7", "8", "9", DEL_KEY,
        "4", "5", "6", CLEAR_KEY,
        "1", "2", "3", EMPTY_KEY,
        DOT, "0", EMPTY_KEY, EMPTY_KEY
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
                                when (key) {
                                    DEL_KEY -> onBackspaceClick()
                                    CLEAR_KEY -> onClear()
                                    EMPTY_KEY -> Unit
                                    DOT -> onDecimalClick()
                                    else -> onNumberClick(key)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (key == DEL_KEY) {
                            MizanIcon(
                                icon = IconValue(IconRes.ic_backspace),
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

/**
 * Sealed class representing all possible keypad keys in the 4x4 grid.
 */
private sealed class KeypadKey {
    data class Number(val value: String) : KeypadKey()
    object Decimal : KeypadKey()
    object Clear : KeypadKey()
    object Backspace : KeypadKey()
    object Empty : KeypadKey()
}

/**
 * Individual keypad button with glassmorphic styling.
 * 
 * @param isClear If true, applies warning/red tint for Clear button
 * @param isBackspace If true, uses larger font size for backspace icon
 */
@Composable
private fun KeypadButton(
    modifier: Modifier = Modifier,
    text: String = "",
    isClear: Boolean = false,
    isBackspace: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .aspectRatio(1.5f)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = if (isClear) {
                        // Subtle red/warning tint for Clear button
                        listOf(
                            Color(0xFFF5576C).copy(alpha = 0.2f),
                            Color(0xFFF5576C).copy(alpha = 0.12f)
                        )
                    } else {
                        listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color.White.copy(alpha = 0.08f)
                        )
                    }
                )
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = when {
                isBackspace -> 28.sp
                isClear -> 22.sp
                else -> 24.sp
            },
            fontWeight = if (isClear) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isClear) {
                Color(0xFFF5576C).copy(alpha = 0.95f)
            } else {
                Color.White.copy(alpha = 0.9f)
            }
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
private fun PreviewExchangeRateNumericKeypadLight() {
    // Mizan loyihasidagi asosiy tema (AnorPrimaryTheme)
    MizanTheme {
        Box(
            modifier = Modifier
                .background(MizanTheme.premium.background.primary)
                .padding(16.dp)
        ) {
            ExchangeRateNumericKeypad(
                onNumberClick = { /* Preview uchun bo'sh qoldiramiz */ },
                onDecimalClick = { },
                onBackspaceClick = { },
                onClear = { }
            )
        }
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewExchangeRateNumericKeypadDark() {
    MizanTheme {
        Box(
            modifier = Modifier
                .background(MizanTheme.premium.background.primary)
                .padding(16.dp)
        ) {
            ExchangeRateNumericKeypad(
                onNumberClick = { },
                onDecimalClick = { },
                onBackspaceClick = { },
                onClear = { }
            )
        }
    }
}