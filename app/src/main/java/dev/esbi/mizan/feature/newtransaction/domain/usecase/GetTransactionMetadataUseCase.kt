package dev.esbi.mizan.feature.newtransaction.domain.usecase

import dev.esbi.mizan.feature.addtransaction.domain.model.Account
import dev.esbi.mizan.feature.addtransaction.domain.model.Category
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.newtransaction.domain.model.TransactionMetadata
import dev.esbi.mizan.feature.newtransaction.domain.repository.NewTransactionRepository
import javax.inject.Inject

class GetTransactionMetadataUseCase @Inject constructor(
    private val repository: NewTransactionRepository
) {
    suspend operator fun invoke(type: TransactionType): Result<TransactionMetadata> {
        return try {
            val categoriesResult = repository.getCategories(type)
            val accountsResult = repository.getAccounts()
            
            if (categoriesResult.isSuccess && accountsResult.isSuccess) {
                Result.success(
                    TransactionMetadata(
                        categories = categoriesResult.getOrDefault(emptyList()),
                        accounts = accountsResult.getOrDefault(emptyList())
                    )
                )
            } else {
                // Combine errors if both failed, or return the failed one
                val categoriesError = categoriesResult.exceptionOrNull()
                val accountsError = accountsResult.exceptionOrNull()
                
                val error = when {
                    categoriesError != null && accountsError != null -> 
                        Exception("Failed to load categories and accounts: ${categoriesError.message}, ${accountsError.message}")
                    categoriesError != null -> categoriesError
                    accountsError != null -> accountsError
                    else -> Exception("Unknown error occurred")
                }
                Result.failure(error)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
