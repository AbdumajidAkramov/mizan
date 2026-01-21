package dev.esbi.mizan.data.local

import androidx.room.TypeConverter
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
    fun fromTransactionType(transactionType: TransactionType): String {
        return transactionType.name
    }
    
    @TypeConverter
    fun toTransactionType(transactionType: String): TransactionType {
        return TransactionType.valueOf(transactionType)
    }
}
