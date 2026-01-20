package dev.esbi.mizan.feature.addtransaction.presentation.models

import java.time.LocalDate

data class TransactionResult(
    val amount: Double,
    val type: TransactionType,
    val category: String? = null,
    val fromAccountId: String? = null,
    val toAccountId: String? = null,
    val date: LocalDate,
    val notes: String? = null,
    val categoryId: Long = 0
)
