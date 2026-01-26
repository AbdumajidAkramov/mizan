package dev.esbi.mizan.domain.model

interface Transaction {
    val id: Long

    val type: Type

    // 1. ASOSIY SUMMA (Chiqim yoki Kirim)
    // Expense uchun: Xarajat summasi (Account valyutasida)
    // Income uchun: Daromad summasi (Account valyutasida)
    // Transfer uchun: "From Account" dan qancha pul ketgani
    val amount: Double

    // 2. TRASFER UCHUN QO'SHIMCHA (Exchange Rate)
    // Faqat TRANSFER bo'lganda va valyutalar har xil bo'lganda to'ldiriladi.
    // "To Account" ga qancha pul tushgani (Target Currency da).
    val targetAmount: Double?

    val date: Long
    val note: String?
    val description: String?
    val photoPaths: List<String>

    // ... (Foreign Keylar o'zgarishsiz: accountId, categoryId...)
    val accountId: Long?       // Source Account

    val categoryId: Long?       // Kategoriya (Masalan: Daily Essentials)

    // Subkategoriya (Masalan: Fruits). Skrinshotda "Daily Essentials/Fruits" ko'rindi
    val subCategoryId: Long?

    // --- Transfer Specific ---
    val targetAccountId: Long?  // Faqat Transfer uchun: 'To Account'
    val fee: Double          // Transfer komissiyasi (Fees)

    // --- Advanced Features (Skrinshotlardan kelib chiqib) ---

    // Bookmark (Yulduzcha tugmasi uchun)
    val isBookmarked: Boolean

    // Repeat / Takrorlanish (Every Day, Weekdays va h.k.)
    // Agar null bo'lsa - takrorlanmaydi.
    // Qiymat bo'lsa - qoida (masalan: "DAILY", "WEEKLY" yoki RRule string)
    val recurrenceRule: String?

    // Installment / Bo'lib to'lash (12 Months)
    val isInstallment: Boolean
    val installmentTotalMonths: Int? // Jami oylar (masalan, 12)
    val installmentCurrentMonth: Int? // Hozirgi oy (masalan, 1)

    // Agar bu tranzaksiya avtomatik yaratilgan bo'lsa, ota tranzaksiya IDsi
    val parentTransactionId: Long?

    enum class Type {
        EXPENSE,
        INCOME,
        TRANSFER
    }
}