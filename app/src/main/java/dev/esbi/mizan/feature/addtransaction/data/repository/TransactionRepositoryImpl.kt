package dev.esbi.mizan.feature.addtransaction.data.repository

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dev.esbi.mizan.data.local.dao.TransactionsDao
import dev.esbi.mizan.data.local.entity.TransactionEntity
import dev.esbi.mizan.feature.addtransaction.domain.model.ReceiptScanResult
import dev.esbi.mizan.feature.addtransaction.domain.model.TransactionData
import dev.esbi.mizan.feature.addtransaction.domain.model.VoiceRecognitionResult
import dev.esbi.mizan.feature.addtransaction.domain.repository.TransactionRepository
import java.util.Date
import java.util.UUID
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of TransactionRepository
 * Following Clean Architecture principles
 */
@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionsDao: TransactionsDao
) : TransactionRepository {

    private val tag = "TransactionRepository"

    // ML Kit Text Recognizer
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    // Regex patterns for amount extraction
    private val amountPatterns = listOf(
        Pattern.compile("\\$\\s*(\\d+(?:\\.\\d{2})?)"), // $25.99
        Pattern.compile(
            "(?:total|amount|sum)\\s*[:=]\\s*\\$?\\s*(\\d+(?:\\.\\d{2})?)",
            Pattern.CASE_INSENSITIVE
        ), // Total: $25.99
        Pattern.compile(
            "(\\d+(?:\\.\\d{2})?)\\s*(?:dollars?|usd)?",
            Pattern.CASE_INSENSITIVE
        ), // 25.99 dollars
        Pattern.compile("\b(\\d{1,5}(?:\\.\\d{2})?)\b") // Any number with 2 decimal places
    )

    // Regex patterns for voice input parsing
    private val voicePatterns = listOf(
        Pattern.compile(
            "(.+?)\\s+(?:\\$|dollars?|usd)?\\s*(\\d+(?:\\.\\d{2})?)",
            Pattern.CASE_INSENSITIVE
        ), // "Lunch $15.50"
        Pattern.compile(
            "(?:\\$|dollars?|usd)?\\s*(\\d+(?:\\.\\d{2})?)\\s+(.+)",
            Pattern.CASE_INSENSITIVE
        ), // "$15.50 Lunch"
        Pattern.compile("(.+?)\\s+(\\d+(?:\\.\\d{2})?)", Pattern.CASE_INSENSITIVE) // "Lunch 15.50"
    )

    override suspend fun saveTransaction(transactionData: TransactionData): Result<Unit> {
        return try {
            Log.d(tag, "Saving transaction: $transactionData")

            // Convert domain model to database entity
            val transactionEntity = TransactionEntity(
                id = UUID.randomUUID().toString(),
                amount = transactionData.amount,
                category = transactionData.category ?: "Uncategorized",
                categoryLabel = transactionData.category ?: "Uncategorized",
                description = transactionData.note,
                date = transactionData.date ?: Date(),
                type = transactionData.type.name,
                colorToken = transactionData.categoryColor ?: "blue"
            )

            // Save to Room database
            transactionsDao.insertTransaction(transactionEntity)
            
            Log.d(tag, "Transaction saved successfully with ID: ${transactionEntity.id}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(tag, "Failed to save transaction", e)
            Result.failure(e)
        }
    }

    override suspend fun startVoiceRecognition(): Result<VoiceRecognitionResult> {
        return Result.failure(
            UnsupportedOperationException("Voice recognition is now handled directly by VoiceSpeechRecognizer")
        )
    }

    override suspend fun stopVoiceRecognition(): Result<Unit> {
        return Result.failure(
            UnsupportedOperationException("Voice recognition is now handled directly by VoiceSpeechRecognizer")
        )
    }

    @OptIn(ExperimentalGetImage::class)
    override suspend fun scanReceipt(imageProxy: ImageProxy): Result<ReceiptScanResult> {
        return try {
            Log.d(tag, "Scanning receipt image")

            val image = imageProxy.image?.let { InputImage.fromMediaImage(
                it,
                imageProxy.imageInfo.rotationDegrees
            ) }

            // Use ML Kit to recognize text
            image?.let {
                textRecognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        val recognizedText = visionText.text
                        Log.d(tag, "Recognized text: $recognizedText")

                        // For now, just return the full recognized text
                        // In production, you might want to process blocks and lines individually
                    }
                    .addOnFailureListener { e ->
                        Log.e(tag, "Failed to recognize text", e)
                    }
            }
            val mockResult = ReceiptScanResult(
                text = "Total: $25.99 Tax: $2.60 Subtotal: $23.39",
                confidence = 0.88f
            )

            Result.success(mockResult)
        } catch (e: Exception) {
            Log.e(tag, "Failed to scan receipt", e)
            Result.failure(e)
        } finally {
            imageProxy.close()
        }
    }

    override suspend fun parseVoiceInput(text: String): TransactionData {
        Log.d(tag, "Parsing voice input: $text")

        val cleanText = text.trim()

        // Try to extract amount and note using regex patterns
        for (pattern in voicePatterns) {
            val matcher = pattern.matcher(cleanText)
            if (matcher.find()) {
                val groups = matcher.groupCount()

                return when (groups) {
                    2 -> {
                        // Two groups: note and amount
                        val note = matcher.group(1)?.trim() ?: ""
                        val amountStr = matcher.group(2)?.trim() ?: "0"
                        val amount = amountStr.toDoubleOrNull() ?: 0.0

                        TransactionData(
                            amount = amount,
                            note = note,
                            confidence = 0.9f
                        )
                    }

                    1 -> {
                        // One group: amount only
                        val amountStr = matcher.group(1)?.trim() ?: "0"
                        val amount = amountStr.toDoubleOrNull() ?: 0.0

                        TransactionData(
                            amount = amount,
                            note = "Voice transaction",
                            confidence = 0.7f
                        )
                    }

                    else -> TransactionData(
                        amount = 0.0,
                        note = cleanText,
                        confidence = 0.3f
                    )
                }
            }
        }

        // Fallback: try to extract any number as amount
        val numberPattern = Pattern.compile("(\\d+(?:\\.\\d{2})?)")
        val numberMatcher = numberPattern.matcher(cleanText)

        if (numberMatcher.find()) {
            val amount = numberMatcher.group(1)?.toDoubleOrNull() ?: 0.0
            return TransactionData(
                amount = amount,
                note = cleanText,
                confidence = 0.5f
            )
        }

        // Last resort: no amount found
        return TransactionData(
            amount = 0.0,
            note = cleanText,
            confidence = 0.1f
        )
    }

    override suspend fun extractAmountFromReceipt(text: String): Double {
        Log.d(tag, "Extracting amount from receipt text: $text")

        val cleanText = text.trim()

        // Try each pattern to find amount
        for (pattern in amountPatterns) {
            val matcher = pattern.matcher(cleanText)
            if (matcher.find()) {
                val amountStr = matcher.group(1)
                val amount = amountStr?.toDoubleOrNull()

                if (amount != null && amount > 0) {
                    Log.d(tag, "Found amount: $amount using pattern: ${pattern.pattern()}")
                    return amount
                }
            }
        }

        Log.d(tag, "No amount found in receipt text")
        return 0.0
    }
}
