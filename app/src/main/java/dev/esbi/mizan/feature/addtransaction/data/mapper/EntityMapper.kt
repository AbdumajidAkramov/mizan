package dev.esbi.mizan.feature.addtransaction.data.mapper

import dev.esbi.mizan.data.local.entity.account.AccountEntity
import dev.esbi.mizan.data.local.entity.category.CategoryEntity
import dev.esbi.mizan.feature.addtransaction.domain.model.Account
import dev.esbi.mizan.feature.addtransaction.domain.model.Category

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = "id",
        name = name,
        iconName = iconName,
        type = "type",
        color = color,
        parentId = "parentId"
    )
}

fun AccountEntity.toDomain(): Account {
    return Account(
        id = "id",
        name = name,
        iconName = iconName.orEmpty(),
        currentBalance = 12.90,
        currency = "currency"
    )
}
