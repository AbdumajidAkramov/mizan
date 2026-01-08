package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_spending")
data class CategorySpendingEntity(
    @PrimaryKey val category: String,
    val categoryLabel: String,
    val totalAmount: Double,
    val transactionCount: Int,
    val percentage: Double,
    val colorToken: String
)
