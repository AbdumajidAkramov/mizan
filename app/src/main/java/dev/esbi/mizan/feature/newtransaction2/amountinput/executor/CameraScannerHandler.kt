package dev.esbi.mizan.feature.newtransaction2.amountinput.executor

import javax.inject.Inject

/**
 * Handles calculator logic for keypad inputs.
 * Pure math and string manipulation logic without side effects.
 */
internal class CameraScannerHandler @Inject constructor() {

    private fun extractAmountFromText(text: String): Double? {
        val amountPatterns = listOf(
            Regex("\\$\\s*(\\d+(?:\\.\\d{2})?)"), // $25.99
            Regex(
                "(?:total|amount|sum)\\s*[:=]\\s*\\$?\\s*(\\d+(?:\\.\\d{2})?)",
                RegexOption.IGNORE_CASE
            ), // Total: $25.99
            Regex(
                "(\\d+(?:\\.\\d{2})?)\\s*(?:dollars?|usd)?",
                RegexOption.IGNORE_CASE
            ), // 25.99 dollars
            Regex("\\b(\\d{1,5}(?:\\.\\d{2})?)\\b") // Any number with 2 decimal places
        )

        for (pattern in amountPatterns) {
            val match = pattern.find(text)
            if (match != null) {
                return match.groupValues[1].toDoubleOrNull()
            }
        }
        return null
    }
}
