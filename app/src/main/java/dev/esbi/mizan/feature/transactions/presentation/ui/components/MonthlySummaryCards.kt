package dev.esbi.mizan.feature.transactions.presentation.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.theme.PremiumColors
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MonthlySummaryCards(
    totalIncome: Double,
    totalExpense: Double,
    modifier: Modifier = Modifier
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 2
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Income Card
        PremiumCard(
            variant = PremiumCardVariant.Glass,
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFF06FFA5), CircleShape)
                    )
                    Text(
                        text = stringResource(R.string.transactions_total_income),
                        fontSize = 12.sp,
                        color = PremiumColors.TextTertiary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "+${numberFormat.format(totalIncome)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF06FFA5)
                )
            }
        }

        // Expense Card
        PremiumCard(
            variant = PremiumCardVariant.Glass,
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFFFF6B6B), CircleShape)
                    )
                    Text(
                        text = stringResource(R.string.transactions_total_expense),
                        fontSize = 12.sp,
                        color = PremiumColors.TextTertiary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "-${numberFormat.format(totalExpense)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PremiumColors.TextPrimary
                )
            }
        }
    }
}
