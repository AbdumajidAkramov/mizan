package dev.esbi.mizan.feature.addtransaction.data.repository

import android.util.Log
import dev.esbi.mizan.data.local.dao.CategoryDao
import dev.esbi.mizan.feature.addtransaction.data.mapper.toDomain
import dev.esbi.mizan.feature.addtransaction.domain.model.Category
import dev.esbi.mizan.feature.addtransaction.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    
    private val tag = "CategoryRepository"
    
    override fun getCategoriesByType(type: String): Flow<List<Category>> {
        Log.d(tag, "Getting categories by type: $type")
        return categoryDao.getCategoriesByType(type).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getAllCategories(): Flow<List<Category>> {
        Log.d(tag, "Getting all categories")
        return categoryDao.getAllCategories().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getCategoryById(id: String): Category? {
        Log.d(tag, "Getting category by id: $id")
        return categoryDao.getCategoryById(id)?.toDomain()
    }
}
