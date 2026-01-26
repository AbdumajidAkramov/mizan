package dev.esbi.mizan.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.esbi.mizan.data.local.entity.account.AccountType
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromTransactionType(value: TransactionType): String = value.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)

    // Converters.kt
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
        fun toTransactionType(value: String) = enumValueOf<TransactionType>(value)

        @TypeConverter
        fun fromTransactionType(value: TransactionType) = value.name

        // 3. AccountType (String -> Enum)
        @TypeConverter
        fun toAccountType(value: String) = enumValueOf<AccountType>(value)

        @TypeConverter
        fun fromAccountType(value: AccountType) = value.name

        // 4. PhotoPaths (JSON String -> List<String>)
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
}
