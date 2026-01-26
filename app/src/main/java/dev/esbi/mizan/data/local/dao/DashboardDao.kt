package dev.esbi.mizan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.esbi.mizan.data.local.entity.CategorySpendingEntity
import dev.esbi.mizan.data.local.entity.DashboardSummaryEntity
import dev.esbi.mizan.data.local.entity.WeeklySpendingEntity
import dev.esbi.mizan.data.local.entity.transaction.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DashboardDao {

    @Query("SELECT * FROM dashboard_summary WHERE id = 1")
    fun observeDashboardSummary(): Flow<DashboardSummaryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDashboardSummary(summary: DashboardSummaryEntity)

    @Query("SELECT * FROM category_spending ORDER BY totalAmount DESC")
    fun observeTopCategories(): Flow<List<CategorySpendingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategorySpendingEntity>)

    @Query("DELETE FROM category_spending")
    suspend fun clearCategories()

    @Query("SELECT * FROM weekly_spending ORDER BY dayIndex ASC")
    fun observeWeeklySpending(): Flow<List<WeeklySpendingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklySpending(spending: List<WeeklySpendingEntity>)

    @Query("DELETE FROM weekly_spending")
    suspend fun clearWeeklySpending()

    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT :limit")
    fun observeRecentTransactions(limit: Int = 5): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Query("DELETE FROM transactions")
    suspend fun clearTransactions()
}
