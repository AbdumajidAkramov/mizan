package dev.esbi.mizan.presentation.feature.goals.domain.model

import java.math.BigDecimal

data class Goal(
    val id: Long = 0,
    val name: String,
    val targetAmount: BigDecimal,
    val currentAmount: BigDecimal,
    val deadline: Long? = null,
    val icon: String,
    val color: String
) {
    val progressPercent: Double
        get() = if (targetAmount > BigDecimal.ZERO) {
            (currentAmount.toDouble() / targetAmount.toDouble() * 100).coerceIn(0.0, 100.0)
        } else 0.0

    val isCompleted: Boolean
        get() = currentAmount >= targetAmount
}
