package dev.esbi.mizan.feature.addtransaction2.presentation.widgets

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun SubcategoryPicker(
    subcategories: List<Category>,
    parentCategoryName: String,
    selectedSubcategory: String?,
    onSelectSubcategory: (String) -> Unit,
    onBackToCategories: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Header with back button and parent category name
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MizanTheme.premium.spacing.lg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackToCategories,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MizanTheme.premium.text.secondary
                )
            }

            Spacer(Modifier.width(MizanTheme.premium.spacing.sm))

            Text(
                text = parentCategoryName,
                style = MizanTheme.typography.bodyLg,
                fontWeight = FontWeight.Medium,
                color = MizanTheme.premium.text.primary
            )
        }

        // Subcategories grid
        Column(
            verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
        ) {
            val rows = subcategories.chunked(3)
            rows.forEach { rowCats ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowCats.forEach { subcategory ->
                        PremiumSubcategoryItem(
                            subcategory = subcategory,
                            isSelected = selectedSubcategory == subcategory.name,
                            onSelect = { onSelectSubcategory(subcategory.name) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Fill empty slots if row has less than 3 items
                    repeat(3 - rowCats.size) {
                        Spacer(Modifier.width(100.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PremiumSubcategoryItem(
    subcategory: Category,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = getCategoryColor(subcategory.color)
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
                        icon = IconValue(getIconName(subcategory.iconName)),
                        modifier = Modifier.size(24.dp),
                        tint = categoryColor
                    )
                }

                Spacer(Modifier.height(MizanTheme.premium.spacing.sm))

                // Label
                Text(
                    text = subcategory.name,
                    style = MizanTheme.typography.bodySm,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                    color = if (isSelected) MizanTheme.premium.text.primary else MizanTheme.premium.text.secondary,
                    maxLines = 2
                )
            }
        }
    }
}
