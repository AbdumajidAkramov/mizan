package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_budgets")
data class CategoryBudgetEntity(
    @PrimaryKey val categoryId: String,
    val categoryName: String,
    val budgetAmount: Double,
    val spentAmount: Double
)
