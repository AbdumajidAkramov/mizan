package dev.esbi.mizan.presentation.feature.goals.domain.model

data class Goal(
    val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val deadline: Long? = null,
    val icon: String,
    val color: String
) {
    val progressPercent: Double
        get() = if (targetAmount > 0) ((currentAmount / targetAmount) * 100).coerceIn(0.0, 100.0) else 0.0

    val isCompleted: Boolean
        get() = currentAmount >= targetAmount
}
