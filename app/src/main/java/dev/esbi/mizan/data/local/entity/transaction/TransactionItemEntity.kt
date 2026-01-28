package dev.esbi.mizan.data.local.entity.transaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaction_items",
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["id"],
            childColumns = ["transactionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransactionItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "transactionId")
    val transactionId: Long,         // transactions.id bilan bog'lanadi

    @ColumnInfo(name = "name")
    val name: String,                // Mahsulot nomi (masalan: "Choy")

    @ColumnInfo(name = "mxikCode")
    val mxikCode: String? = null,    // Soliq tizimidagi MXIK (IKPU) kodi

    @ColumnInfo(name = "quantity")
    val quantity: Double = 1.0,      // Miqdori (masalan: 2.5 kg yoki 1 ta)

    @ColumnInfo(name = "price")
    val price: Double,               // Bir birlik uchun narxi

    @ColumnInfo(name = "totalPrice")
    val totalPrice: Double,          // Jami narxi (quantity * price)

    @ColumnInfo(name = "unitName")
    val unitName: String? = null,    // O'lchov birligi (dona, kg, litr)

    @ColumnInfo(name = "vatAmount")
    val vatAmount: Double = 0.0      // Ushbu mahsulot uchun hisoblangan QQS (NDS)
)
