package dev.esbi.mizan.feature.transactions.data.mapper

import dev.esbi.mizan.data.local.entity.TransactionDetailEntity
import dev.esbi.mizan.feature.transactions.domain.model.Transaction
import dev.esbi.mizan.feature.transactions.domain.model.TransactionType

fun TransactionDetailEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        title = title,
        amount = amount,
        type = when (type.lowercase()) {
            "income" -> TransactionType.INCOME
            else -> TransactionType.EXPENSE
        },
        category = category,
        categoryName = categoryName,
        timestamp = timestamp,
        description = description
    )
}

fun Transaction.toEntity(): TransactionDetailEntity {
    return TransactionDetailEntity(
        id = id,
        title = title,
        amount = amount,
        type = type.name.lowercase(),
        category = category,
        categoryName = categoryName,
        timestamp = timestamp,
        description = description
    )
}
