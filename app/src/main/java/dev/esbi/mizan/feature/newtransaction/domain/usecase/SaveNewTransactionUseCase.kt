package dev.esbi.mizan.feature.newtransaction.domain.usecase

import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.newtransaction.domain.repository.NewTransactionRepository
import javax.inject.Inject

class SaveNewTransactionUseCase @Inject constructor(
    private val repository: NewTransactionRepository
) {
    suspend operator fun invoke(
        amount: Double,
        type: TransactionType,
        categoryId: String?,
        accountId: String,
        note: String,
        date: Long
    ): Result<Unit> {
        return try {
            // Business logic validation
            if (amount <= 0) {
                return Result.failure(IllegalArgumentException("Amount must be greater than 0"))
            }
            
            if (accountId.isBlank()) {
                return Result.failure(IllegalArgumentException("Account ID is required"))
            }
            
            // Call repository to save the transaction
            repository.saveTransaction(
                amount = amount,
                type = type,
                categoryId = categoryId,
                accountId = accountId,
                note = note,
                date = date
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
