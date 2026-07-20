package dev.esbi.mizan.features.dashboard.presentation.widgets.premium

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.kit.glass.CardVariant
import dev.esbi.mizan.design.kit.glass.PremiumCard
import dev.esbi.mizan.design.kit.icon.IconValue
import dev.esbi.mizan.design.kit.icon.MizanIcon
import dev.esbi.mizan.design.theme.MizanTheme
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.design.utils.IconRes
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.max

// Grafik ma'lumotlari uchun model
data class CashFlowDataPoint(
    val name: String,
    val income: Float,
    val expense: Float
)

@Composable
fun PremiumCashFlowCard(
    income: Double,
    expenses: Double,
    chartData: List<CashFlowDataPoint>,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val netFlow = income - expenses
    val isPositive = netFlow >= 0
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    PremiumCard(
        variant = CardVariant.Glass,
        modifier = modifier,
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // --- HEADER SECTION ---
            Text(
                text = "Monthly Cash Flow",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary,
                modifier = Modifier.padding(bottom = MizanTheme.premium.spacing.sm)
            )

            // Net Flow Indicator (Pill Box)
            val indicatorBg = if (isPositive) MizanTheme.premium.colors.success.copy(alpha = 0.2f)
            else MizanTheme.premium.colors.error.copy(alpha = 0.2f)
            val indicatorColor = if (isPositive) MizanTheme.premium.colors.success
            else MizanTheme.premium.colors.error

            Box(
                modifier = Modifier
                    .background(indicatorBg, RoundedCornerShape(MizanTheme.premium.radius.md))
                    .padding(
                        horizontal = MizanTheme.premium.spacing.md,
                        vertical = MizanTheme.premium.spacing.sm
                    )
            ) {
                Text(
                    text = "${if (isPositive) "+" else ""}${currencyFormat.format(netFlow)}",
                    style = MizanTheme.typography.headingLg,
                    color = indicatorColor
                )
            }

            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

            // --- INCOME & EXPENSE GRID ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
            ) {
                // INCOME COLUMN
                CashFlowItem(
                    label = "Income",
                    amount = income,
                    isIncome = true,
                    modifier = Modifier.weight(1f)
                )

                // EXPENSE COLUMN
                CashFlowItem(
                    label = "Expenses",
                    amount = expenses,
                    isIncome = false,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

            // --- MINI BAR CHART ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                CashFlowBarChart(
                    data = chartData,
                    successColor = MizanTheme.premium.colors.success,
                    errorColor = MizanTheme.premium.colors.error,
                    modifier = Modifier.matchParentSize()
                )
            }
        }
    }
}

// Yordamchi komponent: Income yoki Expense qatorini chizish uchun
@Composable
private fun CashFlowItem(
    label: String,
    amount: Double,
    isIncome: Boolean,
    modifier: Modifier = Modifier
) {
    val color = if (isIncome) MizanTheme.premium.colors.success else MizanTheme.premium.colors.error
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    // Icon tanlash: CallReceived (Pastga) = Income, CallMade (Tepaga) = Expense
    val icon = if (isIncome) IconValue(IconRes.ic_arrow_down)
    else IconValue(IconRes.ic_arrow_up)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Circle
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(color.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            MizanIcon(
                icon = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))

        Column {
            Text(
                text = label,
                style = MizanTheme.typography.bodyXs,
                color = MizanTheme.premium.text.muted
            )
            Text(
                text = currencyFormat.format(amount),
                style = MizanTheme.typography.bodyMd.copy(fontWeight = FontWeight.Medium),
                color = MizanTheme.premium.text.primary
            )
        }
    }
}

// Canvas orqali chizilgan Bar Chart
@Composable
fun CashFlowBarChart(
    data: List<CashFlowDataPoint>,
    successColor: Color,
    errorColor: Color,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Eng baland ustunni topamiz (Scale qilish uchun)
        val maxVal = data.maxOf { max(it.income, it.expense) }
        val safeMax = if (maxVal == 0f) 1f else maxVal

        // Hisob-kitoblar
        val itemCount = data.size
        // Har bir guruh (income + expense) uchun ajratilgan joy
        val groupWidth = width / itemCount
        // Ustunlar orasidagi masofa (Padding)
        val padding = 4.dp.toPx()
        // Bitta ustunning kengligi (Guruh ichida 2 ta ustun bor va ozgina bo'sh joy qoldiramiz)
        val barWidth = (groupWidth - (padding * 2)) / 2.5f

        data.forEachIndexed { index, point ->
            val startX = index * groupWidth + padding

            // 1. INCOME BAR
            val incomeHeight = (point.income / safeMax) * height
            val incomeTop = height - incomeHeight

            drawRoundRect(
                color = successColor.copy(alpha = 0.8f),
                topLeft = Offset(startX, incomeTop),
                size = Size(barWidth, incomeHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()) // Tepasi yumaloq
            )

            // 2. EXPENSE BAR (Income ning yonida)
            val expenseHeight = (point.expense / safeMax) * height
            val expenseTop = height - expenseHeight

            drawRoundRect(
                color = errorColor.copy(alpha = 0.8f),
                topLeft = Offset(startX + barWidth + (padding / 2), expenseTop),
                size = Size(barWidth, expenseHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
        }
    }
}

// --- PREVIEW ---
@Preview(
    name = "Dark mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PremiumCashFlowCardPreviewDark() {
    MizanTheme(darkTheme = true) {
        PremiumCashFlowCard(
            income = 4250.0,
            expenses = 2800.0,
            listOf(
                CashFlowDataPoint("Week 1", 950f, 520f),
                CashFlowDataPoint("Week 2", 1900f, 680f),
                CashFlowDataPoint("Week 3", 2900f, 720f),
                CashFlowDataPoint("Week 4", 1000f, 685f),
            )
        )
    }
}

// --- PREVIEW ---
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun PremiumCashFlowCardPreviewLight() {
    MizanTheme(darkTheme = false) {
        PremiumCashFlowCard(
            income = 4250.0,
            expenses = 2800.0,
            listOf(
                CashFlowDataPoint("Week 1", 950f, 520f),
                CashFlowDataPoint("Week 2", 1900f, 680f),
                CashFlowDataPoint("Week 3", 2900f, 720f),
                CashFlowDataPoint("Week 4", 1000f, 685f),
            )
        )
    }
}