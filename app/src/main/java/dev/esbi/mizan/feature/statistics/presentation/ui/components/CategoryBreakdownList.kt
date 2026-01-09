package dev.esbi.mizan.feature.statistics.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import dev.esbi.mizan.feature.statistics.domain.model.CategoryBreakdown
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.PremiumColors
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CategoryBreakdownList(
    categories: List<CategoryBreakdown>,
    modifier: Modifier = Modifier
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.forEach { category ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = parseColor(category.color),
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = category.categoryName,
                        fontSize = 14.sp,
                        color = PremiumColors.TextSecondary
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = numberFormat.format(category.amount),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = PremiumColors.TextPrimary
                    )
                    Text(
                        text = "${String.format("%.0f", category.percentage)}%",
                        fontSize = 11.sp,
                        color = PremiumColors.TextMuted
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoryBreakdownListPreview() {
    val mockCategoryBreakdownList = listOf(
        CategoryBreakdown(
            categoryId = "food",
            categoryName = "Food & Groceries",
            amount = 1250.0,
            percentage = 22.5f,
            color = "#FF7043"
        ),
        CategoryBreakdown(
            categoryId = "rent",
            categoryName = "Rent / Housing",
            amount = 2200.0,
            percentage = 39.6f,
            color = "#42A5F5"
        ),
        CategoryBreakdown(
            categoryId = "transport",
            categoryName = "Transportation",
            amount = 480.0,
            percentage = 8.6f,
            color = "#66BB6A"
        ),
        CategoryBreakdown(
            categoryId = "utilities",
            categoryName = "Utilities",
            amount = 320.0,
            percentage = 5.8f,
            color = "#AB47BC"
        ),
        CategoryBreakdown(
            categoryId = "entertainment",
            categoryName = "Entertainment",
            amount = 410.0,
            percentage = 7.4f,
            color = "#FFA726"
        ),
        CategoryBreakdown(
            categoryId = "health",
            categoryName = "Health",
            amount = 290.0,
            percentage = 5.2f,
            color = "#26C6DA"
        ),
        CategoryBreakdown(
            categoryId = "education",
            categoryName = "Education",
            amount = 360.0,
            percentage = 6.5f,
            color = "#8D6E63"
        ),
        CategoryBreakdown(
            categoryId = "shopping",
            categoryName = "Shopping",
            amount = 520.0,
            percentage = 9.4f,
            color = "#EC407A"
        ),
        CategoryBreakdown(
            categoryId = "subscriptions",
            categoryName = "Subscriptions",
            amount = 180.0,
            percentage = 3.2f,
            color = "#78909C"
        ),
        CategoryBreakdown(
            categoryId = "other",
            categoryName = "Other",
            amount = 170.0,
            percentage = 3.1f,
            color = "#BDBDBD"
        )
    )
    MizanTheme {
        CategoryBreakdownList(
            categories = mockCategoryBreakdownList,
            modifier = Modifier
        )
    }
}

private fun parseColor(colorString: String): Color {
    return try {
        Color(colorString.toColorInt())
    } catch (e: Exception) {
        Color(0xFF667EEA)
    }
}
