package dev.esbi.mizan.data.local.entity.transaction

import androidx.room.Embedded
import androidx.room.Relation
import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity

data class TransactionWithCurrencyEntity(
    @Embedded val transaction: TransactionEntity,
    @Relation(
        parentColumn = "currencyCode",
        entityColumn = "code"
    )
    val currency: CurrencyEntity
)
