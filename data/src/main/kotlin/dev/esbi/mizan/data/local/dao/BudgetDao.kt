package dev.esbi.mizan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.esbi.mizan.data.local.entity.CategoryBudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    
    @Query("SELECT * FROM category_budgets ORDER BY categoryName ASC")
    fun observeAllBudgets(): Flow<List<CategoryBudgetEntity>>
    
    @Query("SELECT * FROM category_budgets WHERE categoryId = :categoryId")
    suspend fun getBudgetByCategory(categoryId: String): CategoryBudgetEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: CategoryBudgetEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgets(budgets: List<CategoryBudgetEntity>)
    
    @Update
    suspend fun updateBudget(budget: CategoryBudgetEntity)
    
    @Query("UPDATE category_budgets SET budgetAmount = :budgetAmount WHERE categoryId = :categoryId")
    suspend fun updateBudgetAmount(categoryId: String, budgetAmount: Double)
    
    @Query("UPDATE category_budgets SET spentAmount = :spentAmount WHERE categoryId = :categoryId")
    suspend fun updateSpentAmount(categoryId: String, spentAmount: Double)
    
    @Query("DELETE FROM category_budgets WHERE categoryId = :categoryId")
    suspend fun deleteBudget(categoryId: String)
    
    @Query("DELETE FROM category_budgets")
    suspend fun clearAllBudgets()
}
