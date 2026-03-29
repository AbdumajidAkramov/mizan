package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_spending")
data class WeeklySpendingEntity(
    @PrimaryKey val dayLabel: String,
    val totalAmount: Double,
    val dayIndex: Int
)
