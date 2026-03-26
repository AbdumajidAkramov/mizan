package dev.esbi.mizan.data.local.mapper

import dev.esbi.mizan.data.local.entity.account.AccountWithCurrencyEntity
import dev.esbi.mizan.domain.model.Account

fun AccountWithCurrencyEntity.toDomain(): Account {
    return Account(
        id = account.id,
        groupId = account.groupId,
        name = account.name,
        type = Account.Type.valueOf(account.type.name),
        balance = account.balance,
        currency = currency.toDomain(),
        iconName = account.iconName,
        color = account.color,
        isArchived = account.isArchived,
        excludeFromTotal = account.excludeFromTotal,
        description = account.description
    )
}
