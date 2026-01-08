package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "financial_projections")
data class FinancialProjectionEntity(
    @PrimaryKey val year: String,
    val conservative: Double,
    val realistic: Double,
    val optimistic: Double
)
