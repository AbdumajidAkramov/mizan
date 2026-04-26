package dev.esbi.mizan.feature.newtransaction.ui.categoryselector

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun CategoryCard(
    category: Category,
    isSelected: Boolean,
    isExpanded: Boolean,
    tintColor: Color,
    subCategories: List<Category>,
    selectedSubCategoryId: Long?,
    onParentClick: () -> Unit,
    onSubCategoryClick: (Category) -> Unit
) {
    val cardBackground = if (isSelected) {
        tintColor.copy(alpha = 0.1f)
    } else {
        MizanTheme.premium.colors.surface2
    }

    val borderColor = if (isSelected) tintColor else Color.Transparent

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .clickable { onParentClick() }
            .animateContentSize(),
        color = cardBackground,
        shape = RoundedCornerShape(MizanTheme.premium.radius.xl),
        border = if (isSelected) {
            BorderStroke(2.dp, borderColor)
        } else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Parent Category Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Category Icon Container
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) tintColor.copy(alpha = 0.2f)
                            else MizanTheme.premium.colors.surface3
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = getCategoryIcon(category.name)),
                        contentDescription = null,
                        tint = if (isSelected) tintColor else MizanTheme.premium.text.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Category Name
                Text(
                    text = category.name,
                    style = MizanTheme.typography.bodyMd,
                    color = if (isSelected) tintColor else MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // Chevron indicator
                if (subCategories.isNotEmpty()) {
                    Icon(
                        painter = painterResource(
                            id = if (isExpanded) Icons.ic_chevron_left else Icons.ic_chevron_right
                        ),
                        contentDescription = null,
                        tint = MizanTheme.premium.text.tertiary,
                        modifier = Modifier
                            .size(16.dp)
                            .then(
                                if (isExpanded) Modifier else Modifier
                            )
                    )
                }
            }

            // Subcategories (Expandable)
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(Modifier.height(12.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        subCategories.forEach { sub ->
                            SubCategoryChip(
                                label = sub.name,
                                isSelected = selectedSubCategoryId == sub.id,
                                tintColor = tintColor,
                                onClick = { onSubCategoryClick(sub) }
                            )
                        }
                    }
                }
            }
        }
    }
}
