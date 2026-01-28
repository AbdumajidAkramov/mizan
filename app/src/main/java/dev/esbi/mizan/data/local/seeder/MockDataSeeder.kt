package dev.esbi.mizan.data.local.seeder

import dev.esbi.mizan.data.local.entity.account.AccountEntity
import dev.esbi.mizan.data.local.entity.category.CategoryEntity
import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity
import dev.esbi.mizan.data.local.entity.transaction.TransactionEntity
import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.data.local.dao.AccountGroupDao
import dev.esbi.mizan.data.local.dao.CategoryDao
import dev.esbi.mizan.data.local.dao.CurrencyDao
import dev.esbi.mizan.data.local.dao.TransactionsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockDataSeeder @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val accountDao: AccountDao,
    private val accountGroupDao: AccountGroupDao,
    private val categoryDao: CategoryDao,
    private val transactionsDao: TransactionsDao
) {

    suspend fun seedData() = withContext(Dispatchers.IO) {
        // Only seed if accounts table is empty
        if (accountDao.getAccountCount() > 0) return@withContext

        seedCurrencies()
        seedAccountGroups()
        seedCategories()
        seedAccounts()
        seedTransactions()
    }

    private suspend fun seedCurrencies() {
        val currencies = listOf(
            CurrencyEntity(
                code = "UZS",
                name = "O'zbek so'mi",
                symbol = "so'm",
                rateToBase = 1.0,
                isBaseCurrency = true
            ),
            CurrencyEntity(
                code = "USD",
                name = "US Dollar",
                symbol = "$",
                rateToBase = 12800.0,
                isBaseCurrency = false
            )
        )
        currencies.forEach { currencyDao.insert(it) }
    }

    private suspend fun seedAccountGroups() {
        val accountGroups = listOf(
            dev.esbi.mizan.data.local.entity.account.AccountGroupEntity(
                id = 1,
                name = "Accounts",
                iconName = "ic_accounts",
                orderIndex = 0
            )
        )
        accountGroups.forEach { accountGroupDao.insertGroup(it) }
    }

    private suspend fun seedCategories() {
        val categories = listOf(
            CategoryEntity(
                id = 1,
                name = "Food & Dining",
                type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
                parentId = null,
                iconName = "ic_food",
                color = "#F44336",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 0
            ),
            CategoryEntity(
                id = 2,
                name = "Transport",
                type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
                parentId = null,
                iconName = "ic_transport",
                color = "#2196F3",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 1
            ),
            CategoryEntity(
                id = 3,
                name = "Shopping",
                type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
                parentId = null,
                iconName = "ic_shopping",
                color = "#FF9800",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 2
            ),
            CategoryEntity(
                id = 4,
                name = "Healthcare",
                type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
                parentId = null,
                iconName = "ic_health",
                color = "#4CAF50",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 3
            ),
            CategoryEntity(
                id = 5,
                name = "Salary",
                type = dev.esbi.mizan.domain.model.Transaction.Type.INCOME,
                parentId = null,
                iconName = "ic_salary",
                color = "#4CAF50",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 4
            ),
            CategoryEntity(
                id = 6,
                name = "Freelance",
                type = dev.esbi.mizan.domain.model.Transaction.Type.INCOME,
                parentId = null,
                iconName = "ic_freelance",
                color = "#00BCD4",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 5
            )
        )
        categories.forEach { categoryDao.insertCategory(it) }
    }

    private suspend fun seedAccounts() {
        val accounts = listOf(
            AccountEntity(
                id = 1,
                groupId = 1,
                name = "Naqd pul",
                type = dev.esbi.mizan.domain.model.Account.Type.CASH,
                balance = 5_000_000.0,
                currencyCode = "UZS",
                iconName = "ic_cash",
                color = "#FFC107",
                isArchived = false,
                excludeFromTotal = false,
                description = "Cash at hand"
            ),
            AccountEntity(
                id = 2,
                groupId = 1,
                name = "Visa Gold",
                type = dev.esbi.mizan.domain.model.Account.Type.CARD,
                balance = 1_200.0,
                currencyCode = "USD",
                iconName = "ic_card",
                color = "#9C27B0",
                isArchived = false,
                excludeFromTotal = false,
                description = "Visa Gold Credit Card"
            )
        )
        accounts.forEach { accountDao.insertAccount(it) }
    }

    private suspend fun seedTransactions() {
        val now = System.currentTimeMillis()
        val oneDay = TimeUnit.DAYS.toMillis(1)

        val transactions = listOf(
            TransactionEntity(
                id = 0,
                type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
                amount = 150_000.0,
                currencyCode = "UZS",
                exchangeRate = 1.0,
                targetAmount = null,
                date = now,
                note = "Lunch at cafe",
                description = null,
                photoPaths = emptyList(),
                accountId = 1,
                categoryId = 1,
                targetAccountId = null,
                isBookmarked = false,
                recurrenceRule = null,
                isInstallment = false,
                installmentTotalMonths = null,
                installmentCurrentMonth = null,
                parentTransactionId = null
            ),
            TransactionEntity(
                id = 0,
                type = dev.esbi.mizan.domain.model.Transaction.Type.INCOME,
                amount = 3_000_000.0,
                currencyCode = "UZS",
                exchangeRate = 1.0,
                targetAmount = null,
                date = now - oneDay,
                note = "Monthly salary",
                description = null,
                photoPaths = emptyList(),
                accountId = 1,
                categoryId = 5,
                targetAccountId = null,
                isBookmarked = false,
                recurrenceRule = null,
                isInstallment = false,
                installmentTotalMonths = null,
                installmentCurrentMonth = null,
                parentTransactionId = null
            )
        )
        transactions.forEach { transactionsDao.insertTransaction(it) }
    }
}
