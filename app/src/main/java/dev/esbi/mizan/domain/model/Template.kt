package dev.esbi.mizan.domain.model

data class Template(
    val id: Long,
    val name: String,
    val amount: Double,
    val iconName: String? = null,
    val transactionType: Transaction.Type,
    val categoryId: Long? = null
)
