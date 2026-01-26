package dev.esbi.mizan.feature.newtransaction.categoryselect.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.newtransaction.categoryselect.store.CategorySelectReducer.Message
import dev.esbi.mizan.feature.newtransaction.categoryselect.store.CategorySelectStore.Intent
import dev.esbi.mizan.feature.newtransaction.categoryselect.store.CategorySelectStore.Label
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class CategorySelectExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    // TODO: Add category repository dependency
    // private val categoryRepository: CategoryRepository
) : CoroutineExecutor<Intent, Unit, CategorySelectStore.State, Message, Label>(
    mainContext = mainDispatcher
) {

    interface Factory {
        fun create(): CategorySelectExecutor
    }

    override fun executeAction(action: Unit) {
        // Initial load - will be triggered by Intent.LoadCategories
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadCategories -> {
                loadCategories(state().transactionType)
            }
            
            is Intent.SelectParentCategory -> {
                selectParentCategory(intent.category)
            }
            
            is Intent.SelectSubCategory -> {
                selectSubCategory(intent.category)
            }
            
            is Intent.NavigateBack -> {
                handleNavigateBack()
            }
            
            is Intent.RetryLoad -> {
                loadCategories(state().transactionType)
            }
            
            is Intent.ManageCategories -> {
                publish(Label.NavigateToManageCategories)
            }
        }
    }
    
    private fun loadCategories(transactionType: TransactionType) {
        scope.launch {
            dispatch(Message.LoadingChanged(true))
            
            try {
                // TODO: Replace with actual repository call
                // val categories = categoryRepository.getCategoriesByType(transactionType)
                val categories = getMockCategories(transactionType)
                
                dispatch(Message.CategoriesLoaded(categories))
            } catch (e: Exception) {
                dispatch(Message.ErrorChanged("Failed to load categories: ${e.message}"))
                publish(Label.ShowError("Failed to load categories"))
            }
        }
    }
    
    private fun selectParentCategory(category: dev.esbi.mizan.feature.addtransaction.domain.model.Category) {
        val state = state()
        val hasSubcategories = state.categories.any { it.parentId == category.id }
        
        if (hasSubcategories) {
            dispatch(Message.ParentCategorySelected(category.id))
        } else {
            // This category doesn't have children, treat as final selection
            dispatch(Message.CategorySelected(category))
            publish(Label.CategorySelected(category))
        }
    }
    
    private fun selectSubCategory(category: dev.esbi.mizan.feature.addtransaction.domain.model.Category) {
        dispatch(Message.CategorySelected(category))
        publish(Label.CategorySelected(category))
    }
    
    private fun handleNavigateBack() {
        val currentState = state()
        
        if (currentState.selectedParentId != null) {
            // Go back to parent categories
            dispatch(Message.NavigateToParent)
        } else {
            // Go back to previous screen
            publish(Label.NavigateBack)
        }
    }
    
    // TODO: Remove this mock data method and replace with repository
    private fun getMockCategories(transactionType: TransactionType): List<dev.esbi.mizan.feature.addtransaction.domain.model.Category> {
        return if (transactionType == TransactionType.Expense) {
            listOf(
                dev.esbi.mizan.feature.addtransaction.domain.model.Category(
                    id = "1",
                    name = "Food & Dining",
                    iconName = "restaurant",
                    type = "EXPENSE",
                    color = "#FF6B9D",
                    parentId = null
                ),
                dev.esbi.mizan.feature.addtransaction.domain.model.Category(
                    id = "2",
                    name = "Transport",
                    iconName = "directions_car",
                    type = "EXPENSE",
                    color = "#4FACFE",
                    parentId = null
                ),
                dev.esbi.mizan.feature.addtransaction.domain.model.Category(
                    id = "3",
                    name = "Shopping",
                    iconName = "shopping_bag",
                    type = "EXPENSE",
                    color = "#FFA34D",
                    parentId = null
                ),
                dev.esbi.mizan.feature.addtransaction.domain.model.Category(
                    id = "4",
                    name = "Entertainment",
                    iconName = "movie",
                    type = "EXPENSE",
                    color = "#C471F5",
                    parentId = null
                ),
                dev.esbi.mizan.feature.addtransaction.domain.model.Category(
                    id = "5",
                    name = "Health",
                    iconName = "favorite",
                    type = "EXPENSE",
                    color = "#FF6B6B",
                    parentId = null
                ),
                // Subcategories for Food & Dining
                dev.esbi.mizan.feature.addtransaction.domain.model.Category(
                    id = "11",
                    name = "Restaurants",
                    iconName = "restaurant",
                    type = "EXPENSE",
                    color = "#FF6B9D",
                    parentId = "1"
                ),
                dev.esbi.mizan.feature.addtransaction.domain.model.Category(
                    id = "12",
                    name = "Fast Food",
                    iconName = "lunch_dining",
                    type = "EXPENSE",
                    color = "#FF6B9D",
                    parentId = "1"
                ),
                dev.esbi.mizan.feature.addtransaction.domain.model.Category(
                    id = "13",
                    name = "Coffee",
                    iconName = "coffee",
                    type = "EXPENSE",
                    color = "#FF6B9D",
                    parentId = "1"
                ),
                // Subcategories for Transport
                dev.esbi.mizan.feature.addtransaction.domain.model.Category(
                    id = "21",
                    name = "Taxi",
                    iconName = "local_taxi",
                    type = "EXPENSE",
                    color = "#4FACFE",
                    parentId = "2"
                ),
                dev.esbi.mizan.feature.addtransaction.domain.model.Category(
                    id = "22",
                    name = "Public Transport",
                    iconName = "directions_bus",
                    type = "EXPENSE",
                    color = "#4FACFE",
                    parentId = "2"
                )
            )
        } else {
            // Income categories
            listOf(
                dev.esbi.mizan.feature.addtransaction.domain.model.Category(
                    id = "101",
                    name = "Salary",
                    iconName = "work",
                    type = "INCOME",
                    color = "#4CAF50",
                    parentId = null
                ),
                dev.esbi.mizan.feature.addtransaction.domain.model.Category(
                    id = "102",
                    name = "Freelance",
                    iconName = "computer",
                    type = "INCOME",
                    color = "#2196F3",
                    parentId = null
                ),
                dev.esbi.mizan.feature.addtransaction.domain.model.Category(
                    id = "103",
                    name = "Investment",
                    iconName = "trending_up",
                    type = "INCOME",
                    color = "#FF9800",
                    parentId = null
                )
            )
        }
    }
}
