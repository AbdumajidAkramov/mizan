package dev.esbi.mizan.feature.premiumaddtransaction.ui

/**
 * Pure-Kotlin representation of a quick template.
 * The UI layer in :app can map this to a richer model with Color/icon resources.
 */
data class QuickTemplate(
    val id: String,
    val name: String,
    val amount: Double,
    val category: String,
    val subcategory: String,
    val accountName: String,
    val typeColorHex: String = "",
    val iconName: String = ""
)
