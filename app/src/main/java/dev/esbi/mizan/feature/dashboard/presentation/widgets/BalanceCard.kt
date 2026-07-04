package dev.esbi.mizan.feature.dashboard.presentation.widgets


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.kit.balance.BalanceAmount
import dev.esbi.mizan.design.kit.card.GradientColorCard
import dev.esbi.mizan.design.kit.glass.PressCard
import dev.esbi.mizan.design.kit.icon.IconValue
import dev.esbi.mizan.design.kit.icon.MizanIcon
import dev.esbi.mizan.design.theme.MizanTheme
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.design.utils.IconRes
import java.math.BigDecimal

@Composable
fun BalanceCard(
    total: BigDecimal,
    income: BigDecimal,
    expenses: BigDecimal,
    mainCurrency: String,
    modifier: Modifier = Modifier
) {
    var show by remember { mutableStateOf(true) }
    PressCard {
        GradientColorCard(
            modifier = modifier
        ) {
            Column(Modifier.padding(24.dp)) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(0.2f)), Alignment.Center
                        ) {
                            MizanIcon(
                                icon = IconValue(IconRes.ic_wallet),
                                modifier = Modifier.size(20.dp),
                                tint = Color.White
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "Total Balance",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(0.9f)
                        )
                    }
                    IconButton({ show = !show }) {
                        MizanIcon(

                            icon = IconValue(if (show) IconRes.ic_visibility else IconRes.ic_visibility_off),
                            modifier = Modifier.size(20.dp),
                            tint = Color.White.copy(0.8f)
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))

                if (show) {
                    BalanceAmount(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        balance = total,
                        currency = "UZS",
                        color = MizanTheme.premium.colors.white,
                        typography = MizanTheme.typography.displayMd
                    )
                } else {
                    Text(
                        text = "••••••",
                        style = MizanTheme.typography.displayMd,
                    )
                }
                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
                    SubCard(
                        label = "Income",
                        modifier = Modifier.weight(1f),
                        amount = income,
                        isIncome = true,
                        mainCurrency = mainCurrency
                    )
                    SubCard(
                        label = "Expenses",
                        amount = expenses,
                        isIncome = false,
                        mainCurrency = mainCurrency,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun BalanceCardPreview() {
    MizanTheme {
        BalanceCard(
            total = BigDecimal("1234567890.34"),
            income = BigDecimal("1234567890.23"),
            expenses = BigDecimal("1234567890.2"),
            mainCurrency = "UZS"
        )
    }
}
