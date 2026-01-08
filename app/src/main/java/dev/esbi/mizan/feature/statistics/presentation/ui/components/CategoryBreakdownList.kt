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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.statistics.domain.model.CategoryBreakdown
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

private fun parseColor(colorString: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(colorString))
    } catch (e: Exception) {
        Color(0xFF667EEA)
    }
}
