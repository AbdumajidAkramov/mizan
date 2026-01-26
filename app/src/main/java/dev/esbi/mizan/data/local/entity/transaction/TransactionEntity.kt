package dev.esbi.mizan.data.local.entity.transaction
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val type: TransactionType,

    // 1. ASOSIY SUMMA (Chiqim yoki Kirim)
    // Expense uchun: Xarajat summasi (Account valyutasida)
    // Income uchun: Daromad summasi (Account valyutasida)
    // Transfer uchun: "From Account" dan qancha pul ketgani
    val amount: Double,

    // 2. TRASFER UCHUN QO'SHIMCHA (Exchange Rate)
    // Faqat TRANSFER bo'lganda va valyutalar har xil bo'lganda to'ldiriladi.
    // "To Account" ga qancha pul tushgani (Target Currency da).
    val targetAmount: Double? = null,

    val date: Long,
    val note: String? = null,
    val description: String? = null,
    val photoPaths: List<String> = emptyList(),

    // ... (Foreign Keylar o'zgarishsiz: accountId, categoryId...)
    val accountId: Long? = null,       // Source Account

    val categoryId: Long? = null,       // Kategoriya (Masalan: Daily Essentials)

    // Subkategoriya (Masalan: Fruits). Skrinshotda "Daily Essentials/Fruits" ko'rindi
    val subCategoryId: Long? = null,

    // --- Transfer Specific ---
    val targetAccountId: Long? = null,  // Faqat Transfer uchun: 'To Account'
    val fee: Double = 0.0,              // Transfer komissiyasi (Fees)

    // --- Advanced Features (Skrinshotlardan kelib chiqib) ---

    // Bookmark (Yulduzcha tugmasi uchun)
    val isBookmarked: Boolean = false,

    // Repeat / Takrorlanish (Every Day, Weekdays va h.k.)
    // Agar null bo'lsa - takrorlanmaydi.
    // Qiymat bo'lsa - qoida (masalan: "DAILY", "WEEKLY" yoki RRule string)
    val recurrenceRule: String? = null,

    // Installment / Bo'lib to'lash (12 Months)
    val isInstallment: Boolean = false,
    val installmentTotalMonths: Int? = null, // Jami oylar (masalan, 12)
    val installmentCurrentMonth: Int? = null, // Hozirgi oy (masalan, 1)

    // Agar bu tranzaksiya avtomatik yaratilgan bo'lsa, ota tranzaksiya IDsi
    val parentTransactionId: Long? = null
)
