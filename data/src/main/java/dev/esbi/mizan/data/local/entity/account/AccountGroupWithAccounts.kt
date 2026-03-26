package dev.esbi.mizan.data.local.entity.account

import androidx.room.Embedded
import androidx.room.Relation

data class AccountGroupWithAccounts(
    @Embedded val group: AccountGroupEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "groupId"
    )
    val accounts: List<AccountEntity>
)