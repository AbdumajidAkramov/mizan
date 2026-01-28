package dev.esbi.mizan.feature.newtransaction.confirm.state

import dev.esbi.mizan.domain.model.Transaction

data class ConfirmTransactionUiState(
    val amount: String,
    val currencyCode: String,
    val transactionType: Transaction.Type,
    val categoryName: String?,
    val subCategoryName: String? = null,
    val categoryIcon: String?,
    val accountName: String,
    val toAccountName: String? = null,
    val date: Long,
    val note: String,
    val saveAsTemplate: Boolean = false,
    val isLoading: Boolean = false
)
