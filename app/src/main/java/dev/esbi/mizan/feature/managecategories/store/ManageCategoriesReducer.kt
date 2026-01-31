package dev.esbi.mizan.feature.managecategories.store

import com.arkivanov.mvikotlin.core.store.Reducer

/**
 * Reducer for ManageCategories - updates state based on messages
 */
internal class ManageCategoriesReducer : Reducer<ManageCategoriesStore.State, ManageCategoriesStore.Message> {

    override fun ManageCategoriesStore.State.reduce(
        msg: ManageCategoriesStore.Message
    ): ManageCategoriesStore.State = when (msg) {
        is ManageCategoriesStore.Message.CategoriesLoaded ->
            copy(categories = msg.categories)

        is ManageCategoriesStore.Message.CategoryMoved ->
            copy(categories = msg.categories)

        is ManageCategoriesStore.Message.CategoryDeleted ->
            copy(categories = categories.filter { it.id != msg.id })

        is ManageCategoriesStore.Message.CategorySaved ->
            if (msg.category.id == 0L) {
                // New category added
                copy(categories = categories + msg.category)
            } else {
                // Existing category updated
                copy(
                    categories = categories.map { category ->
                        if (category.id == msg.category.id) msg.category else category
                    }
                )
            }

        is ManageCategoriesStore.Message.CategoryExpanded ->
            copy(expandedCategoryIds = expandedCategoryIds + msg.id)

        is ManageCategoriesStore.Message.CategoryCollapsed ->
            copy(expandedCategoryIds = expandedCategoryIds - msg.id)

        is ManageCategoriesStore.Message.EditSheetShown ->
            copy(
                isEditSheetVisible = true,
                editingCategory = msg.category
            )

        is ManageCategoriesStore.Message.EditSheetHidden ->
            copy(
                isEditSheetVisible = false,
                editingCategory = null
            )

        is ManageCategoriesStore.Message.LoadingChanged ->
            copy(isLoading = msg.isLoading)

        is ManageCategoriesStore.Message.ErrorOccurred ->
            copy(error = msg.error, isLoading = false)
    }
}
