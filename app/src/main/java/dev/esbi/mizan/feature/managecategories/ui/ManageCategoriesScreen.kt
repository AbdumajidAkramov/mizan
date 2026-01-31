package dev.esbi.mizan.feature.managecategories.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.managecategories.ManageCategoriesViewModel
import dev.esbi.mizan.feature.managecategories.ui.components.DraggableCategoryRow
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

/**
 * Manage Categories Screen
 *
 * Full CRUD operations for categories with drag-and-drop reordering
 */
@Composable
fun ManageCategoriesScreen(
    viewModel: ManageCategoriesViewModel,
    onBack: () -> Unit,
    onNavigateToEditCategory: (Long?) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // Handle labels for navigation
    LaunchedEffect(Unit) {
        viewModel.state.collect { storeState ->
            // Handle navigation labels if needed
        }
    }

    Scaffold(
        topBar = {
            // Top Toolbar
            ManageCategoriesHeader(
                onBack = onBack,
                onAddNew = {
                    onNavigateToEditCategory(null)
                    viewModel.onAddCategory()
                }
            )

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MizanTheme.premium.background.primary)
                .padding(paddingValues)
        ) {

            // Content
            Box(modifier = Modifier.weight(1f)) {
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier.width(200.dp),
                            color = MizanTheme.premium.colors.emerald
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = MizanTheme.premium.spacing.lg,
                            vertical = MizanTheme.premium.spacing.lg
                        ),
                        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
                    ) {
                        // Info Card
                        item {
                            InfoCard()
                        }

                        // Categories List
                        itemsIndexed(
                            items = state.categories,
                            key = { index, category -> category.id }
                        ) { index, category ->
                            DraggableCategoryRow(
                                category = category,
                                index = index,
                                isExpanded = state.expandedCategoryIds.contains(category.id),
                                onEdit = {
                                    onNavigateToEditCategory(it.id)
                                    viewModel.onEditCategory(it)
                                },
                                onDelete = { viewModel.onDeleteCategory(it) },
                                onToggleExpand = { viewModel.onToggleExpand(it) },
                                onMove = { from, to -> viewModel.onMoveCategory(from, to) }
                            )
                        }

                        // Empty State
                        if (state.categories.isEmpty()) {
                            item {
                                EmptyState(onAddNew = {
                                    onNavigateToEditCategory(null)
                                    viewModel.onAddCategory()
                                })
                            }
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageCategoriesHeader(
    onBack: () -> Unit,
    onAddNew: () -> Unit
) {

    TopAppBar(
        title = {
            Text(
                text = "Manage Categories",
                style = MizanTheme.premium.typography.headingSm,
                color = MizanTheme.premium.text.primary
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                MizanIcon(
                    icon = IconValue(Icons.ic_arrow_back),
                    contentDescription = "Back",
                    tint = MizanTheme.premium.text.primary
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.emerald)
                    .clickable { onAddNew() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Add,
                    contentDescription = "Add Transaction",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MizanTheme.premium.background.primary
        )
    )
}

@Composable
private fun InfoCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(MizanTheme.premium.colors.surface2)
            .padding(MizanTheme.premium.spacing.lg)
    ) {
        Text(
            text = "Drag and drop to reorder categories. Click edit to modify or delete to remove. Expand categories to view their subcategories.",
            style = MizanTheme.typography.bodySm,
            color = MizanTheme.premium.text.secondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EmptyState(
    onAddNew: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MizanTheme.premium.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.lg)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MizanTheme.premium.colors.surface2),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = androidx.compose.ui.res.painterResource(R.drawable.ic_add),
                contentDescription = null,
                tint = MizanTheme.premium.text.tertiary,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = "No categories yet",
            style = MizanTheme.typography.bodyMd,
            color = MizanTheme.premium.text.secondary
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                .background(MizanTheme.premium.colors.emerald)
                .clickable { onAddNew() }
                .padding(
                    horizontal = MizanTheme.premium.spacing.xl,
                    vertical = MizanTheme.premium.spacing.md
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Create Your First Category",
                style = MizanTheme.typography.bodyMd,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
