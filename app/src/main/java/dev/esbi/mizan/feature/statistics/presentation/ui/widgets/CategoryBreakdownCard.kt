package dev.esbi.mizan.feature.statistics.presentation.ui.widgets

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.statistics.domain.model.CategoryData
import dev.esbi.mizan.ui.components.PremiumDonutChart
import dev.esbi.mizan.ui.components.PremiumDonutChartComponent
import dev.esbi.mizan.ui.components.PremiumDonutChartComponent.Pie
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.kit.glass.PressCard
import dev.esbi.mizan.ui.theme.colors.MizanTheme


@Composable
internal fun CategoryBreakdownCard(
    categories: List<CategoryData>,
    modifier: Modifier = Modifier
) {
    val categoryData: List<CategoryData> = categories.take(4)
    PressCard(modifier = modifier) {
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "Category Breakdown",
                        style = MizanTheme.typography.headingMd,
                        color = MizanTheme.premium.text.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Spending by category",
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        PremiumDonutChart(
                            component = PremiumDonutChartComponent(
                                data = categoryData.map { c ->
                                    Pie(
                                        amount = c.amount.toDouble(),
                                        color = c.color
                                    )
                                },
                            ),
                            modifier = Modifier.size(120.dp),
                        )
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            categoryData.forEach { category ->
                                CategoryItem(category = category)
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
internal fun CategoryItem(
    category: CategoryData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(category.color)
            )
            Text(
                text = category.name,
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.secondary
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$${category.amount.toInt()}",
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.primary
            )
            Text(
                text = "${category.percentage.toInt()}%",
                style = MizanTheme.typography.bodyXs,
                color = MizanTheme.premium.text.muted

            )
        }
    }
}

// ==========================================
// PREVIEW DATA
// ==========================================

private val mockCategoryData = listOf(
    CategoryData(
        name = "Food & Dining",
        amount = 1250f,
        percentage = 45f,
        color = Color(0xFFFF6B9D) // Pink
    ),
    CategoryData(
        name = "Transportation",
        amount = 850f,
        percentage = 30f,
        color = Color(0xFF4FACFE) // Blue
    ),
    CategoryData(
        name = "Entertainment",
        amount = 450f,
        percentage = 15f,
        color = Color(0xFFFFA34D) // Orange
    ),
    CategoryData(
        name = "Others",
        amount = 280f,
        percentage = 10f,
        color = Color(0xFF00D2FF) // Cyan
    )
)

// ==========================================
// PREVIEW COMPONENT
// ==========================================

@Preview(
    name = "Category Breakdown - Dark",
    showBackground = true,
    backgroundColor = 0xFF111827 // To'q fon
)
@Composable
private fun CategoryBreakdownCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        CategoryBreakdownCard(
            categories = mockCategoryData
        )
    }
}
