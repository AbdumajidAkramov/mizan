package dev.esbi.mizan.data.local.entity.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity
import dev.esbi.mizan.domain.model.Account

@Entity(
    tableName = "accounts",
    foreignKeys = [
        ForeignKey(
            entity = AccountGroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CurrencyEntity::class,
            parentColumns = ["code"],
            childColumns = ["currencyCode"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") override val id: Long = 0,
    @ColumnInfo("groupId") override val groupId: Long, // <--- MAJBURIY BOG'LANISH
    @ColumnInfo("name") override val name: String,
    @ColumnInfo("type") override val type: Account.Type,
    @ColumnInfo("balance") override val balance: Double,
    @ColumnInfo("currencyCode") override val currencyCode: String, // "USD" yoki "UZS"  // Bu hisob faqat shu valyutada pul saqlaydi
    @ColumnInfo("iconName") override val iconName: String? = null,       // Ikonka nomi (resurs ID emas, string. Masalan: "ic_card")
    @ColumnInfo("color") override val color: String? = null,          // HEX rang kodi (Masalan: "#4FACFE")
    @ColumnInfo("isArchived") override val isArchived: Boolean = false, // O'chirib yubormasdan, arxivlash uchun
    @ColumnInfo("excludeFromTotal") override val excludeFromTotal: Boolean = false, // Umumiy balansda ko'rsatmaslik uchun
    @ColumnInfo("description") override val description: String? = null
) : Account
