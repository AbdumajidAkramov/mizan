package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "dashboard_summary")
data class DashboardSummaryEntity(
    @PrimaryKey val id: Int = 1,
    val totalBalance: BigDecimal,
    val monthlyExpenses: BigDecimal,
    val monthlySavings: BigDecimal,
    val budgetLimit: BigDecimal,
    val budgetPercentageUsed: BigDecimal,
    val lastUpdated: Long = System.currentTimeMillis()
)
