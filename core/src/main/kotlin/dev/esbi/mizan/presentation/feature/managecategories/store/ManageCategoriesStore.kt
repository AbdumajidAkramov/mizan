package dev.esbi.mizan.presentation.feature.managecategories.store

import com.arkivanov.mvikotlin.core.store.Store

/**
 * MVI Store for Manage Categories screen
 * Manages state for category CRUD operations and drag-and-drop reordering
 */
interface ManageCategoriesStore :
    Store<ManageCategoriesStore.Intent, ManageCategoriesStore.State, ManageCategoriesStore.Label> {

    /**
     * Category item with hierarchy support
     */
    data class CategoryItem(
        val id: Long,
        val name: String,
        val iconName: String?,
        val color: String,
        val type: String, // "EXPENSE" or "INCOME"
        val order: Int = 0,
        val isSubcategory: Boolean = false,
        val parentCategoryId: Long? = null,
        val subcategories: List<CategoryItem> = emptyList()
    )

    /**
     * State for the Manage Categories screen
     */
    data class State(
        val categories: List<CategoryItem> = emptyList(),
        val expandedCategoryIds: Set<Long> = emptySet(),
        val isEditSheetVisible: Boolean = false,
        val editingCategory: CategoryItem? = null,
        val isLoading: Boolean = true,
        val error: String? = null
    )

    /**
     * User intents
     */
    sealed interface Intent {
        data object LoadCategories : Intent
        data class MoveCategory(val fromIndex: Int, val toIndex: Int) : Intent
        data class DeleteCategory(val id: Long) : Intent
        data class SaveCategory(val category: CategoryItem) : Intent
        data class ToggleExpand(val id: Long) : Intent
        data object ShowAddCategory : Intent
        data class ShowEditCategory(val category: CategoryItem) : Intent
        data object HideEditSheet : Intent
        data object BackClicked : Intent
    }

    /**
     * Internal messages for reducer
     */
    sealed interface Message {
        data class CategoriesLoaded(val categories: List<CategoryItem>) : Message
        data class CategoryMoved(val categories: List<CategoryItem>) : Message
        data class CategoryDeleted(val id: Long) : Message
        data class CategorySaved(val category: CategoryItem) : Message
        data class CategoryExpanded(val id: Long) : Message
        data class CategoryCollapsed(val id: Long) : Message
        data class EditSheetShown(val category: CategoryItem?) : Message
        data object EditSheetHidden : Message
        data class LoadingChanged(val isLoading: Boolean) : Message
        data class ErrorOccurred(val error: String?) : Message
    }

    /**
     * Labels for side effects (navigation, etc.)
     */
    sealed interface Label {
        data object NavigateBack : Label
        data class ShowDeleteConfirmation(val category: CategoryItem) : Label
    }
}
