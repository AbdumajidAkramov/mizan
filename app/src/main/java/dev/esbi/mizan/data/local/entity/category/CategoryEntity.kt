package dev.esbi.mizan.data.local.entity.category

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction

@Entity(
    tableName = "categories",
    // Subkategoriyani ota kategoriyaga bog'lash (Otasi o'chsa, bolasi ham o'chadi yoki cascade)
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    // Tezkor qidiruv uchun indekslash
    indices = [Index(value = ["parentId"])]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,

    override val name: String,           // "Oziq-ovqat", "Transport"
    override val type: Transaction.Type,  // EXPENSE yoki INCOME (Avvalgi enumdan foydalanamiz)

    // Subkategoriya mantig'i
    override val parentId: Long? = null, // Null bo'lsa - Main Category

    // UI uchun
    override val iconName: String,       // Masalan: "ic_food", "emoji_apple"
    override val color: String,          // HEX rang kodi ("#FF5722")

    // Budjet (ixtiyoriy, kelajak uchun)
    override val budgetLimit: Double? = null, // Shu kategoriya uchun oylik limit

    override val isArchived: Boolean = false,  // Ishlatilmaydigan kategoriyalar uchun
    override val orderIndex: Int = 0           // Ro'yxatda chiqish tartibi
) : Category