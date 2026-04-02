package dev.esbi.mizan.data.local.entity.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity

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
    @ColumnInfo("id") val id: Long = 0,
    @ColumnInfo("groupId") val groupId: Long,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("type") val type: String = "CASH",
    @ColumnInfo("balance") val balance: Double,
    @ColumnInfo("currencyCode") val currencyCode: String,
    @ColumnInfo("iconName") val iconName: String? = null,
    @ColumnInfo("color") val color: String? = null,
    @ColumnInfo("isArchived") val isArchived: Boolean = false,
    @ColumnInfo("excludeFromTotal") val excludeFromTotal: Boolean = false,
    @ColumnInfo("description") val description: String? = null,
    @ColumnInfo("is_deleted") val isDeleted: Boolean = false
)
