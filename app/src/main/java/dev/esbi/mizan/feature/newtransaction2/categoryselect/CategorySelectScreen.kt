package dev.esbi.mizan.feature.newtransaction2.categoryselect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.feature.newtransaction2.categoryselect.store.CategorySelectStore

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

    /*    CategoryChooserContent(
            state = state,
            accept = viewModel::onIntent
        )*/
}
