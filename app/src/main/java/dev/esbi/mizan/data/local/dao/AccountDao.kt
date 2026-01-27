package dev.esbi.mizan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.esbi.mizan.data.local.entity.account.AccountEntity
import dev.esbi.mizan.data.local.entity.account.AccountWithCurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    
    @Query("SELECT * FROM accounts ORDER BY name ASC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Transaction
    @Query("SELECT * FROM accounts ORDER BY name ASC")
    fun getAllAccountsWithCurrency(): Flow<List<AccountWithCurrencyEntity>>
    
    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: String): AccountEntity?

    @Transaction
    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountWithCurrencyById(id: String): AccountWithCurrencyEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accounts: List<AccountEntity>)
    
    @Query("DELETE FROM accounts")
    suspend fun clearAllAccounts()
    
    @Query("SELECT COUNT(*) FROM accounts")
    suspend fun getAccountCount(): Int
}
