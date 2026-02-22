package dev.esbi.mizan.feature.premiumaddtransaction.part2

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal

@Composable
fun Transaction.Type.color(): Color = when (this) {
    Transaction.Type.EXPENSE -> Color(0xFFF5576C)
    Transaction.Type.INCOME -> Color(0xFF4FACFE)
    Transaction.Type.TRANSFER -> Color(0xFF10B981)
}

@Composable
fun NewTransactionAmountContent(
    amount: BigDecimal,
    currency: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    onCurrencyClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        MizanResizableAmount(
            modifier = Modifier.weight(1f),
            amount = amount,
            color = color
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onCurrencyClick
                )
                .padding(8.dp),
            color = color,
            text = currency,
            style = MizanTheme.typography.headingMd
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun NewTransactionAmountContentPreview() {
    MizanTheme {
        NewTransactionAmountContent(
            amount = BigDecimal("1534.23"),
            currency = "EUR",
            color = Transaction.Type.INCOME.color(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
    }
}
