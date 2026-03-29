package dev.esbi.mizan.presentation.feature.addtransaction.domain.model

import dev.esbi.mizan.presentation.feature.addtransaction.presentation.models.TransactionType
import java.util.Date

/**
 * Represents parsed transaction data from voice input or receipt scanning
 */
data class TransactionData(
    val amount: Double,
    val note: String,
    val category: String? = null,
    val confidence: Float = 1.0f,
    val type: TransactionType = TransactionType.EXPENSE,
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
 * Represents receipt scan result (pure Kotlin — no android.graphics.PointF)
 */
data class ReceiptScanResult(
    val text: String,
    val confidence: Float
)
