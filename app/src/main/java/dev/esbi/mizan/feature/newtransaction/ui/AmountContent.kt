package dev.esbi.mizan.feature.newtransaction.ui


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
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.State
import dev.esbi.mizan.ui.kit.text.MizanResizableAmount
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal

@Composable
fun AmountContent(
    modifier: Modifier = Modifier,
    state: State,
    accept: (Intent) -> Unit
) {
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
        }
    }
}
