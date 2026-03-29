package dev.esbi.mizan.feature.newtransaction.categorychooser

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun CategoryList(
    state: CategoryChooserState,
    tintColor: Color,
    onParentClick: (Category) -> Unit,
    onSubCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MizanTheme.premium.spacing.lg,
            vertical = MizanTheme.premium.spacing.sm
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.mainCategories, key = { it.id }) { parent ->
            val subCategories = state.categories
                .filter { it.parentId == parent.id }
                .sortedBy { it.orderIndex }

            val isSelected = state.selectedParentId == parent.id
            val isExpanded = isSelected && subCategories.isNotEmpty()

            CategoryCard(
                category = parent,
                isSelected = isSelected,
                isExpanded = isExpanded,
                tintColor = tintColor,
                subCategories = subCategories,
                selectedSubCategoryId = state.selectedCategory?.id,
                onParentClick = { onParentClick(parent) },
                onSubCategoryClick = onSubCategoryClick
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryCard(
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
            androidx.compose.foundation.BorderStroke(2.dp, borderColor)
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

@Composable
private fun SubCategoryChip(
    label: String,
    isSelected: Boolean,
    tintColor: Color,
    onClick: () -> Unit
) {
    val background = if (isSelected) tintColor else MizanTheme.premium.colors.surface3
    val textColor = if (isSelected) Color.White else MizanTheme.premium.text.secondary

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(MizanTheme.premium.radius.full))
            .clickable { onClick() },
        color = background,
        shape = RoundedCornerShape(MizanTheme.premium.radius.full)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MizanTheme.typography.labelMd,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun BottomSection(
    enabled: Boolean,
    tintColor: Color,
    selectedAccountName: String?,
    onAccountClick: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MizanTheme.premium.glass.bg,
                shape = RoundedCornerShape(
                    topStart = MizanTheme.premium.radius.xxl,
                    topEnd = MizanTheme.premium.radius.xxl
                )
            )
            .border(
                width = 1.dp,
                color = MizanTheme.premium.glass.border,
                shape = RoundedCornerShape(
                    topStart = MizanTheme.premium.radius.xxl,
                    topEnd = MizanTheme.premium.radius.xxl
                )
            )
            .padding(MizanTheme.premium.spacing.lg)
    ) {
        // Account Selector Card
        AccountSelectorCard(
            selectedAccountName = selectedAccountName,
            onClick = onAccountClick
        )

        Spacer(Modifier.height(MizanTheme.premium.spacing.md))

        // Continue Button
        ContinueButton(
            enabled = enabled,
            tintColor = tintColor,
            onClick = onContinue
        )
    }
}

@Composable
private fun AccountSelectorCard(
    selectedAccountName: String? = null,
    onClick: () -> Unit
) {
    val hasAccount = selectedAccountName != null

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .clickable { onClick() },
        color = if (hasAccount) MizanTheme.premium.colors.emerald.copy(alpha = 0.1f)
        else MizanTheme.premium.colors.surface2,
        shape = RoundedCornerShape(MizanTheme.premium.radius.xl),
        border = androidx.compose.foundation.BorderStroke(
            width = if (hasAccount) 2.dp else 1.dp,
            color = if (hasAccount) MizanTheme.premium.colors.emerald
            else MizanTheme.premium.glass.border
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Account Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (hasAccount) MizanTheme.premium.colors.emerald.copy(alpha = 0.2f)
                        else MizanTheme.premium.colors.surface3
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = Icons.ic_attach_money),
                    contentDescription = null,
                    tint = if (hasAccount) MizanTheme.premium.colors.emerald
                    else MizanTheme.premium.text.secondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Text
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = selectedAccountName ?: "Select Account",
                    style = MizanTheme.typography.bodyMd,
                    color = if (hasAccount) MizanTheme.premium.colors.emerald
                    else MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (hasAccount) "Tap to change account"
                    else "Tap to choose from your accounts",
                    style = MizanTheme.typography.bodyXs,
                    color = MizanTheme.premium.text.tertiary
                )
            }

            // Chevron
            Icon(
                painter = painterResource(id = Icons.ic_chevron_right),
                contentDescription = null,
                tint = if (hasAccount) MizanTheme.premium.colors.emerald
                else MizanTheme.premium.text.tertiary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ContinueButton(
    enabled: Boolean,
    tintColor: Color,
    onClick: () -> Unit
) {
    val bgColor = if (enabled) tintColor else MizanTheme.premium.colors.surface2
    val textColor = if (enabled) Color.White else MizanTheme.premium.text.muted

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(MizanTheme.premium.radius.full))
            .background(bgColor)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Continue",
            style = MizanTheme.typography.bodyLg,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

private fun getCategoryIcon(categoryName: String): Int {
    val name = categoryName.lowercase()
    return when {
        name.contains("food") || name.contains("dining") -> Icons.ic_home
        name.contains("shopping") -> Icons.ic_home
        name.contains("bill") || name.contains("utilit") -> Icons.ic_home
        name.contains("entertainment") -> Icons.ic_home
        name.contains("health") || name.contains("fitness") -> Icons.ic_home
        name.contains("travel") -> Icons.ic_home
        name.contains("tech") -> Icons.ic_home
        else -> Icons.ic_home
    }
}
