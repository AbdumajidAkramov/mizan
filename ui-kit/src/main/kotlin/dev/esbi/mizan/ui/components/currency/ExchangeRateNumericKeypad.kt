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
 * - 4x4 grid layout (1-9, C, 0, ., Backspace)
 * - Clear (C) button for instant reset
 * - Glassmorphic styling
 * - Haptic feedback on each press
 * - BigDecimal-safe input handling
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

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Rows 1-3: Numbers 1-9
        for (row in 0..2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (col in 1..3) {
                    val number = (row * 3 + col).toString()
                    KeypadButton(
                        text = number,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNumberClick(number)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Row 4: Clear, 0, Decimal, Backspace
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Clear button
            KeypadButton(
                text = "C",
                isClear = true,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClear()
                },
                modifier = Modifier.weight(1f)
            )

            // Zero
            KeypadButton(
                text = "0",
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onNumberClick("0")
                },
                modifier = Modifier.weight(1f)
            )

            // Decimal point
            KeypadButton(
                text = ".",
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onDecimalClick()
                },
                modifier = Modifier.weight(1f)
            )

            // Backspace
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
    }
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
