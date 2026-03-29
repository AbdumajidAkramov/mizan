package dev.esbi.mizan.presentation.feature.dashboard.domain.model

data class WeeklySpendingPoint(
    val dayLabel: String,
    val totalAmount: Double,
    val dayIndex: Int
)
