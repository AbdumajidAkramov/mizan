package dev.esbi.mizan.feature.managecategories

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dev.esbi.mizan.feature.managecategories.store.ManageCategoriesStore
import dev.esbi.mizan.feature.managecategories.store.ManageCategoriesStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * ViewModel for Manage Categories screen
 * Wraps the MVI Store for Android lifecycle integration
 */
class ManageCategoriesViewModel @Inject constructor(
    storeFactory: ManageCategoriesStoreFactory
) : ViewModel() {

    private val store: ManageCategoriesStore = storeFactory.create()

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<ManageCategoriesStore.State> = store.stateFlow

    init {
        // Load categories when ViewModel is created
        store.accept(ManageCategoriesStore.Intent.LoadCategories)
    }

    fun onIntent(intent: ManageCategoriesStore.Intent) {
        store.accept(intent)
    }

    fun onBack() {
        store.accept(ManageCategoriesStore.Intent.BackClicked)
    }

    fun onAddCategory() {
        store.accept(ManageCategoriesStore.Intent.ShowAddCategory)
    }

    fun onEditCategory(category: ManageCategoriesStore.CategoryItem) {
        store.accept(ManageCategoriesStore.Intent.ShowEditCategory(category))
    }

    fun onDeleteCategory(id: Long) {
        store.accept(ManageCategoriesStore.Intent.DeleteCategory(id))
    }

    fun onSaveCategory(category: ManageCategoriesStore.CategoryItem) {
        store.accept(ManageCategoriesStore.Intent.SaveCategory(category))
    }

    fun onMoveCategory(fromIndex: Int, toIndex: Int) {
        store.accept(ManageCategoriesStore.Intent.MoveCategory(fromIndex, toIndex))
    }

    fun onToggleExpand(id: Long) {
        store.accept(ManageCategoriesStore.Intent.ToggleExpand(id))
    }

    fun onHideEditSheet() {
        store.accept(ManageCategoriesStore.Intent.HideEditSheet)
    }
}
