package dev.esbi.mizan.data.local.entity.account

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity

enum class AccountType {
    CASH,       // Naqd pul
    CARD,       // Plastik karta (Uzcard, Humo, Visa)
    SAVINGS,    // Omonat / Yig'im
    DEBT,       // Qarz (Men birovdan qarzdorman yoki birov mendan)
    INVESTMENT  // Investitsiya
}

@Entity(
    tableName = "accounts",
    foreignKeys = [
        ForeignKey(
            entity = CurrencyEntity::class,
            parentColumns = ["code"],
            childColumns = ["currencyCode"],
            onDelete = ForeignKey.RESTRICT // Valyuta o'chib ketsa, hisob buzilmasligi kerak
        )
    ]
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val type: AccountType,

    val initialBalance: Double,

    // --- MULTICURRENCY O'ZGARISHI ---
    // Bu hisob faqat shu valyutada pul saqlaydi
    val currencyCode: String, // "USD" yoki "UZS"

    // UI uchun
    val iconName: String,       // Ikonka nomi (resurs ID emas, string. Masalan: "ic_card")
    val color: String,          // HEX rang kodi (Masalan: "#4FACFE")

    val isArchived: Boolean = false, // O'chirib yubormasdan, arxivlash uchun
    val excludeFromTotal: Boolean = false // Umumiy balansda ko'rsatmaslik uchun
)
