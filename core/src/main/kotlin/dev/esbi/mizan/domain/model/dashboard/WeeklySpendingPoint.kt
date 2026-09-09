package dev.esbi.mizan.domain.model.dashboard

data class WeeklySpendingPoint(
    val dayLabel: String,
    val totalAmount: Double,
    val dayIndex: Int
)
