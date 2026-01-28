package dev.esbi.mizan.feature.newtransaction.confirm.state

import dev.esbi.mizan.domain.model.Transaction

data class ConfirmTransactionUiState(
    val amount: String,
    val currencyCode: String,
    val transactionType: Transaction.Type,
    val categoryName: String?,
    val categoryIcon: String?,
    val accountName: String,
    val toAccountName: String? = null,
    val date: Long,
    val note: String,
    val isLoading: Boolean = false
)
