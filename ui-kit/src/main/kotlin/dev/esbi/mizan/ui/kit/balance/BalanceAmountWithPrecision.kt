package dev.esbi.mizan.ui.kit.balance

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal

/**
 * Balance amount display with long-press precision tooltip.
 * 
 * Features:
 * - Shows abbreviated format by default (if enabled)
 * - Long-press reveals full precision value in glassmorphic tooltip
 * - Haptic feedback on long-press
 * 
 * @param balance The amount to display
 * @param currency Currency code (e.g., "UZS", "USD")
 * @param fullPrecisionText Pre-formatted full precision text (e.g., "123,456.78 UZS")
 * @param abbreviatedText Pre-formatted abbreviated text (e.g., "123.5 K UZS")
 * @param modifier Modifier for the composable
 * @param color Text color
 * @param typography Text style
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BalanceAmountWithPrecision(
    balance: BigDecimal,
    currency: String,
    fullPrecisionText: String,
    abbreviatedText: String,
    modifier: Modifier = Modifier,
    excludeFromTotal: Boolean = false,
    color: Color = MizanTheme.premium.colors.emerald,
    typography: TextStyle = MizanTheme.premium.typography.bodyLg
) {
    val haptic = LocalHapticFeedback.current
    var showTooltip by remember { mutableStateOf(false) }

    val amountColor: Color = when {
        excludeFromTotal -> MizanTheme.premium.text.muted
        balance < BigDecimal.ZERO -> MizanTheme.premium.colors.error
        else -> color
    }

    Box(modifier = modifier) {
        Text(
            text = abbreviatedText,
            style = typography,
            color = amountColor,
            modifier = Modifier
                .combinedClickable(
                    onClick = { /* No-op on regular click */ },
                    onLongClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showTooltip = true
                    }
                )
                .padding(vertical = MizanTheme.premium.spacing.sm)
        )

        // Glassmorphic Tooltip Popup
        if (showTooltip) {
            Popup(
                alignment = Alignment.TopCenter,
                offset = IntOffset(0, -100),
                onDismissRequest = { showTooltip = false },
                properties = PopupProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    focusable = true
                )
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.15f),
                                    Color.White.copy(alpha = 0.08f)
                                )
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = fullPrecisionText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.premium.colors.emerald
                    )
                }
            }
        }
    }
}
