package dev.esbi.mizan.feature.addtransaction.data.repository

import android.util.Log
import dev.esbi.mizan.data.local.dao.CategoryDao
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.feature.addtransaction.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    private val tag = "CategoryRepository"

    override fun getMainCategoriesByType(type: String): Flow<List<Category>> {
        Log.d(tag, "Getting main categories by type: $type")
        return categoryDao.getMainCategoriesByType(type)
    }

    override fun getSubcategories(parentId: String): Flow<List<Category>> {
        Log.d(tag, "Getting subcategories for parent: $parentId")
        return categoryDao.getSubcategories(parentId)
    }

    override fun getCategoriesByType(type: String): Flow<List<Category>> {
        Log.d(tag, "Getting all categories by type: $type")
        return categoryDao.getCategoriesByType(type)
    }

    override fun getAllCategories(): Flow<List<Category>> {
        Log.d(tag, "Getting all categories")
        return categoryDao.getAllCategories()
    }

    override suspend fun getCategoryById(id: String): Category? {
        Log.d(tag, "Getting category by id: $id")
        return categoryDao.getCategoryById(id)
    }
}
