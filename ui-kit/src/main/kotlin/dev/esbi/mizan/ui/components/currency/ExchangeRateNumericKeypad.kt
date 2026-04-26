package dev.esbi.mizan.ui.components.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
@Composable
fun ExchangeRateNumericKeypad(
    onNumberClick: (String) -> Unit,
    onDecimalClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    // Define 4x4 grid data structure
    val keypadLayout = listOf(
        // Row 1: 1, 2, 3, C
        listOf(
            KeypadKey.Number("1"),
            KeypadKey.Number("2"),
            KeypadKey.Number("3"),
            KeypadKey.Clear
        ),
        // Row 2: 4, 5, 6, Backspace
        listOf(
            KeypadKey.Number("4"),
            KeypadKey.Number("5"),
            KeypadKey.Number("6"),
            KeypadKey.Backspace
        ),
        // Row 3: 7, 8, 9, Empty
        listOf(
            KeypadKey.Number("7"),
            KeypadKey.Number("8"),
            KeypadKey.Number("9"),
            KeypadKey.Empty
        ),
        // Row 4: Decimal, 0, Empty, Empty
        listOf(
            KeypadKey.Decimal,
            KeypadKey.Number("0"),
            KeypadKey.Empty,
            KeypadKey.Empty
        )
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keypadLayout.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { key ->
                    when (key) {
                        is KeypadKey.Number -> {
                            KeypadButton(
                                text = key.value,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onNumberClick(key.value)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        is KeypadKey.Decimal -> {
                            KeypadButton(
                                text = ".",
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onDecimalClick()
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        is KeypadKey.Clear -> {
                            KeypadButton(
                                text = "C",
                                isClear = true,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onClear()
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        is KeypadKey.Backspace -> {
                            KeypadButton(
                                text = "⌫",
                                isBackspace = true,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onBackspaceClick()
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        is KeypadKey.Empty -> {
                            // Empty slot - maintains grid alignment
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1.5f)
                            )
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
