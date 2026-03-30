package dev.esbi.mizan.data.local.entity.account

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.esbi.mizan.domain.model.AccountGroup

@Entity(tableName = "account_groups")
data class AccountGroupEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Long,
    override val name: String,        // "Accounts", "Savings", "Dreams"
    override val iconName: String?,   // Guruh uchun umumiy ikonka
    override val orderIndex: Int? = 0,  // Ro'yxatdagi tartibi
    override val type: dev.esbi.mizan.domain.model.AccountGroupType = dev.esbi.mizan.domain.model.AccountGroupType.DEFAULT
) : AccountGroup()
