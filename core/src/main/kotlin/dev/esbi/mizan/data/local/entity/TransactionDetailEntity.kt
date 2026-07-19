package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

import java.math.BigDecimal

@Entity(tableName = "transaction_details")
data class TransactionDetailEntity(
    @PrimaryKey val id: String,
    val title: String,
    val amount: BigDecimal,
    val type: String,
    val category: String,
    val categoryName: String,
    val timestamp: Long,
    val description: String?
)
