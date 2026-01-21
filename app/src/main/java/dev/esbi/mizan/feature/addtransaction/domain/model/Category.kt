package dev.esbi.mizan.feature.addtransaction.domain.model

data class Category(
    val id: String,
    val name: String,
    val iconName: String,
    val type: String, // "EXPENSE" or "INCOME"
    val color: String
)
