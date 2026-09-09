package dev.esbi.mizan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity
import kotlinx.coroutines.flow.Flow

import java.math.BigDecimal

@Dao
interface CurrencyDao {

    // ========== All Currencies ==========
    
    @Query("SELECT * FROM currencies ORDER BY order_index ASC, code ASC")
    fun observeAll(): Flow<List<CurrencyEntity>>
    
    @Query("SELECT * FROM currencies ORDER BY order_index ASC, code ASC")
    suspend fun getAll(): List<CurrencyEntity>

    @Query("SELECT * FROM currencies WHERE code = :code LIMIT 1")
    suspend fun getCurrencyByCode(code: String): CurrencyEntity?

    // ========== Main Currency (Single Primary) ==========
    
    @Query("SELECT * FROM currencies WHERE is_main_currency = 1 LIMIT 1")
    suspend fun getMainCurrency(): CurrencyEntity?

    @Query("SELECT * FROM currencies WHERE is_main_currency = 1 LIMIT 1")
    fun observeMainCurrency(): Flow<CurrencyEntity?>
    
    // Legacy support
    @Query("SELECT * FROM currencies WHERE isBaseCurrency = 1 LIMIT 1")
    suspend fun getBaseCurrency(): CurrencyEntity?

    @Query("SELECT * FROM currencies WHERE isBaseCurrency = 1 LIMIT 1")
    fun observeBaseCurrency(): Flow<CurrencyEntity?>

    // ========== Secondary Currencies (Quick Access) ==========
    
    @Query("SELECT * FROM currencies WHERE is_secondary = 1 ORDER BY order_index ASC")
    fun observeSecondaryCurrencies(): Flow<List<CurrencyEntity>>
    
    @Query("SELECT * FROM currencies WHERE is_secondary = 1 ORDER BY order_index ASC")
    suspend fun getSecondaryCurrencies(): List<CurrencyEntity>

    // ========== Transaction Entry Currencies (Main + Secondary) ==========
    
    @Query("""
        SELECT * FROM currencies 
        WHERE is_main_currency = 1 OR is_secondary = 1 
        ORDER BY is_main_currency DESC, order_index ASC
    """)
    fun observeTransactionCurrencies(): Flow<List<CurrencyEntity>>

    // ========== User-Defined Currencies ==========
    
    @Query("SELECT * FROM currencies WHERE is_user_defined = 1 ORDER BY code ASC")
    fun observeUserDefinedCurrencies(): Flow<List<CurrencyEntity>>

    // ========== Insert/Update Operations ==========
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currency: CurrencyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyEntity>)

    @Query("UPDATE currencies SET exchange_rate = :rate WHERE code = :code")
    suspend fun updateExchangeRate(code: String, rate: BigDecimal)
    
    // Legacy support - delegates to updateExchangeRate
    suspend fun updateRate(code: String, rate: BigDecimal) {
        updateExchangeRate(code, rate)
    }

    @Query("UPDATE currencies SET is_secondary = :isSecondary WHERE code = :code")
    suspend fun updateSecondaryStatus(code: String, isSecondary: Boolean)
    
    @Query("UPDATE currencies SET order_index = :orderIndex WHERE code = :code")
    suspend fun updateOrderIndex(code: String, orderIndex: Int)
    
    @Query("""
        UPDATE currencies 
        SET exchange_rate = :exchangeRate, 
            unit_position = :unitPosition, 
            decimal_digits = :decimalDigits 
        WHERE code = :code
    """)
    suspend fun updateSettings(
        code: String,
        exchangeRate: BigDecimal,
        unitPosition: String,
        decimalDigits: Int
    )

    // ========== Main Currency Management ==========
    
    @Query("UPDATE currencies SET is_main_currency = 0")
    suspend fun clearMainCurrency()
    
    @Query("UPDATE currencies SET is_main_currency = 1 WHERE code = :code")
    suspend fun setMainCurrencyInternal(code: String)
    
    // Legacy support
    @Query("UPDATE currencies SET isBaseCurrency = 0")
    suspend fun clearBaseCurrency()

    @Query("UPDATE currencies SET isBaseCurrency = 1 WHERE code = :code")
    suspend fun setBaseCurrencyInternal(code: String)

    @Transaction
    suspend fun setMainCurrency(code: String) {
        clearMainCurrency()
        setMainCurrencyInternal(code)
    }
    
    @Transaction
    suspend fun setBaseCurrency(code: String) {
        clearBaseCurrency()
        setBaseCurrencyInternal(code)
    }
    
    // ========== Delete Operations ==========
    
    @Query("DELETE FROM currencies WHERE code = :code AND is_main_currency = 0")
    suspend fun deleteByCode(code: String)
    
    @Query("SELECT COUNT(*) FROM currencies")
    suspend fun getCount(): Int
}
