package dev.esbi.mizan.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.domain.model.AccountGroupType
import java.math.BigDecimal
import java.util.Date

class Converters {
    // 1. Date (Long -> Date)
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // 2. TransactionType (String -> Enum)
    @TypeConverter
    fun toTransactionType(value: String): Transaction.Type = enumValueOf<Transaction.Type>(value)

    @TypeConverter
    fun fromTransactionType(value: Transaction.Type): String = value.name

    // 3. AccountGroupType (String -> Enum)
    @TypeConverter
    fun toAccountGroupType(value: String): AccountGroupType = enumValueOf<AccountGroupType>(value)

    @TypeConverter
    fun fromAccountGroupType(value: AccountGroupType): String = value.name

    // 4. BigDecimal (String -> BigDecimal)
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    // 5. PhotoPaths/String Lists (JSON String -> List<String>)
    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String {
        return Gson().toJson(list)
    }
}
