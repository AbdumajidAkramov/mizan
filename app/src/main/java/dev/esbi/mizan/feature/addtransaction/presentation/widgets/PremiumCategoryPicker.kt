package dev.esbi.mizan.feature.addtransaction.presentation.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun PremiumCategoryPicker(
    categories: List<Category>,
    selectedCategory: String?,
    onSelectCategory: (String) -> Unit,
    onSelectParentCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        // Grid manually - 3 columns
        val rows = categories.chunked(3)
        rows.forEach { rowCats ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowCats.forEach { category ->
                    PremiumCategoryItem(
                        category = category,
                        isSelected = selectedCategory == category.name,
                        onSelect = {
                            // Check if this category has subcategories by looking at its ID pattern
                            // Main categories have IDs like "food_main", "transport_main"
                            /*if (category.id.endsWith("_main")) {
                                onSelectParentCategory(category.name)
                            } else {
                                onSelectCategory(category.name)
                            }*/
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Fill empty slots if row has less than 3 items
                repeat(3 - rowCats.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun PremiumCategoryItem(
    category: Category,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = getCategoryColor(category.color)
    val scale by animateFloatAsState(if (isSelected) 0.95f else 1f)

    Card(
        modifier = modifier
            .width(100.dp)
            .aspectRatio(1f)
            .scale(scale)
            .clickable { onSelect() },
        shape = RoundedCornerShape(MizanTheme.premium.radius.lg),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MizanTheme.premium.colors.surface3 else MizanTheme.premium.colors.surface2
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        )
    ) {
        Box {
            // Selection Indicator
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(20.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF667eea),
                                    Color(0xFF764ba2)
                                )
                            ),
                            shape = RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(MizanTheme.premium.spacing.md)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon with gradient background
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    categoryColor.copy(alpha = 0.4f),
                                    categoryColor.copy(alpha = 0.2f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    MizanIcon(
                        icon = IconValue(getIconName(category.iconName)),
                        modifier = Modifier.size(24.dp),
                        tint = categoryColor
                    )
                }

                Spacer(Modifier.height(MizanTheme.premium.spacing.sm))

                // Label
                Text(
                    text = category.name,
                    style = MizanTheme.typography.bodySm,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                    color = if (isSelected) MizanTheme.premium.text.primary else MizanTheme.premium.text.secondary,
                    maxLines = 2
                )
            }
        }
    }
}


// 2. Preview Komponenti
@Preview(showBackground = true, backgroundColor = 0xFF111827) // Dark mode foni
@Composable
fun PremiumCategoryPickerPreview() {
    // Tanlangan kategoriyani eslab qolish uchun state
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }

    MizanTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            PremiumCategoryPicker(
                categories = emptyList(),
                selectedCategory = selectedCategoryId,
                onSelectCategory = { newCategory ->
                    selectedCategoryId = newCategory
                },
                onSelectParentCategory = { parentId ->
                    // Parent bosilganda nima bo'lishini simulyatsiya qilish
                    println("Parent category clicked: $parentId")
                }
            )
        }
    }
}