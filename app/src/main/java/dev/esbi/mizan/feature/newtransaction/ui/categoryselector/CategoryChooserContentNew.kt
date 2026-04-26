package dev.esbi.mizan.feature.newtransaction.ui.categoryselector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.feature.newtransaction2.categorychooser.CategoryChooserState
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

internal fun getCategoryIcon(categoryName: String): Int {
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
