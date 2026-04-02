package dev.esbi.mizan.data.local.mapper

import dev.esbi.mizan.data.local.entity.account.AccountWithCurrencyEntity
import dev.esbi.mizan.domain.model.Account

fun AccountWithCurrencyEntity.toDomain(): Account {
    return Account(
        id = account.id,
        groupId = account.groupId,
        name = account.name,
        balance = account.balance,
        currency = currency.toDomain(),
        isArchived = account.isArchived,
        excludeFromTotal = account.excludeFromTotal,
        description = account.description,
        isDeleted = account.isDeleted
    )
}
