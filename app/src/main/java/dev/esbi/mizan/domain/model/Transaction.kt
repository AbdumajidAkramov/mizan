package dev.esbi.mizan.domain.model

data class Transaction(
    val id: Long,
    val type: Type,
    // 1. ASOSIY SUMMA (Chiqim yoki Kirim)
    // Expense uchun: Xarajat summasi (Account valyutasida)
    // Income uchun: Daromad summasi (Account valyutasida)
    // Transfer uchun: "From Account" dan qancha pul ketgani
    val amount: Double,
    // Transaction currency at the time of transaction
    val currency: Currency,
    // Exchange rate snapshot at the time of transaction (currency -> base)
    val exchangeRate: Double,
    // 2. TRANSFER UCHUN QO'SHIMCHA
    // Faqat TRANSFER bo'lganda va valyutalar har xil bo'lganda to'ldiriladi.
    val targetAmount: Double? = null,
    val date: Long,
    val note: String? = null,
    val description: String? = null,
    val photoPaths: List<String> = emptyList(),
    // ... (Foreign Keylar o'zgarishsiz: accountId, categoryId...)
    val accountId: Long? = null,       // Source Account
    val categoryId: Long? = null,
    val subCategoryId: Long? = null,   // Deprecated - not used in new schema
    // --- Transfer Specific ---
    val targetAccountId: Long? = null,
    val fee: Double = 0.0,             // Deprecated - not used in new schema
    val isBookmarked: Boolean = false,
    val recurrenceRule: String? = null,
    val isInstallment: Boolean = false,
    val installmentTotalMonths: Int? = null,
    val installmentCurrentMonth: Int? = null,
    val parentTransactionId: Long? = null,
    // --- Fiscal Information ---
    val merchantName: String? = null,   // Merchant name for fiscal receipts
    val fiscalSign: String? = null      // Fiscal sign for fiscal receipts
) {
    enum class Type {
        EXPENSE,
        INCOME,
        TRANSFER
    }
}