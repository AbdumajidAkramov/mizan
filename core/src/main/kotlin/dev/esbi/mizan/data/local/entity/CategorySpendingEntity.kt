package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

import java.math.BigDecimal

@Entity(tableName = "category_spending")
data class CategorySpendingEntity(
    @PrimaryKey val category: String,
    val categoryLabel: String,
    val totalAmount: BigDecimal,
    val transactionCount: Int,
    val percentage: BigDecimal,
    val colorToken: String
)
