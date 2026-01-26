package dev.esbi.mizan.data.local.entity.category

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType

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
    val id: Long = 0,

    val name: String,           // "Oziq-ovqat", "Transport"
    val type: TransactionType,  // EXPENSE yoki INCOME (Avvalgi enumdan foydalanamiz)
    
    // Subkategoriya mantig'i
    val parentId: Long? = null, // Null bo'lsa - Main Category
    
    // UI uchun
    val iconName: String,       // Masalan: "ic_food", "emoji_apple"
    val color: String,          // HEX rang kodi ("#FF5722")
    
    // Budjet (ixtiyoriy, kelajak uchun)
    val budgetLimit: Double? = null, // Shu kategoriya uchun oylik limit

    val isArchived: Boolean = false,  // Ishlatilmaydigan kategoriyalar uchun
    val orderIndex: Int = 0           // Ro'yxatda chiqish tartibi
)