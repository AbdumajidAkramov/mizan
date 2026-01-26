package dev.esbi.mizan.feature.addtransaction.presentation.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.addtransaction.domain.model.Category
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun DynamicCategoryGrid(
    categories: List<Category>,
    selectedCategory: String?,
    onSelectCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PremiumCategoryPicker(
        modifier = modifier,
        categories = categories,
        selectedCategory = selectedCategory,
        onSelectCategory = onSelectCategory,
        onSelectParentCategory = { /* Handle parent category selection if needed */ }
    )

    /*
        FlowRow(
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            categories.forEach { category ->
                CategoryItem(
                    category = category,
                    isSelected = selectedCategory == category.name,
                    onClick = { onSelectCategory(category.name) }
                )
            }
        }
    */
}

@Composable
private fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val categoryColor = getCategoryColor(category.color)
    /*Box(
        modifier = Modifier
            .width(100.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
            .background(if (isSelected) MizanTheme.premium.colors.surface3 else MizanTheme.premium.colors.surface2)
            .clickable { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Category Icon
            Icon(
                icon = IconValue(getIconName(category.iconName)),
                modifier = Modifier.size(32.dp),
                tint = if (isSelected) Color.White else categoryColor
            )

            Spacer(Modifier.size(8.dp))

            // Category Name
            Text(
                text = category.name,
                style = MizanTheme.typography.bodySm,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp,
                maxLines = 2
            )
        }
*//*
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
                    .background(categoryColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                // CategoryIcon(id) bu yerda chaqiriladi
                Icon(
                    icon = IconValue(Icons.ic_mic),
                    modifier = Modifier.size(24.dp),
                    tint = categoryColor
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = category.name,
                style = MizanTheme.typography.bodySm,
                color = if (isSelected) MizanTheme.premium.text.primary else MizanTheme.premium.text.secondary
            )
        }
*//*
    }*/

    Card(
        modifier = Modifier
            .clickable { onClick() }
            .width(100.dp), // Fixed width for consistent grid look
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) categoryColor else MizanTheme.premium.colors.surface2,
            contentColor = if (isSelected) Color.White else MizanTheme.premium.text.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Category Icon
            MizanIcon(
                icon = IconValue(getIconName(category.iconName)),
                modifier = Modifier.size(32.dp),
                tint = if (isSelected) Color.White else categoryColor
            )

            Spacer(Modifier.size(8.dp))

            // Category Name
            Text(
                text = category.name,
                style = MizanTheme.typography.bodySm,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp,
                maxLines = 2
            )
        }
    }
}

fun getCategoryColor(colorName: String): Color {
    return when (colorName.lowercase()) {
        "red" -> Color(0xFFEF4444)
        "blue" -> Color(0xFF3B82F6)
        "green" -> Color(0xFF10B981)
        "purple" -> Color(0xFF8B5CF6)
        "pink" -> Color(0xFFEC4899)
        "orange" -> Color(0xFFF97316)
        "indigo" -> Color(0xFF6366F1)
        "cyan" -> Color(0xFF06B6D4)
        "teal" -> Color(0xFF14B8A6)
        "emerald" -> Color(0xFF10B981)
        "gray" -> Color(0xFF6B7280)
        else -> Color(0xFF3B82F6) // Default blue color
    }
}

fun getIconName(iconName: String): String {
    return when (iconName.lowercase()) {
        "restaurant" -> "restaurant"
        "car" -> "directions_car"
        "shopping_bag" -> "shopping_bag"
        "movie" -> "movie"
        "receipt" -> "receipt"
        "medical" -> "medical_services"
        "school" -> "school"
        "work" -> "work"
        "laptop" -> "laptop"
        "trending_up" -> "trending_up"
        "business" -> "business"
        "card_giftcard" -> "card_giftcard"
        "wallet" -> "account_balance_wallet"
        "credit_card" -> "credit_card"
        "account_balance" -> "account_balance"
        "savings" -> "savings"
        "more_horiz" -> "more_horiz"
        else -> "category"
    }
}
