package dev.esbi.mizan.presentation.feature.addtransaction.domain.repository

import dev.esbi.mizan.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getMainCategoriesByType(type: String): Flow<List<Category>>
    fun getSubcategories(parentId: String): Flow<List<Category>>
    fun getCategoriesByType(type: String): Flow<List<Category>>
    fun getAllCategories(): Flow<List<Category>>
    fun observeCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: String): Category?
    suspend fun createCategory(category: Category): Category
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(id: Long)
}
