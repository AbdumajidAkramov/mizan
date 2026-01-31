package dev.esbi.mizan.feature.managecategories.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.data.local.entity.category.CategoryEntity
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.addtransaction.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Executor for ManageCategories - handles business logic and side effects
 */
internal class ManageCategoriesExecutor(
    private val mainDispatcher: CoroutineDispatcher,
    private val categoryRepository: CategoryRepository
) : CoroutineExecutor<
        ManageCategoriesStore.Intent,
        Unit,
        ManageCategoriesStore.State,
        ManageCategoriesStore.Message,
        ManageCategoriesStore.Label>(
    mainContext = mainDispatcher
) {

    override fun executeAction(action: Unit) {
        loadCategories()
    }

    override fun executeIntent(intent: ManageCategoriesStore.Intent) {
        when (intent) {
            is ManageCategoriesStore.Intent.LoadCategories -> {
                loadCategories()
            }

            is ManageCategoriesStore.Intent.MoveCategory -> {
                moveCategory(intent.fromIndex, intent.toIndex)
            }

            is ManageCategoriesStore.Intent.DeleteCategory -> {
                publish(ManageCategoriesStore.Label.ShowDeleteConfirmation(
                    state().categories.find { it.id == intent.id }
                        ?: return
                ))
            }

            is ManageCategoriesStore.Intent.SaveCategory -> {
                saveCategory(intent.category)
            }

            is ManageCategoriesStore.Intent.ToggleExpand -> {
                toggleExpand(intent.id)
            }

            is ManageCategoriesStore.Intent.ShowAddCategory -> {
                dispatch(ManageCategoriesStore.Message.EditSheetShown(null))
            }

            is ManageCategoriesStore.Intent.ShowEditCategory -> {
                dispatch(ManageCategoriesStore.Message.EditSheetShown(intent.category))
            }

            is ManageCategoriesStore.Intent.HideEditSheet -> {
                dispatch(ManageCategoriesStore.Message.EditSheetHidden)
            }

            is ManageCategoriesStore.Intent.BackClicked -> {
                publish(ManageCategoriesStore.Label.NavigateBack)
            }
        }
    }

    private fun loadCategories() {
        dispatch(ManageCategoriesStore.Message.LoadingChanged(true))

        categoryRepository.observeCategories()
            .onEach { categories ->
                val categoryItems = categories.map { category ->
                    ManageCategoriesStore.CategoryItem(
                        id = category.id,
                        name = category.name,
                        iconName = category.iconName,
                        color = category.color,
                        type = category.type.name,
                        order = category.orderIndex,
                        isSubcategory = category.parentId != null,
                        parentCategoryId = category.parentId
                    )
                }.sortedBy { it.order }

                dispatch(ManageCategoriesStore.Message.CategoriesLoaded(categoryItems))
                dispatch(ManageCategoriesStore.Message.LoadingChanged(false))
            }
            .launchIn(scope)
    }

    private fun moveCategory(fromIndex: Int, toIndex: Int) {
        val currentCategories = state().categories.toMutableList()
        if (fromIndex < currentCategories.size && toIndex < currentCategories.size) {
            val movedCategory = currentCategories.removeAt(fromIndex)
            currentCategories.add(toIndex, movedCategory)

            // Update order values
            val updatedCategories = currentCategories.mapIndexed { index, category ->
                category.copy(order = index)
            }

            // Update in repository
            scope.launch {
                try {
                    // Update order for each category
                    updatedCategories.forEach { category ->
                        val entity = CategoryEntity(
                            id = category.id,
                            name = category.name,
                            iconName = category.iconName ?: "",
                            color = category.color,
                            type = Transaction.Type.valueOf(category.type),
                            orderIndex = category.order,
                            parentId = category.parentCategoryId
                        )
                        categoryRepository.updateCategory(entity)
                    }
                    dispatch(ManageCategoriesStore.Message.CategoryMoved(updatedCategories))
                } catch (e: Exception) {
                    dispatch(ManageCategoriesStore.Message.ErrorOccurred("Failed to reorder categories"))
                }
            }
        }
    }

    private fun saveCategory(category: ManageCategoriesStore.CategoryItem) {
        scope.launch {
            try {
                val entity = CategoryEntity(
                    id = category.id,
                    name = category.name,
                    iconName = category.iconName ?: "",
                    color = category.color,
                    type = Transaction.Type.valueOf(category.type),
                    orderIndex = category.order,
                    parentId = category.parentCategoryId
                )

                if (category.id == 0L) {
                    // Create new category
                    val newCategory = categoryRepository.createCategory(entity)
                    dispatch(ManageCategoriesStore.Message.CategorySaved(
                        category.copy(id = newCategory.id)
                    ))
                } else {
                    // Update existing category
                    categoryRepository.updateCategory(entity)
                    dispatch(ManageCategoriesStore.Message.CategorySaved(category))
                }
            } catch (e: Exception) {
                dispatch(ManageCategoriesStore.Message.ErrorOccurred("Failed to save category"))
            }
        }
    }

    private fun toggleExpand(id: Long) {
        val isExpanded = state().expandedCategoryIds.contains(id)
        if (isExpanded) {
            dispatch(ManageCategoriesStore.Message.CategoryCollapsed(id))
        } else {
            dispatch(ManageCategoriesStore.Message.CategoryExpanded(id))
        }
    }
}
