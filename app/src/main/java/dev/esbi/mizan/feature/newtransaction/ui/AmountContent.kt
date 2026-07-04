package dev.esbi.mizan.feature.newtransaction.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.newtransaction.color
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.State
import dev.esbi.mizan.presentation.feature.currencymanagement.CurrencyFormatter
import dev.esbi.mizan.design.kit.text.MizanResizableAmount
import dev.esbi.mizan.design.theme.colors.MizanTheme
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
    val enteredAmount = state.currentValue.toBigDecimalOrNull() ?: BigDecimal.ZERO

    // Show exchange rate UI only when non-main currency is selected
    val showExchangeRateUI = selectedCurrency != null &&
            mainCurrency != null &&
            selectedCurrency.code != mainCurrency.code

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
            // Expression display
            Text(
                text = state.expression,
                color = MizanTheme.premium.text.primary
            )

            // Main amount input
            MizanResizableAmount(
                modifier = Modifier.padding(vertical = MizanTheme.premium.spacing.sm),
                amount = enteredAmount,
                maxFontSize = 40.sp,
                currency = selectedCurrency?.code,
                color = state.transactionType.color(),
            )

            // Exchange Rate & Preview (only for non-main currencies)
            if (showExchangeRateUI) {
                Column(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(8.dp))
                        .clickable(
                            onClick = onExchangeRateClick
                        )
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(4.dp))

                    // Calculate effective exchange rate
                    val effectiveRate = manualExchangeRate ?: selectedCurrency.exchangeRate

                    // Exchange Rate Display (clickable to edit)
                    val exchangeRateText = stringResource(
                        id = R.string.exchange_rate_format,
                        selectedCurrency.code,
                        formatExchangeRate(effectiveRate, mainCurrency.decimalDigits),
                        mainCurrency.code
                    )

                    Text(
                        text = exchangeRateText,
                        color = MizanTheme.premium.text.tertiary,
                        style = MizanTheme.premium.typography.bodyXs
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Preview Amount Calculation
                    val previewAmount = enteredAmount
                        .multiply(effectiveRate)
                        .setScale(mainCurrency.decimalDigits, RoundingMode.HALF_EVEN)

                    val formattedPreview = CurrencyFormatter.format(previewAmount, mainCurrency)
                    val previewText = stringResource(
                        id = R.string.preview_amount_label,
                        formattedPreview
                    )

                    Text(
                        text = previewText,
                        color = MizanTheme.premium.text.secondary,
                        style = MizanTheme.premium.typography.bodySm
                    )
                }

            }
        }
    }
}

/**
 * Format exchange rate with appropriate decimal places.
 * Uses thousand separators for readability.
 */
private fun formatExchangeRate(rate: BigDecimal, decimalDigits: Int): String {
    val rounded = rate.setScale(decimalDigits, RoundingMode.HALF_EVEN)

    return if (decimalDigits == 0) {
        // For whole numbers (e.g., UZS): "12,200"
        String.format(
            java.util.Locale.US,
            "%,.0f",
            rounded.toDouble()
        )
    } else {
        // For decimals (e.g., USD): "0.00008"
        rounded.stripTrailingZeros().toPlainString()
    }
}