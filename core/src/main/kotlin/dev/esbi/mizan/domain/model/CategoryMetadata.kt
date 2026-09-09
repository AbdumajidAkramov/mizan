package dev.esbi.mizan.domain.model

interface CategoryMetadata {
    val id: Long
    val label: String
    val groupType: String // EXPENSE / INCOME
    val slug: String      // "expense_must", "income_active"
    val color: String?
        get() = null

}
