package dev.esbi.mizan.design.components.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.theme.MizanTheme
import dev.esbi.mizan.design.theme.colors.MizanTheme
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Exchange Rate BottomSheet for manual rate adjustment.
 * 
 * Features:
 * - Custom numeric keypad for rate input
 * - Real-time conversion preview
 * - CBU API sync button
 * - Glassmorphic design
 * - BigDecimal precision (scale 12)
 * 
 * @param isVisible Whether the bottom sheet is visible
 * @param currencyCode The currency code being edited (e.g., "USD")
 * @param mainCurrencyCode The main currency code (e.g., "UZS")
 * @param currentRate Current exchange rate
 * @param transactionAmount Current transaction amount for preview
 * @param onRateChanged Callback when rate is updated
 * @param onSyncRate Callback to sync rate from CBU API
 * @param onDismiss Callback when bottom sheet is dismissed
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRateBottomSheet(
    isVisible: Boolean,
    currencyCode: String,
    mainCurrencyCode: String,
    currentRate: BigDecimal,
    transactionAmount: BigDecimal,
    onRateChanged: (BigDecimal) -> Unit,
    onSyncRate: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var rateInput by remember(currentRate) {
        mutableStateOf(currentRate.toPlainString())
    }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color.Transparent,
            modifier = modifier
        ) {
            ExchangeRateContent(
                modifier = Modifier.padding(16.dp),
                currencyCode = currencyCode,
                mainCurrencyCode = mainCurrencyCode,
                rateInput = rateInput,
                transactionAmount = transactionAmount,
                onRateInputChange = { newInput ->
                    rateInput = newInput
                    // Parse and notify parent
                    try {
                        val rate = BigDecimal(newInput)
                        if (rate > BigDecimal.ZERO) {
                            onRateChanged(rate)
                        }
                    } catch (e: Exception) {
                        // Invalid input, ignore
                    }
                },
                onSyncRate = {
                    onSyncRate()
                },
                onApply = {
                    try {
                        val rate = BigDecimal(rateInput)
                        if (rate > BigDecimal.ZERO) {
                            onRateChanged(rate)
                            onDismiss()
                        }
                    } catch (e: Exception) {
                        // Invalid input, don't dismiss
                    }
                }
            )
        }
    }
}

