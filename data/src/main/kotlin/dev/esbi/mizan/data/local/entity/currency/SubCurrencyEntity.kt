package dev.esbi.mizan.data.local.entity.currency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sub_currencies")
data class SubCurrencyEntity(
    @PrimaryKey
    val code: String,

    val name: String,

    val symbol: String,

    @ColumnInfo(name = "exchange_rate")
    val exchangeRate: String, // BigDecimal stored as String for precision

    @ColumnInfo(name = "unit_position", defaultValue = "FRONT")
    val unitPosition: String = "FRONT", // "FRONT" or "END"

    @ColumnInfo(name = "decimal_digits", defaultValue = "2")
    val decimalDigits: Int = 2,

    @ColumnInfo(name = "order_index", defaultValue = "0")
    val orderIndex: Int = 0,

    @ColumnInfo(name = "is_main_currency", defaultValue = "0")
    val isMainCurrency: Boolean = false,

    @ColumnInfo(name = "is_user_defined", defaultValue = "0")
    val isUserDefined: Boolean = false
)
