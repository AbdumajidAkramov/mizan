package dev.esbi.mizan.data.local.entity.template

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.esbi.mizan.data.local.entity.category.CategoryEntity
import dev.esbi.mizan.domain.model.Transaction

@Entity(
    tableName = "templates",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("categoryId"), Index("transactionType")]
)
data class TemplateEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Long = 0,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("amount") val amount: Double,
    @ColumnInfo("iconName") val iconName: String? = null,
    @ColumnInfo("transactionType") val transactionType: Transaction.Type,
    @ColumnInfo("categoryId") val categoryId: Long? = null,
    @ColumnInfo("accountId") val accountId: Long? = null,
    @ColumnInfo("note") val note: String? = null
)
