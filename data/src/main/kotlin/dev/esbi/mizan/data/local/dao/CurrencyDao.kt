package dev.esbi.mizan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currencies ORDER BY code ASC")
    fun getAll(): Flow<List<CurrencyEntity>>

    @Query("SELECT * FROM currencies WHERE isBaseCurrency = 1 LIMIT 1")
    suspend fun getBaseCurrency(): CurrencyEntity?

    @Query("SELECT * FROM currencies WHERE code = :code LIMIT 1")
    suspend fun getCurrencyByCode(code: String): CurrencyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currency: CurrencyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyEntity>)

    @Query("UPDATE currencies SET rateToBase = :rate WHERE code = :code")
    suspend fun updateRate(code: String, rate: Double)

    @Query("UPDATE currencies SET isBaseCurrency = 0")
    suspend fun clearBaseCurrency()

    @Query("UPDATE currencies SET isBaseCurrency = 1 WHERE code = :code")
    suspend fun setBaseCurrencyInternal(code: String)

    @Transaction
    suspend fun setBaseCurrency(code: String) {
        clearBaseCurrency()
        setBaseCurrencyInternal(code)
    }
}
