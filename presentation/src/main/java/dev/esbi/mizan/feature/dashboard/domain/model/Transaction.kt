package dev.esbi.mizan.feature.dashboard.domain.model

import java.util.Date

data class Transaction(
    val id: String,
    val amount: Double,
    val category: String,
    val categoryLabel: String,
    val description: String,
    val date: Date,
    val type: TransactionType,
    val colorToken: String
)

enum class TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER
}
