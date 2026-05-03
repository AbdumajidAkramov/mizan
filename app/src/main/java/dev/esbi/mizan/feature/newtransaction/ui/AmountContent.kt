package dev.esbi.mizan.feature.newtransaction.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.newtransaction.color
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.State
import dev.esbi.mizan.ui.kit.text.MizanResizableAmount
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun AmountContent(
    state: State,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onExchangeRateClick: () -> Unit = {}
) {
    val selectedCurrency = state.selectedCurrency
    val mainCurrency = state.currencies.firstOrNull { it.isMainCurrency }
    val manualExchangeRate = state.manualExchangeRate
    val equivalentAmount = state.equivalentInMainCurrency

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Text(text = state.expression, color = MizanTheme.premium.text.primary)
            MizanResizableAmount(
                modifier = Modifier.padding(vertical = MizanTheme.premium.spacing.sm),
                amount = state.currentValue.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                maxFontSize = 40.sp,
                currency = state.selectedCurrency?.code,
                color = state.transactionType.color(),
            )
            manualExchangeRate?.let {
                Text(
                    modifier = Modifier.clickable(
                        enabled = true,
                        onClick = onExchangeRateClick
                    ),
                    text = "Preview: ${formatAmount(equivalentAmount)}",
                    color = MizanTheme.premium.text.tertiary
                )
            }
            manualExchangeRate?.let {
                Text(
                    modifier = Modifier.clickable(
                        enabled = true,
                        onClick = onExchangeRateClick
                    ),
                    text = "Exchange rate: ${formatAmount(manualExchangeRate)}",
                    color = MizanTheme.premium.text.tertiary
                )
            }
        }
    }
}


/**
 * Format amount with thousand separators and 2 decimal places.
 */
private fun formatAmount(amount: BigDecimal): String {
    val rounded = amount.setScale(2, RoundingMode.HALF_UP)
    return String.format(
        java.util.Locale.US,
        "%,.2f", rounded.toDouble()
    )
        .replace(",", " ")
}