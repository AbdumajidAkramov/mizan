package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

import java.math.BigDecimal

@Entity(tableName = "weekly_spending")
data class WeeklySpendingEntity(
    @PrimaryKey val dayLabel: String,
    val totalAmount: BigDecimal,
    val dayIndex: Int
)
