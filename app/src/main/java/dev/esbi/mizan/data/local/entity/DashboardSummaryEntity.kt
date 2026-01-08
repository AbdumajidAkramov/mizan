package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dashboard_summary")
data class DashboardSummaryEntity(
    @PrimaryKey val id: Int = 1,
    val totalBalance: Double,
    val monthlyExpenses: Double,
    val monthlySavings: Double,
    val budgetLimit: Double,
    val budgetPercentageUsed: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)
