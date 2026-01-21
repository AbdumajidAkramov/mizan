package dev.esbi.mizan.feature.addtransaction.domain.repository

import dev.esbi.mizan.feature.addtransaction.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getMainCategoriesByType(type: String): Flow<List<Category>>
    fun getSubcategories(parentId: String): Flow<List<Category>>
    fun getCategoriesByType(type: String): Flow<List<Category>>
    fun getAllCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: String): Category?
}
