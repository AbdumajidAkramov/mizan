package dev.esbi.mizan.feature.managecategories.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.esbi.mizan.feature.managecategories.ManageCategoriesViewModel
import dev.esbi.mizan.feature.managecategories.ui.components.AddEditCategorySheet

/**
 * Content wrapper for Manage Categories Screen
 * Handles the bottom sheet modal for add/edit functionality
 */
@Composable
fun ManageCategoriesContent(
    viewModel: ManageCategoriesViewModel,
    onBack: () -> Unit,
    onNavigateToEditCategory: (Long?) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Handle navigation labels
    LaunchedEffect(Unit) {
        viewModel.state.collect { storeState ->
            // Handle navigation if needed
        }
    }

    // Main Screen
    ManageCategoriesScreen(
        viewModel = viewModel,
        onBack = onBack,
        onNavigateToEditCategory = onNavigateToEditCategory
    )

    // Add/Edit Bottom Sheet
    AddEditCategorySheet(
        isVisible = state.isEditSheetVisible,
        editingCategory = state.editingCategory,
        onDismiss = { viewModel.onHideEditSheet() },
        onSave = { category ->
            viewModel.onSaveCategory(category)
            viewModel.onHideEditSheet()
        }
    )
}
