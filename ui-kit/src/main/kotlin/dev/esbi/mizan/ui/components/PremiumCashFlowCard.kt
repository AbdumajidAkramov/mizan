package dev.esbi.mizan.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.utils.Strings
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PremiumCashFlowCard(
    income: Double,
    expenses: Double,
    modifier: Modifier = Modifier
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }

    val netCashFlow = income - expenses
    val isPositive = netCashFlow >= 0

    PremiumCard(
        variant = PremiumCardVariant.Glass,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(Strings.financial_mirror_cash_flow),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                val barWidth = size.width / 3
                val maxValue = maxOf(income, expenses)
                val incomeHeight =
                    if (maxValue > 0) (income / maxValue * size.height * 0.8f).toFloat() else 0f
                val expensesHeight =
                    if (maxValue > 0) (expenses / maxValue * size.height * 0.8f).toFloat() else 0f

                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF00F2FE),
                            Color(0xFF4FACFE)
                        )
                    ),
                    topLeft = Offset(barWidth * 0.3f, size.height - incomeHeight),
                    size = Size(barWidth * 0.4f, incomeHeight),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )

                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFF6B6B),
                            Color(0xFFFEE140)
                        )
                    ),
                    topLeft = Offset(barWidth * 1.3f, size.height - expensesHeight),
                    size = Size(barWidth * 0.4f, expensesHeight),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Canvas(modifier = Modifier
                            .width(12.dp)
                            .height(12.dp)) {
                            drawCircle(color = Color(0xFF00F2FE))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(Strings.dashboard_income),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = numberFormat.format(income),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Canvas(modifier = Modifier
                            .width(12.dp)
                            .height(12.dp)) {
                            drawCircle(color = Color(0xFFFF6B6B))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(Strings.dashboard_expenses),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = numberFormat.format(expenses),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Strings.financial_mirror_net_cash_flow),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${if (isPositive) "+" else ""}${numberFormat.format(netCashFlow)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPositive) Color(0xFF00F2FE) else Color(0xFFFF6B6B)
                )
            }
        }
    }
}
