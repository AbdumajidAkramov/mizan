package dev.esbi.mizan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.esbi.mizan.data.local.entity.TransactionDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {
    
    @Query("SELECT * FROM transaction_details ORDER BY timestamp DESC")
    fun observeAllTransactions(): Flow<List<TransactionDetailEntity>>
    
    @Query("SELECT * FROM transaction_details WHERE type = :type ORDER BY timestamp DESC")
    fun observeTransactionsByType(type: String): Flow<List<TransactionDetailEntity>>
    
    @Query("SELECT * FROM transaction_details WHERE id = :id")
    suspend fun getTransactionById(id: String): TransactionDetailEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionDetailEntity>)
    
    @Query("DELETE FROM transaction_details")
    suspend fun clearAllTransactions()
}
