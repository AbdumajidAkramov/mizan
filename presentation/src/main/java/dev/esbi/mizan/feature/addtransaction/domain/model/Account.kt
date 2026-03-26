package dev.esbi.mizan.feature.addtransaction.domain.model

data class Account(
    val id: String,
    val name: String,
    val iconName: String,
    val currentBalance: Double,
    val currency: String
)
