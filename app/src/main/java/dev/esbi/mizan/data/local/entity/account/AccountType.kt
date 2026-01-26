package dev.esbi.mizan.data.local.entity.account

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity
import dev.esbi.mizan.domain.model.Account


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
    override val id: Long = 0,

    override val name: String,
    override val type: Account.Type,

    override val initialBalance: Double,

    // --- MULTICURRENCY O'ZGARISHI ---
    // Bu hisob faqat shu valyutada pul saqlaydi
    override val currencyCode: String, // "USD" yoki "UZS"

    // UI uchun
    override val iconName: String,       // Ikonka nomi (resurs ID emas, string. Masalan: "ic_card")
    override val color: String,          // HEX rang kodi (Masalan: "#4FACFE")

    override val isArchived: Boolean = false, // O'chirib yubormasdan, arxivlash uchun
    override val excludeFromTotal: Boolean = false // Umumiy balansda ko'rsatmaslik uchun
) : Account
