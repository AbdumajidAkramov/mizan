package dev.esbi.mizan.domain.model.profile

/**
 * User profile information
 */
data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val memberSince: String,
    val totalTransactions: Int,
    val totalIncome: java.math.BigDecimal,
    val totalExpense: java.math.BigDecimal,
    val totalSaved: java.math.BigDecimal,
    val avatarInitials: String
)
