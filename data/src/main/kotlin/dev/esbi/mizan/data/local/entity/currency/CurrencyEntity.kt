package dev.esbi.mizan.data.local.entity.currency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

/**
 * Unified Currency Entity - Single source of truth for all currencies.
 * 
 * Replaces both CurrencyEntity and SubCurrencyEntity.
 * 
 * Flags:
 * - isMainCurrency: The single primary app currency (only one can be true)
 * - isSecondary: User-selected currencies for quick access in transaction entry
 * - isUserDefined: Custom currencies created by the user
 */
@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey
    val code: String,
    
    val name: String,
    
    val symbol: String,
    
    // Exchange rate to base currency (for reports and total balance)
    @ColumnInfo(name = "exchange_rate")
    val exchangeRate: BigDecimal = BigDecimal.ONE,
    
    // Symbol position: "FRONT" ($100) or "END" (100 so'm)
    @ColumnInfo(name = "unit_position", defaultValue = "END")
    val unitPosition: String = "END",
    
    // Number of decimal places to display
    @ColumnInfo(name = "decimal_digits", defaultValue = "2")
    val decimalDigits: Int = 2,
    
    // Display order in lists
    @ColumnInfo(name = "order_index", defaultValue = "0")
    val orderIndex: Int = 0,
    
    // The single primary app currency (only one can be true)
    @ColumnInfo(name = "is_main_currency", defaultValue = "0")
    val isMainCurrency: Boolean = false,
    
    // User-selected currencies for quick access in transaction entry
    @ColumnInfo(name = "is_secondary", defaultValue = "0")
    val isSecondary: Boolean = false,
    
    // Custom currencies created by the user
    @ColumnInfo(name = "is_user_defined", defaultValue = "0")
    val isUserDefined: Boolean = false,
    
    // Legacy field for backward compatibility (will be migrated to isMainCurrency)
    @ColumnInfo(name = "isBaseCurrency", defaultValue = "0")
    val isBaseCurrency: Boolean = false
)
