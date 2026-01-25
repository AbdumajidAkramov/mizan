package dev.esbi.mizan.feature.newtransaction.amountinput.voice

import java.util.regex.Pattern

/**
 * Parses voice input to extract amount and note/category information
 */
data class VoiceParseResult(
    val amount: Double?,
    val note: String?
)

/**
 * Utility class to parse transaction details from voice input
 */
class TransactionVoiceParser {
    
    companion object {
        // Regex patterns for amount extraction
        private val AMOUNT_PATTERNS = listOf(
            // Numbers with optional decimal points and commas
            Pattern.compile("(?<!\\d)(\\d{1,3}(?:,\\d{3})*(?:\\.\\d+)?)(?!\\d)"),
            // Numbers with decimal points only
            Pattern.compile("(?<!\\d)(\\d+(?:\\.\\d+)?)(?!\\d)"),
            // Numbers with "k" suffix (e.g., "50k", "100k")
            Pattern.compile("(?<!\\d)(\\d+(?:\\.\\d+)?)k(?!\\d)", Pattern.CASE_INSENSITIVE),
            // Numbers with "K" suffix
            Pattern.compile("(?<!\\d)(\\d+(?:\\.\\d+)?)K(?!\\d)"),
            // Simple digits
            Pattern.compile("(?<!\\d)(\\d+)(?!\\d)")
        )
    }
    
    /**
     * Parses voice input text to extract amount and note
     */
    fun parse(text: String): VoiceParseResult {
        val cleanText = text.trim()
        
        if (cleanText.isEmpty()) {
            return VoiceParseResult(null, null)
        }
        
        val amount = extractAmount(cleanText)
        val note = extractNote(cleanText, amount)
        
        return VoiceParseResult(amount, note)
    }
    
    /**
     * Extracts the first numeric amount from the text
     */
    private fun extractAmount(text: String): Double? {
        for (pattern in AMOUNT_PATTERNS) {
            val matcher = pattern.matcher(text)
            if (matcher.find()) {
                val amountStr = matcher.group(1) ?: matcher.group(0)
                return parseAmountString(amountStr)
            }
        }
        return null
    }
    
    /**
     * Converts amount string to double, handling "k" suffixes
     */
    private fun parseAmountString(amountStr: String): Double? {
        return try {
            val cleanAmount = amountStr.replace(",", "")
            
            when {
                cleanAmount.endsWith("k", ignoreCase = true) -> {
                    val numberPart = cleanAmount.dropLast(1)
                    numberPart.toDoubleOrNull()?.times(1000)
                }
                cleanAmount.endsWith("K") -> {
                    val numberPart = cleanAmount.dropLast(1)
                    numberPart.toDoubleOrNull()?.times(1000)
                }
                else -> {
                    cleanAmount.toDoubleOrNull()
                }
            }
        } catch (e: NumberFormatException) {
            null
        }
    }
    
    /**
     * Extracts note/category text by removing the amount from the original text
     */
    private fun extractNote(text: String, amount: Double?): String? {
        if (amount == null) {
            // If no amount found, return the cleaned text as note
            return text.trim().takeIf { it.isNotEmpty() }
        }
        
        // Find and remove the amount from text to get the note
        var noteText = text
        
        // Try to find and remove various amount formats
        for (pattern in AMOUNT_PATTERNS) {
            val matcher = pattern.matcher(noteText)
            if (matcher.find()) {
                noteText = noteText.replace(matcher.group(0), "").trim()
                break
            }
        }
        
        // Clean up common prepositions and connectors
        noteText = noteText
            .replace(Regex("\\b(for|in|to|at|on|of|with|and|&)\\b"), "")
            .replace(Regex("\\s+"), " ")
            .trim()
        
        return noteText.takeIf { it.isNotEmpty() }
    }
}
