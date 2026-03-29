package dev.esbi.mizan.data.repository

import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.data.local.dao.AccountGroupDao
import dev.esbi.mizan.data.local.entity.account.AccountEntity
import dev.esbi.mizan.data.local.entity.account.AccountGroupEntity
import dev.esbi.mizan.data.local.mapper.toDomain
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.AccountGroup
import dev.esbi.mizan.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val accountGroupDao: AccountGroupDao
) : AccountRepository {

    override fun observeAccounts(): Flow<List<Account>> {
        return accountDao.getAllAccountsWithCurrency().map { rows ->
            rows.map { it.toDomain() }
        }
    }

    override suspend fun getAccount(id: Long): Account? {
        return accountDao.getAccountWithCurrencyById(id.toString())?.toDomain()
    }

    override suspend fun createAccount(account: Account): Long {
        val entity = AccountEntity(
            id = 0L,
            groupId = account.groupId,
            name = account.name,
            type = account.type,
            balance = account.balance,
            currencyCode = account.currency.code,
            iconName = account.iconName,
            color = account.color,
            isArchived = account.isArchived,
            excludeFromTotal = account.excludeFromTotal,
            description = account.description
        )
        accountDao.insertAccount(entity)
        return entity.id
    }

    override suspend fun updateAccount(account: Account) {
        accountDao.updateAccount(
            id = account.id,
            name = account.name,
            type = account.type.name,
            balance = account.balance,
            iconName = account.iconName,
            color = account.color,
            isArchived = account.isArchived,
            description = account.description
        )
    }

    override suspend fun deleteAccount(id: Long) {
        accountDao.deleteAccount(id)
    }

    override suspend fun updateBalance(id: Long, amount: Double) {
        accountDao.updateBalance(id, amount)
    }

    // --- Account Groups ---

    override fun observeAccountGroups(): Flow<List<AccountGroup>> {
        return accountGroupDao.getAllGroups().map { it }
    }

    override suspend fun saveAccountGroup(group: AccountGroup) {
        val entity = AccountGroupEntity(
            id = group.id,
            name = group.name,
            iconName = group.iconName,
            orderIndex = group.orderIndex,
            type = group.type
        )
        if (group.id == 0L) {
            accountGroupDao.insertGroup(entity)
        } else {
            accountGroupDao.updateGroup(entity)
        }
    }

    override suspend fun deleteAccountGroup(id: Long) {
        accountGroupDao.deleteGroupById(id)
    }
}