@Composable
private fun ExchangeRateContent(
    currencyCode: String,
    mainCurrencyCode: String,
    rateInput: String,
    transactionAmount: BigDecimal,
    onRateInputChange: (String) -> Unit,
    onSyncRate: () -> Unit,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MizanTheme.premium.background.primary.copy(alpha = 0.95f),
                        MizanTheme.premium.background.secondary.copy(alpha = 0.98f)
                    )
                )
            )
            .then(modifier)
    ) {
        // Header
        Text(
            text = "Exchange Rate",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MizanTheme.premium.text.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Conversion formula
        Text(
            text = "1 $currencyCode = ? $mainCurrencyCode",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MizanTheme.premium.text.tertiary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Rate Input Section
        RateInputSection(
            currencyCode = currencyCode,
            mainCurrencyCode = mainCurrencyCode,
            rateInput = rateInput,
            onSyncRate = onSyncRate
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Real-time Conversion Preview
        if (transactionAmount > BigDecimal.ZERO) {
            ConversionPreview(
                transactionAmount = transactionAmount,
                currencyCode = currencyCode,
                mainCurrencyCode = mainCurrencyCode,
                rateInput = rateInput
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Custom Numeric Keypad
        ExchangeRateNumericKeypad(
            onNumberClick = { digit ->
                val newInput = rateInput + digit
                // Allow up to 8 decimal places
                if (isValidRateInput(newInput)) {
                    onRateInputChange(newInput)
                }
            },
            onDecimalClick = {
                if (!rateInput.contains(".")) {
                    onRateInputChange("$rateInput.")
                }
            },
            onBackspaceClick = {
                if (rateInput.isNotEmpty()) {
                    onRateInputChange(rateInput.dropLast(1))
                }
            },
            onClear = {
                // Clear all input - reset to empty string
                onRateInputChange("")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Apply Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MizanTheme.premium.colors.emerald,
                            MizanTheme.premium.colors.emerald.copy(alpha = 0.8f)
                        )
                    )
                )
                .clickable(onClick = onApply),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Apply",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun RateInputSection(
    currencyCode: String,
    mainCurrencyCode: String,
    rateInput: String,
    onSyncRate: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MizanTheme.premium.colors.surface2.copy(alpha = 0.12f),
                        MizanTheme.premium.colors.surface2.copy(alpha = 0.06f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = MizanTheme.premium.colors.surface2.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: "1 USD ="
        Text(
            text = "1 $currencyCode =",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MizanTheme.premium.text.primary
                .copy(alpha = 0.9f)
        )

        // Center: Rate Input Display
        Text(
            text = rateInput.ifEmpty { "0" },
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MizanTheme.premium.colors.emerald,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        // Right: Currency + Sync Button
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mainCurrencyCode,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MizanTheme.premium.text.primary
                    .copy(alpha = 0.8f)
            )

            // Sync Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.2f))
                    .clickable(onClick = onSyncRate),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Sync from CBU",
                    tint = MizanTheme.premium.colors.emerald,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ConversionPreview(
    transactionAmount: BigDecimal,
    currencyCode: String,
    mainCurrencyCode: String,
    rateInput: String
) {
    val equivalentAmount = try {
        val rate = BigDecimal(rateInput.ifEmpty { "0" })
        transactionAmount.multiply(rate).setScale(2, RoundingMode.HALF_UP)
    } catch (e: Exception) {
        BigDecimal.ZERO
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MizanTheme.premium.colors.surface2.copy(alpha = 0.08f))
            .padding(16.dp)
    ) {
        Text(
            text = "Conversion Preview",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MizanTheme.premium.text.secondary.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${formatAmount(transactionAmount)} $currencyCode",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MizanTheme.premium.text.primary
                    .copy(alpha = 0.9f)

            )

            Text(
                text = "≈",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.5f)
            )

            Text(
                text = "${formatAmount(equivalentAmount)} $mainCurrencyCode",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MizanTheme.premium.colors.emerald
            )
        }
    }
}

private fun formatAmount(amount: BigDecimal): String {
    val rounded = amount.setScale(2, RoundingMode.HALF_UP)
    return String.format("%,.2f", rounded.toDouble()).replace(",", " ")
}

/**
 * Validates exchange rate input to allow up to 8 decimal places.
 * Examples:
 * - "12210.12345678" ✓
 * - "12210.123456789" ✗ (9 decimals)
 * - "12210" ✓
 */
private fun isValidRateInput(input: String): Boolean {
    if (input.isEmpty()) return true

    // Check if it's a valid number format
    val regex = Regex("^\\d*\\.?\\d{0,8}$")
    return regex.matches(input)
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
private fun PreviewExchangeRateContentLight() {
    MizanTheme {
        // Using the background color from your semantic implementation
        Box(
            modifier = Modifier
                .background(MizanTheme.premium.background.primary)
                .padding(16.dp)
        ) {
            ExchangeRateContent(
                currencyCode = "USD",
                mainCurrencyCode = "UZS",
                rateInput = "12850.00",
                transactionAmount = BigDecimal("100.00"),
                onRateInputChange = {},
                onSyncRate = {},
                onApply = {}
            )
        }
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewExchangeRateContentDark() {
    MizanTheme {
        Box(
            modifier = Modifier
                .background(MizanTheme.premium.background.primary)
                .padding(16.dp)
        ) {
            ExchangeRateContent(
                currencyCode = "EUR",
                mainCurrencyCode = "UZS",
                rateInput = "14200.50",
                transactionAmount = BigDecimal("50.00"),
                onRateInputChange = {},
                onSyncRate = {},
                onApply = {}
            )
        }
    }
}