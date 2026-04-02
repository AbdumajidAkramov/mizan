package dev.esbi.mizan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.esbi.mizan.data.local.entity.currency.SubCurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubCurrencyDao {

    @Query("SELECT * FROM sub_currencies ORDER BY order_index ASC")
    fun observeAll(): Flow<List<SubCurrencyEntity>>

    @Query("SELECT * FROM sub_currencies WHERE code = :code LIMIT 1")
    suspend fun getByCode(code: String): SubCurrencyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SubCurrencyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<SubCurrencyEntity>)

    @Query("DELETE FROM sub_currencies WHERE code = :code AND is_main_currency = 0")
    suspend fun deleteByCode(code: String)

    @Query("UPDATE sub_currencies SET order_index = :orderIndex WHERE code = :code")
    suspend fun updateOrder(code: String, orderIndex: Int)

    @Query("""
        UPDATE sub_currencies 
        SET exchange_rate = :exchangeRate, 
            unit_position = :unitPosition, 
            decimal_digits = :decimalDigits 
        WHERE code = :code
    """)
    suspend fun updateSettings(
        code: String,
        exchangeRate: String,
        unitPosition: String,
        decimalDigits: Int
    )

    @Query("SELECT * FROM sub_currencies WHERE is_main_currency = 1 LIMIT 1")
    suspend fun getMainCurrency(): SubCurrencyEntity?

    @Query("SELECT COUNT(*) FROM sub_currencies")
    suspend fun getCount(): Int
}
