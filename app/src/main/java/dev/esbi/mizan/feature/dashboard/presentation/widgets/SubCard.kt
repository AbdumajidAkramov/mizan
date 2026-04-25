package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.kit.balance.BalanceAmount
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.Red
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import java.math.BigDecimal

@Composable
fun SubCard(
    label: String,
    amount: BigDecimal,
    isIncome: Boolean,
    mainCurrency: String,
    modifier: Modifier = Modifier
) {
    val amountDouble = amount.toDouble()
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(0.15f))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isIncome) Color(0xFF047750) else Red), Alignment.Center
            ) {
                MizanIcon(
                    icon = IconValue(if (isIncome) Icons.ic_trend_up else Icons.ic_down_trend),
                    modifier = Modifier.size(14.dp),
                    tint = Color.White
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.8f))
        }
        Spacer(Modifier.height(8.dp))
        BalanceAmount(
            modifier = Modifier,
            balance = amountDouble.toBigDecimal(),
            currency = mainCurrency,
            color = MizanTheme.premium.colors.white,
            typography = MizanTheme.typography.bodyLg
        )
    }
}

@Preview
@Composable
fun SubCardPreview() {
    MizanTheme {
        SubCard(
            label = "Income",
            amount = BigDecimal("12000"),
            isIncome = true,
            mainCurrency = "UZS",
            modifier = Modifier,
        )
    }
}
