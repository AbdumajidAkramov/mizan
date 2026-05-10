package dev.esbi.mizan.ui.components.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal

/**
 * Example integration of ExchangeRateBottomSheet in a transaction screen.
 * 
 * Usage in your transaction screen:
 * 
 * ```kotlin
 * @Composable
 * fun TransactionScreen(
 *     state: AddNewTransactionStore.State,
 *     accept: (AddNewTransactionStore.Intent) -> Unit
 * ) {
 *     // Your transaction UI...
 *     
 *     // Exchange Rate Display (clickable to open bottom sheet)
 *     if (state.selectedCurrency != null && !state.selectedCurrency.isMainCurrency) {
 *         ExchangeRateDisplay(
 *             currencyCode = state.selectedCurrency.code,
 *             mainCurrencyCode = state.currencies.firstOrNull { it.isMainCurrency }?.code ?: "UZS",
 *             exchangeRate = state.manualExchangeRate ?: state.selectedCurrency.exchangeRate,
 *             onClick = {
 *                 accept(AddNewTransactionStore.Intent.OpenExchangeRateBottomSheet)
 *             }
 *         )
 *     }
 *     
 *     // Exchange Rate Bottom Sheet
 *     ExchangeRateBottomSheet(
 *         isVisible = state.isExchangeRateBottomSheetVisible,
 *         currencyCode = state.selectedCurrency?.code ?: "",
 *         mainCurrencyCode = state.currencies.firstOrNull { it.isMainCurrency }?.code ?: "UZS",
 *         currentRate = state.manualExchangeRate ?: state.selectedCurrency?.exchangeRate ?: BigDecimal.ONE,
 *         transactionAmount = state.amountDecimal,
 *         onRateChanged = { newRate ->
 *             accept(AddNewTransactionStore.Intent.UpdateManualExchangeRate(newRate))
 *         },
 *         onSyncRate = {
 *             accept(AddNewTransactionStore.Intent.SyncExchangeRateFromCBU)
 *         },
 *         onDismiss = {
 *             accept(AddNewTransactionStore.Intent.CloseExchangeRateBottomSheet)
 *         }
 *     )
 * }
 * ```
 */

/**
 * Clickable exchange rate display that opens the bottom sheet.
 */
@Composable
fun ExchangeRateDisplay(
    currencyCode: String,
    mainCurrencyCode: String,
    exchangeRate: BigDecimal,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.12f),
                        Color.White.copy(alpha = 0.06f)
                    )
                )
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Exchange Rate",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.6f)
            )
            Text(
                text = "1 $currencyCode = ${formatRate(exchangeRate)} $mainCurrencyCode",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MizanTheme.premium.colors.emerald
            )
        }
        
        Text(
            text = "Edit",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MizanTheme.premium.colors.emerald.copy(alpha = 0.8f)
        )
    }
}

private fun formatRate(rate: BigDecimal): String {
    return rate.setScale(4, java.math.RoundingMode.HALF_UP).toPlainString()
}
