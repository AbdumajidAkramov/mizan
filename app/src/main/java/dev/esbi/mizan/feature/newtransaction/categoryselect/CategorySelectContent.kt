package dev.esbi.mizan.feature.newtransaction.categoryselect

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.addtransaction.domain.model.Category
import dev.esbi.mizan.feature.newtransaction.categoryselect.store.CategorySelectStore
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectContent(
    state: CategorySelectStore.State,
    accept: (CategorySelectStore.Intent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when {
                            state.selectedParentId != null -> {
                                state.parentCategory?.name
                                    ?: "Subcategories"
                            }

                            else -> "Select Category"
                        },
                        style = MizanTheme.premium.typography.headingSm,
                        color = MizanTheme.premium.text.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { accept(CategorySelectStore.Intent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MizanTheme.premium.text.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { accept(CategorySelectStore.Intent.ManageCategories) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Manage Categories",
                            tint = MizanTheme.premium.text.secondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MizanTheme.premium.colors.surface1
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MizanTheme.premium.background.primary)
        ) {
            when {
                state.isLoading -> {
                    LoadingView()
                }

                state.error != null -> {
                    ErrorView(
                        error = state.error,
                        onRetry = {
                            accept(CategorySelectStore.Intent.RetryLoad)
                        }
                    )
                }

                else -> {
                    CategoryGrid(
                        state = state,
                        onCategoryClick = { category ->
                            if (state.selectedParentId != null) {
                                accept(
                                    CategorySelectStore.Intent.SelectSubCategory(
                                        category
                                    )
                                )
                            } else {
                                accept(
                                    CategorySelectStore.Intent.SelectParentCategory(
                                        category
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }

}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MizanTheme.premium.colors.primary,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Loading categories...",
                style = MizanTheme.premium.typography.bodyMd,
                color = MizanTheme.premium.text.secondary
            )
        }
    }
}

@Composable
private fun ErrorView(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Oops!",
                style = MizanTheme.premium.typography.headingLg,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = error,
                style = MizanTheme.premium.typography.bodyMd,
                color = MizanTheme.premium.text.secondary,
                textAlign = TextAlign.Center
            )

            androidx.compose.material3.Button(
                onClick = onRetry,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MizanTheme.premium.colors.primary
                )
            ) {
                Text(
                    text = "Retry",
                    style = MizanTheme.premium.typography.labelMd,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun CategoryGrid(
    state: CategorySelectStore.State,
    onCategoryClick: (Category) -> Unit
) {
    val currentCategories = state.currentCategories

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MizanTheme.premium.spacing.md),
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        items(currentCategories) { category ->
            val hasSubcategories = state.categories.any { it.parentId == category.id }
            CategoryItem(
                category = category,
                hasSubcategories = hasSubcategories,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    hasSubcategories: Boolean = false,
    onClick: () -> Unit
) {
    val categoryColor = remember(category.color) {
        Color(android.graphics.Color.parseColor(category.color))
    }
    val scale by animateFloatAsState(targetValue = 1f, label = "scale")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .scale(scale)
            .clickable { onClick() },
        shape = RoundedCornerShape(MizanTheme.premium.radius.lg),
        colors = CardDefaults.cardColors(
            containerColor = MizanTheme.premium.colors.surface2
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(MizanTheme.premium.spacing.md)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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
                    Icon(
                        icon = IconValue(getIconName(category.iconName)),
                        modifier = Modifier.size(24.dp),
                        tint = categoryColor
                    )
                }

                Spacer(Modifier.height(MizanTheme.premium.spacing.sm))

                // Label
                Text(
                    text = category.name,
                    style = MizanTheme.premium.typography.bodySm,
                    fontWeight = FontWeight.Medium,
                    color = MizanTheme.premium.text.primary,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )

                // Show arrow indicator if has children
                if (hasSubcategories) {
                    Spacer(Modifier.height(MizanTheme.premium.spacing.xs))
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Has subcategories",
                        tint = MizanTheme.premium.text.tertiary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// Helper function to map icon names to actual icon resources
private fun getIconName(iconName: String): String {
    // This is a simplified mapping - you might want to use a proper icon mapper
    return when (iconName) {
        "restaurant" -> "restaurant"
        "directions_car" -> "directions_car"
        "shopping_bag" -> "shopping_bag"
        "movie" -> "movie"
        "favorite" -> "favorite"
        "lunch_dining" -> "lunch_dining"
        "coffee" -> "coffee"
        "local_taxi" -> "local_taxi"
        "directions_bus" -> "directions_bus"
        "work" -> "work"
        "computer" -> "computer"
        "trending_up" -> "trending_up"
        else -> "category" // fallback icon
    }
}

