package dev.esbi.mizan.data.local.entity.account

import androidx.room.Embedded
import androidx.room.Relation
import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity

data class AccountWithCurrencyEntity(
    @Embedded val account: AccountEntity,
    @Relation(
        parentColumn = "currencyCode",
        entityColumn = "code"
    )
    val currency: CurrencyEntity
)
