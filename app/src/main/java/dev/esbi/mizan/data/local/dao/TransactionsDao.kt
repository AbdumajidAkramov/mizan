package dev.esbi.mizan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.esbi.mizan.data.local.entity.TransactionDetailEntity
import dev.esbi.mizan.data.local.entity.transaction.TransactionEntity
import dev.esbi.mizan.data.local.entity.transaction.TransactionWithCurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {

    // TransactionEntity methods (for add transaction feature)
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun observeAllTransactions(): Flow<List<TransactionEntity>>

    @Transaction
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun observeAllTransactionsWithCurrency(): Flow<List<TransactionWithCurrencyEntity>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun observeTransactionsByType(type: String): Flow<List<TransactionEntity>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun observeTransactionsByTypeWithCurrency(type: String): Flow<List<TransactionWithCurrencyEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: String): TransactionEntity?

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionByIdWithCurrency(id: Long): TransactionWithCurrencyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteTransactionById(transactionId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    // TransactionDetailEntity methods (for transactions list feature)
    @Query("SELECT * FROM transaction_details ORDER BY timestamp DESC")
    fun observeAllTransactionDetails(): Flow<List<TransactionDetailEntity>>

    @Query("SELECT * FROM transaction_details WHERE type = :type ORDER BY timestamp DESC")
    fun observeTransactionDetailsByType(type: String): Flow<List<TransactionDetailEntity>>

    @Query("SELECT * FROM transaction_details WHERE id = :id")
    suspend fun getTransactionDetailById(id: String): TransactionDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionDetails(transactions: List<TransactionDetailEntity>)

    @Query("DELETE FROM transactions")
    suspend fun clearAllTransactions()

    @Query("DELETE FROM transaction_details")
    suspend fun clearAllTransactionDetails()
}
