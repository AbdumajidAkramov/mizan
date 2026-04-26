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
 * - 4x3 grid layout (1-9, 0, ., Backspace)
 * - Glassmorphic styling
 * - Haptic feedback on each press
 * - BigDecimal-safe input handling
 */
@Composable
fun ExchangeRateNumericKeypad(
    onNumberClick: (String) -> Unit,
    onDecimalClick: () -> Unit,
    onBackspaceClick: () -> Unit,
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

        // Row 4: Decimal, 0, Backspace
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Decimal point
            KeypadButton(
                text = ".",
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onDecimalClick()
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

            // Backspace
            KeypadButton(
                icon = true,
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
 */
@Composable
private fun KeypadButton(
    text: String = "",
    icon: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1.5f)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.15f),
                        Color.White.copy(alpha = 0.08f)
                    )
                )
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (icon) "⌫" else text,
            fontSize = if (icon) 28.sp else 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}
