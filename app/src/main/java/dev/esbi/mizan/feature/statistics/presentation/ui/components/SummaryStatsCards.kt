package dev.esbi.mizan.feature.statistics.presentation.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.statistics.domain.model.PeriodComparison
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.theme.PremiumColors
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SummaryStatsCards(
    comparison: PeriodComparison,
    modifier: Modifier = Modifier
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
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
                            .size(36.dp)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF00F2FE),
                                        Color(0xFF4FACFE)
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(android.R.drawable.arrow_up_float),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.statistics_income),
                            fontSize = 11.sp,
                            color = PremiumColors.TextMuted
                        )
                        Text(
                            text = numberFormat.format(comparison.currentIncome),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PremiumColors.TextPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${if (comparison.incomeChangePercentage >= 0) "+" else ""}${String.format("%.1f", comparison.incomeChangePercentage)}%",
                        fontSize = 11.sp,
                        color = if (comparison.incomeChangePercentage >= 0) Color(0xFF00F2A0) else Color(0xFFFF6B6B)
                    )
                    Text(
                        text = stringResource(R.string.statistics_vs_last_period),
                        fontSize = 11.sp,
                        color = PremiumColors.TextMuted
                    )
                }
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
                            .size(36.dp)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFF6B6B),
                                        Color(0xFFF5576C)
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(android.R.drawable.arrow_down_float),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.statistics_expenses),
                            fontSize = 11.sp,
                            color = PremiumColors.TextMuted
                        )
                        Text(
                            text = numberFormat.format(comparison.currentExpense),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PremiumColors.TextPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${if (comparison.expenseChangePercentage >= 0) "+" else ""}${String.format("%.1f", comparison.expenseChangePercentage)}%",
                        fontSize = 11.sp,
                        color = if (comparison.expenseChangePercentage < 0) Color(0xFF00F2A0) else Color(0xFFFF6B6B)
                    )
                    Text(
                        text = stringResource(R.string.statistics_vs_last_period),
                        fontSize = 11.sp,
                        color = PremiumColors.TextMuted
                    )
                }
            }
        }
    }
}
