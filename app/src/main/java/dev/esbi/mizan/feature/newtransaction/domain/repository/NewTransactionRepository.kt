package dev.esbi.mizan.feature.newtransaction.domain.repository

import dev.esbi.mizan.feature.addtransaction.domain.model.Account
import dev.esbi.mizan.feature.addtransaction.domain.model.Category
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType

interface NewTransactionRepository {
    suspend fun getCategories(type: TransactionType): Result<List<Category>>
    suspend fun getAccounts(): Result<List<Account>>
    suspend fun saveTransaction(
        amount: Double,
        type: TransactionType,
        categoryId: String?,
        accountId: String,
        note: String,
        date: Long
    ): Result<Unit>
}
