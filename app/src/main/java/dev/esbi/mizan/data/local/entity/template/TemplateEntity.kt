package dev.esbi.mizan.data.local.entity.template

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.esbi.mizan.data.local.entity.category.CategoryEntity
import dev.esbi.mizan.domain.model.Template
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
    @ColumnInfo("id") override val id: Long = 0,
    @ColumnInfo("name") override val name: String,
    @ColumnInfo("amount") override val amount: Double,
    @ColumnInfo("iconName") override val iconName: String? = null,
    @ColumnInfo("transactionType") override val transactionType: Transaction.Type,
    @ColumnInfo("categoryId") override val categoryId: Long? = null
) : Template
