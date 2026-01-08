package dev.esbi.mizan.feature.transactions.domain.model

/**
 * Transaction domain model
 * Represents a financial transaction (income or expense)
 */
data class Transaction(
    val id: String,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val categoryName: String,
    val timestamp: Long,
    val description: String? = null
)

enum class TransactionType {
    INCOME,
    EXPENSE
}
