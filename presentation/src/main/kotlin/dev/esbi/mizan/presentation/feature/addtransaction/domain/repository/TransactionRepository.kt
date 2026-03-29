package dev.esbi.mizan.presentation.feature.addtransaction.domain.repository

import dev.esbi.mizan.presentation.feature.addtransaction.domain.model.TransactionData
import dev.esbi.mizan.presentation.feature.addtransaction.domain.model.VoiceRecognitionResult

/**
 * Repository interface for transaction operations
 * Following Clean Architecture principles
 */
/*
interface TransactionRepository {
    
    */
/**
     * Save transaction to local storage
     *//*

    suspend fun saveTransaction(transactionData: TransactionData): Result<Unit>
    
    */
/**
     * Start voice recognition and return results as flow
     *//*

    suspend fun startVoiceRecognition(): Result<VoiceRecognitionResult>
    
    */
/**
     * Stop voice recognition
     *//*

    suspend fun stopVoiceRecognition(): Result<Unit>
    
    */
/**
     * Parse voice input text to extract transaction data
     *//*

    suspend fun parseVoiceInput(text: String): TransactionData
    
    */
/**
     * Extract amount from receipt text using regex
     *//*

    suspend fun extractAmountFromReceipt(text: String): Double
}
*/
