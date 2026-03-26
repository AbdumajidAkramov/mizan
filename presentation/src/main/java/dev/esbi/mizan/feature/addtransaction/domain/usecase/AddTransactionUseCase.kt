package dev.esbi.mizan.feature.addtransaction.domain.usecase

import dev.esbi.mizan.feature.addtransaction.domain.model.TransactionData
import dev.esbi.mizan.feature.addtransaction.domain.repository.TransactionRepository
import java.util.regex.Pattern
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
    
    // Enhanced voice patterns for better parsing
    private val voicePatterns = listOf(
        // "Lunch 15000" or "Lunch $15000"
        Pattern.compile("(.+?)\\s+(?:\\$|dollars?|usd)?\\s*(\\d{1,10}(?:\\.\\d{2})?)", Pattern.CASE_INSENSITIVE),
        
        // "$15000 Lunch" or "15000 Lunch"
        Pattern.compile("(?:\\$|dollars?|usd)?\\s*(\\d{1,10}(?:\\.\\d{2})?)\\s+(.+)", Pattern.CASE_INSENSITIVE),
        
        // "Lunch 15000 dollars"
        Pattern.compile("(.+?)\\s+(\\d{1,10}(?:\\.\\d{2})?)\\s*(?:dollars?|usd)", Pattern.CASE_INSENSITIVE),
        
        // "15000 for lunch"
        Pattern.compile("(\\d{1,10}(?:\\.\\d{2})?)\\s+(?:for|to)\\s+(.+)", Pattern.CASE_INSENSITIVE),
        
        // Simple "Lunch 15000"
        Pattern.compile("(.+?)\\s+(\\d{1,10}(?:\\.\\d{2})?)", Pattern.CASE_INSENSITIVE),
        
        // Just amount "15000"
        Pattern.compile("(\\d{1,10}(?:\\.\\d{2})?)")
    )
    
    // Common transaction categories to match
    private val commonCategories = listOf(
        "food", "lunch", "dinner", "breakfast", "coffee", "groceries", "restaurant",
        "transport", "uber", "taxi", "bus", "metro", "gas", "fuel", "parking",
        "shopping", "clothes", "electronics", "books", "gift",
        "entertainment", "movie", "concert", "game", "subscription",
        "health", "medicine", "doctor", "hospital", "pharmacy",
        "bills", "rent", "electricity", "water", "internet", "phone",
        "education", "course", "book", "tuition",
        "travel", "hotel", "flight", "vacation",
        "sports", "gym", "equipment"
    )
    
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
     * Enhanced voice input parsing with smart category detection
     */
    suspend fun parseVoiceInput(text: String): TransactionData {
        val cleanText = text.trim()
        
        for (pattern in voicePatterns) {
            val matcher = pattern.matcher(cleanText)
            if (matcher.find()) {
                val groups = matcher.groupCount()
                
                return when (groups) {
                    2 -> {
                        val group1 = matcher.group(1)?.trim() ?: ""
                        val group2 = matcher.group(2)?.trim() ?: ""
                        
                        val (amountStr, note) = if (group2.matches(Regex("\\d+(?:\\.\\d{2})?"))) {
                            group2 to group1
                        } else {
                            group1 to group2
                        }
                        
                        val amount = amountStr.toDoubleOrNull() ?: 0.0
                        val detectedCategory = detectCategory(note)
                        
                        TransactionData(
                            amount = amount,
                            note = note,
                            category = detectedCategory,
                            confidence = if (amount > 0) 0.9f else 0.3f
                        )
                    }
                    
                    1 -> {
                        val amountStr = matcher.group(1)?.trim() ?: "0"
                        val amount = amountStr.toDoubleOrNull() ?: 0.0
                        
                        TransactionData(
                            amount = amount,
                            note = "Voice transaction",
                            category = null,
                            confidence = if (amount > 0) 0.7f else 0.2f
                        )
                    }
                    
                    else -> TransactionData(
                        amount = 0.0,
                        note = cleanText,
                        category = null,
                        confidence = 0.1f
                    )
                }
            }
        }
        
        val numberPattern = Pattern.compile("(\\d{1,10}(?:\\.\\d{2})?)")
        val numberMatcher = numberPattern.matcher(cleanText)
        
        if (numberMatcher.find()) {
            val amount = numberMatcher.group(1)?.toDoubleOrNull() ?: 0.0
            return TransactionData(
                amount = amount,
                note = cleanText,
                category = null,
                confidence = 0.5f
            )
        }
        
        return TransactionData(
            amount = 0.0,
            note = cleanText,
            category = null,
            confidence = 0.1f
        )
    }
    
    private fun detectCategory(note: String): String? {
        val noteLower = note.lowercase()
        
        for (category in commonCategories) {
            if (noteLower.contains(category)) {
                return when (category) {
                    "food", "lunch", "dinner", "breakfast", "coffee", "groceries", "restaurant" -> "Food"
                    "transport", "uber", "taxi", "bus", "metro", "gas", "fuel", "parking" -> "Transport"
                    "shopping", "clothes", "electronics", "books", "gift" -> "Shopping"
                    "entertainment", "movie", "concert", "game", "subscription" -> "Entertainment"
                    "health", "medicine", "doctor", "hospital", "pharmacy" -> "Health"
                    "bills", "rent", "electricity", "water", "internet", "phone" -> "Bills"
                    "education", "course", "book", "tuition" -> "Education"
                    "travel", "hotel", "flight", "vacation" -> "Travel"
                    "sports", "gym", "equipment" -> "Sports"
                    else -> category.replaceFirstChar { it.uppercase() }
                }
            }
        }
        
        return null
    }
    
    /**
     * Extract amount from receipt text using regex patterns
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
}
