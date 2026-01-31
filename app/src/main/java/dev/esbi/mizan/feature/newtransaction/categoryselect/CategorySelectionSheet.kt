package dev.esbi.mizan.feature.newtransaction.categoryselect

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.ui.theme.colors.MizanTheme

/**
 * View state for category selection navigation
 */
private sealed class CategoryViewState {
    object ParentList : CategoryViewState()
    data class SubcategoryList(val parent: Category) : CategoryViewState()
}

/**
 * Premium Category Selection Sheet
 * Full-screen overlay with glassmorphism background
 * Handles both parent category and subcategory selection with smooth transitions
 */
@Composable
fun CategorySelectionSheet(
    isVisible: Boolean,
    categories: List<Category>,
    selectedParentId: Long?,
    selectedChildId: Long?,
    onParentSelected: (Category) -> Unit,
    onChildSelected: (Category) -> Unit,
    onNavigateToManageCategories: () -> Unit,
    onBack: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    // View state management
    var viewState by remember { mutableStateOf<CategoryViewState>(CategoryViewState.ParentList) }

    // Get parent categories
    val parentCategories = remember(categories) {
        categories
            .filter { it.parentId == null }
            .sortedBy { it.orderIndex }
    }

    // Check if a category has children
    val hasChildren: (Category) -> Boolean = remember(categories) {
        { category -> categories.any { it.parentId == category.id } }
    }

    // Get subcategories for a parent
    val getSubcategories: (Long) -> List<Category> = remember(categories) {
        { parentId ->
            categories
                .filter { it.parentId == parentId }
                .sortedBy { it.orderIndex }
        }
    }

    // Handle parent category click
    val handleParentClick: (Category) -> Unit = { category ->
        if (hasChildren(category)) {
            // Has subcategories -> show subcategory view
            viewState = CategoryViewState.SubcategoryList(category)
            onParentSelected(category)
        } else {
            // No subcategories -> select and close
            onParentSelected(category)
            onDismiss()
        }
    }

    // Handle subcategory click (null means "No Subcategory")
    val handleSubcategoryClick: (Category?) -> Unit = { subcategory ->
        if (subcategory != null) {
            onChildSelected(subcategory)
        }
        onDismiss()
    }

    // Handle back navigation
    val handleBack: () -> Unit = {
        viewState = CategoryViewState.ParentList
        onBack()
    }

    // Full-screen overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MizanTheme.premium.glass.bg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.Center
    ) {
        // Main Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp)
                .padding(horizontal = MizanTheme.premium.spacing.lg)
                .clip(RoundedCornerShape(24.dp))
                .background(MizanTheme.premium.background.primary)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { /* Prevent click through */ }
                )
                .padding(MizanTheme.premium.spacing.lg)
        ) {
            // Animated content transition
            AnimatedContent(
                targetState = viewState,
                transitionSpec = {
                    if (targetState is CategoryViewState.SubcategoryList) {
                        // Sliding in from right
                        slideInHorizontally(
                            animationSpec = tween(300),
                            initialOffsetX = { it }
                        ) togetherWith slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { -it }
                        )
                    } else {
                        // Sliding in from left (going back)
                        slideInHorizontally(
                            animationSpec = tween(300),
                            initialOffsetX = { -it }
                        ) togetherWith slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { it }
                        )
                    }
                },
                label = "category_view_transition"
            ) { state ->
                when (state) {
                    is CategoryViewState.ParentList -> {
                        PremiumCategorySelector(
                            categories = parentCategories,
                            selectedCategoryId = selectedParentId,
                            onCategoryClick = handleParentClick,
                            hasChildren = hasChildren,
                            onNavigateToManageCategories = onNavigateToManageCategories
                        )
                    }
                    is CategoryViewState.SubcategoryList -> {
                        PremiumSubcategorySelector(
                            parentCategory = state.parent,
                            subcategories = getSubcategories(state.parent.id),
                            selectedSubcategoryId = selectedChildId,
                            onSubcategoryClick = handleSubcategoryClick,
                            onBackClick = handleBack
                        )
                    }
                }
            }
        }
    }
}
