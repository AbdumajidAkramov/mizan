package dev.esbi.mizan.feature.addtransaction.domain.usecase

import dev.esbi.mizan.feature.addtransaction.domain.model.TransactionData
import dev.esbi.mizan.feature.addtransaction.domain.repository.TransactionRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for adding transactions
 * Following Clean Architecture principles
 */
@Singleton
class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    
    /**
     * Execute transaction addition
     */
    suspend fun execute(
        amount: Double,
        category: String,
        note: String
    ): Result<Unit> {
        val transactionData = TransactionData(
            amount = amount,
            note = note,
            category = category
        )
        return repository.saveTransaction(transactionData)
    }
    
    /**
     * Parse voice input text to extract transaction data
     * Examples:
     * "Lunch $15" -> TransactionData(amount = 15.0, note = "Lunch")
     * "Coffee 5.50" -> TransactionData(amount = 5.50, note = "Coffee")
     * "Uber ride 25.99" -> TransactionData(amount = 25.99, note = "Uber ride")
     */
    suspend fun parseVoiceInput(text: String): TransactionData {
        return repository.parseVoiceInput(text)
    }
    
    /**
     * Extract amount from receipt text using regex patterns
     * Looks for patterns like:
     * - Total: $25.99
     * - Amount: 15.50
     * - $42.00
     */
    suspend fun extractAmountFromReceipt(text: String): Double {
        return repository.extractAmountFromReceipt(text)
    }
    
    /**
     * Start voice recognition
     */
    suspend fun startVoiceRecognition() = repository.startVoiceRecognition()
    
    /**
     * Stop voice recognition
     */
    suspend fun stopVoiceRecognition() = repository.stopVoiceRecognition()
    
    /**
     * Scan receipt for text
     */
    suspend fun scanReceipt(imageProxy: androidx.camera.core.ImageProxy) = 
        repository.scanReceipt(imageProxy)
}
