package dev.esbi.mizan.feature.dashboard.presentation.widgets.premium

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.components.PremiumDonutChart
import dev.esbi.mizan.design.components.PremiumDonutChartComponent
import dev.esbi.mizan.design.components.PremiumDonutChartComponent.Pie
import dev.esbi.mizan.design.kit.glass.CardVariant
import dev.esbi.mizan.design.kit.glass.PremiumCard
import dev.esbi.mizan.design.theme.colors.MizanTheme
import java.text.NumberFormat
import java.util.Locale

// Ma'lumot modeli
data class CategorySpending(
    val categoryLabel: String,
    val totalAmount: Double,
    val colorToken: Color
)

@Composable
fun PremiumTopCategories(
    data: List<CategorySpending>,
    modifier: Modifier = Modifier,
    onSeeAllClick: () -> Unit = {}
) {
    PremiumCard(
        variant = CardVariant.Glass,
        modifier = modifier
    ) {

        Column(modifier = Modifier.padding(24.dp)) {
            // --- HEADER ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Top Categories",
                    style = MizanTheme.typography.headingMd,
                    color = MizanTheme.premium.text.primary
                )

                Text(
                    text = "See all",
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.colors.primary, // Moviy rang
                    modifier = Modifier.clickable(onClick = onSeeAllClick)
                )
            }
            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            // --- CONTENT (Chart + Legend) ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.lg)
            ) {
                // 1. DONUT CHART (Chap taraf)
                // Reactda: w-[140px] h-[140px]
                Box(
                    modifier = Modifier.size(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PremiumDonutChart(
                        component = PremiumDonutChartComponent(
                            data = data.take(4).map { c ->
                                Pie(
                                    amount = c.totalAmount,
                                    color = c.colorToken
                                )
                            },
                        ),
                        modifier = Modifier.size(120.dp),
                    )
                }

                // 2. LEGEND (O'ng taraf)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
                ) {
                    // Faqat birinchi 4 tasini ko'rsatamiz (Reactdagi slice(0, 4))
                    data.take(4).forEach { category ->
                        CategoryLegendItem(category)
                    }
                }
            }
        }
    }
}
/*

@Composable
fun DonutChart(
    data: List<CategorySpending>,
    modifier: Modifier = Modifier,
) {
    val total = data.sumOf { it.totalAmount }
    val colors = data.map { it.colorToken }
    val anim = remember { Animatable(0f) }
    LaunchedEffect(data) { anim.animateTo(1f, tween(1200, easing = FastOutSlowInEasing)) }
    Canvas(modifier) {
        val sw = 24.dp.toPx()
        var start = -90f
        data.forEachIndexed { i, c ->
            val sweep = ((c.totalAmount / total) * 360 * anim.value).toFloat()
            drawArc(
                colors.getOrElse(i) { Purple },
                start,
                sweep - 4,
                false,
                Offset(sw / 2, sw / 2),
                Size(size.width - sw, size.height - sw),
                style = Stroke(sw, cap = StrokeCap.Round)
            )
            start += sweep
        }
    }
}
*/

@Composable
fun CategoryLegendItem(category: CategorySpending) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    currencyFormat.maximumFractionDigits = 0

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label + Dot
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(category.colorToken, CircleShape)
            )
            Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))
            Text(
                text = category.categoryLabel,
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.secondary,
                maxLines = 1,
                overflow = Ellipsis,
            )
        }

        // Amount
        Text(
            text = currencyFormat.format(category.totalAmount),
            style = MizanTheme.typography.bodyMd.copy(
                fontWeight = FontWeight.Medium,
            ),
            maxLines = 1,
            overflow = Ellipsis,
            color = MizanTheme.premium.text.primary,

            )
    }
}

// --- PREVIEW ---
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun PremiumTopCategoriesPreview() {
    val mockCategories = listOf(
        CategorySpending("Food", 850.0, Color(0xFFFF6B9D)),
        CategorySpending("Transport", 420.0, Color(0xFF4FACFE)),
        CategorySpending("Shopping", 340.0, Color(0xFFFFA34D)),
        CategorySpending("Bills", 240.0, Color(0xFF00D2FF)),
        CategorySpending("Others", 120.0, Color.Gray)
    )

    Box(Modifier.padding(16.dp)) {
        PremiumTopCategories(data = mockCategories)
    }
}