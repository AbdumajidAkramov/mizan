package dev.esbi.mizan.feature.newtransaction.categoryselect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.esbi.mizan.feature.addtransaction.domain.model.Category
import dev.esbi.mizan.feature.newtransaction.categoryselect.store.CategorySelectStore

@Composable
internal fun CategorySelectScreen(
    viewModel: CategorySelectViewModel,
    onCategorySelected: (Category) -> Unit,
    onNavigateBack: () -> Unit,
    onManageCategories: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle(
        initialValue = CategorySelectStore.State()
    )

    // Handle labels (navigation events)
    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is CategorySelectStore.Label.NavigateBack -> onNavigateBack()
                is CategorySelectStore.Label.CategorySelected -> onCategorySelected(label.category)
                is CategorySelectStore.Label.NavigateToManageCategories -> onManageCategories()
                is CategorySelectStore.Label.ShowError -> {
                    // TODO: Show error toast or snackbar
                }
            }
        }
    }

    CategorySelectContent(
        state = state,
        accept = viewModel::onIntent
    )
}
