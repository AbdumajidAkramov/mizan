package dev.esbi.mizan.feature.newtransaction.data.mapper

import dev.esbi.mizan.data.local.entity.AccountEntity
import dev.esbi.mizan.data.local.entity.CategoryEntity
import dev.esbi.mizan.feature.addtransaction.domain.model.Account
import dev.esbi.mizan.feature.addtransaction.domain.model.Category

object NewTransactionMapper {
    
    fun mapToDomainCategory(entity: CategoryEntity): Category {
        return Category(
            id = entity.id,
            name = entity.name,
            iconName = entity.iconName,
            type = entity.type,
            color = entity.color,
            parentId = entity.parentId
        )
    }
    
    fun mapToDomainAccount(entity: AccountEntity): Account {
        return Account(
            id = entity.id,
            name = entity.name,
            iconName = entity.iconName,
            currentBalance = entity.currentBalance,
            currency = entity.currency
        )
    }
    
    fun mapToDomainCategories(entities: List<CategoryEntity>): List<Category> {
        return entities.map { mapToDomainCategory(it) }
    }
    
    fun mapToDomainAccounts(entities: List<AccountEntity>): List<Account> {
        return entities.map { mapToDomainAccount(it) }
    }
}
