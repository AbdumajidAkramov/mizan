package dev.esbi.mizan.feature.addtransaction.domain.model

import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import java.util.Date

/**
 * Represents parsed transaction data from voice input or receipt scanning
 */
data class TransactionData(
    val amount: Double,
    val note: String,
    val category: String? = null,
    val confidence: Float = 1.0f,
    val type: TransactionType = TransactionType.Expense,
    val date: Date? = null,
    val categoryColor: String? = null
)

/**
 * Represents voice recognition result
 */
data class VoiceRecognitionResult(
    val text: String,
    val confidence: Float,
    val isFinal: Boolean
)

/**
 * Represents text recognition result from receipt scanning
 */
data class ReceiptScanResult(
    val text: String,
    val boundingBox: List<android.graphics.PointF>? = null,
    val confidence: Float
)
