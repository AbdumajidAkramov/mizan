package dev.esbi.mizan.design.components.currency

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.theme.colors.MizanTheme
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Exchange rate editor with real-time conversion preview.
 * Shows when a sub-currency is selected.
 * 
 * @param selectedCurrency Currently selected sub-currency
 * @param mainCurrency Main currency for conversion
 * @param enteredAmount Amount entered by user
 * @param manualExchangeRate Current exchange rate (editable)
 * @param equivalentAmount Calculated equivalent in main currency
 * @param onRateChanged Callback when rate is manually edited
 * @param modifier Modifier for the composable
 */
@Composable
fun ExchangeRateEditor(
    selectedCurrency: CurrencyModel,
    mainCurrency: CurrencyModel,
    enteredAmount: BigDecimal,
    manualExchangeRate: BigDecimal,
    equivalentAmount: BigDecimal,
    onRateChanged: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSubCurrency = !selectedCurrency.isMainCurrency

    AnimatedVisibility(
        visible = isSubCurrency,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.12f),
                            Color.White.copy(alpha = 0.06f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Exchange Rate Input
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Exchange Rate",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.6f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "1 ${selectedCurrency.code} =",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    BasicTextField(
                        value = manualExchangeRate.toPlainString(),
                        onValueChange = { newValue ->
                            newValue.toBigDecimalOrNull()?.let { rate ->
                                if (rate >= BigDecimal.ZERO) {
                                    onRateChanged(rate)
                                }
                            }
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MizanTheme.premium.colors.emerald
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        cursorBrush = SolidColor(MizanTheme.premium.colors.emerald),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    )

                    Text(
                        text = mainCurrency.code,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // Real-time Conversion Preview
            if (enteredAmount > BigDecimal.ZERO) {
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Equivalent:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.6f)
                    )

                    Text(
                        text = "≈ ${formatAmount(equivalentAmount)} ${mainCurrency.code}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.premium.colors.emerald
                    )
                }
            }
        }
    }
}

/**
 * Format BigDecimal amount with thousand separators.
 */
private fun formatAmount(amount: BigDecimal): String {
    val rounded = amount.setScale(2, RoundingMode.HALF_UP)
    return String.format("%,.2f", rounded.toDouble())
        .replace(",", " ")
}
