package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

import java.math.BigDecimal

@Entity(tableName = "financial_projections")
data class FinancialProjectionEntity(
    @PrimaryKey val year: String,
    val conservative: BigDecimal,
    val realistic: BigDecimal,
    val optimistic: BigDecimal
)
