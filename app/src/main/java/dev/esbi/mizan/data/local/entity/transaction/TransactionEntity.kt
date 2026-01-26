package dev.esbi.mizan.data.local.entity.transaction

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.esbi.mizan.data.local.entity.account.AccountEntity
import dev.esbi.mizan.data.local.entity.category.CategoryEntity
import dev.esbi.mizan.domain.model.Transaction

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["targetAccountId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["subCategoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("date"), Index("accountId"), Index("categoryId")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,

    override val type: Transaction.Type,

    // 1. ASOSIY SUMMA (Chiqim yoki Kirim)
    // Expense uchun: Xarajat summasi (Account valyutasida)
    // Income uchun: Daromad summasi (Account valyutasida)
    // Transfer uchun: "From Account" dan qancha pul ketgani
    override val amount: Double,

    // 2. TRASFER UCHUN QO'SHIMCHA (Exchange Rate)
    // Faqat TRANSFER bo'lganda va valyutalar har xil bo'lganda to'ldiriladi.
    // "To Account" ga qancha pul tushgani (Target Currency da).
    override val targetAmount: Double? = null,

    override val date: Long,
    override val note: String? = null,
    override val description: String? = null,
    override val photoPaths: List<String> = emptyList(),

    // ... (Foreign Keylar o'zgarishsiz: accountId, categoryId...)
    override val accountId: Long? = null,       // Source Account

    override val categoryId: Long? = null,       // Kategoriya (Masalan: Daily Essentials)

    // Subkategoriya (Masalan: Fruits). Skrinshotda "Daily Essentials/Fruits" ko'rindi
    override val subCategoryId: Long? = null,

    // --- Transfer Specific ---
    override val targetAccountId: Long? = null,  // Faqat Transfer uchun: 'To Account'
    override val fee: Double = 0.0,              // Transfer komissiyasi (Fees)

    // --- Advanced Features (Skrinshotlardan kelib chiqib) ---

    // Bookmark (Yulduzcha tugmasi uchun)
    override val isBookmarked: Boolean = false,

    // Repeat / Takrorlanish (Every Day, Weekdays va h.k.)
    // Agar null bo'lsa - takrorlanmaydi.
    // Qiymat bo'lsa - qoida (masalan: "DAILY", "WEEKLY" yoki RRule string)
    override val recurrenceRule: String? = null,

    // Installment / Bo'lib to'lash (12 Months)
    override val isInstallment: Boolean = false,
    override val installmentTotalMonths: Int? = null, // Jami oylar (masalan, 12)
    override val installmentCurrentMonth: Int? = null, // Hozirgi oy (masalan, 1)

    // Agar bu tranzaksiya avtomatik yaratilgan bo'lsa, ota tranzaksiya IDsi
    override val parentTransactionId: Long? = null
) : Transaction
