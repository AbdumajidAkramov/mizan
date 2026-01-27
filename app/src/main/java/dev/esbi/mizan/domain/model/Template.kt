package dev.esbi.mizan.domain.model

interface Template {
    val id: Long
    val name: String
    val amount: Double
    val iconName: String?
    val transactionType: Transaction.Type
    val categoryId: Long?
}
