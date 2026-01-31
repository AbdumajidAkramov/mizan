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
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

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
            // Expense Categories
            CategoryEntity(
                id = 1,
                name = "Food & Dining",
                type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
                parentId = null,
                iconName = "ic_food",
                color = "#FF6B9D",
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
                color = "#4FACFE",
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
                color = "#FFA34D",
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
                color = "#FF6B6B",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 3
            ),
            CategoryEntity(
                id = 5,
                name = "Bills & Utilities",
                type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
                parentId = null,
                iconName = "ic_bills",
                color = "#00D2FF",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 4
            ),
            CategoryEntity(
                id = 6,
                name = "Entertainment",
                type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
                parentId = null,
                iconName = "ic_entertainment",
                color = "#C471F5",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 5
            ),
            CategoryEntity(
                id = 7,
                name = "Rent",
                type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
                parentId = null,
                iconName = "ic_home",
                color = "#667EEA",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 6
            ),
            CategoryEntity(
                id = 8,
                name = "Groceries",
                type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
                parentId = null,
                iconName = "ic_groceries",
                color = "#00F2A0",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 7
            ),
            // Income Categories
            CategoryEntity(
                id = 10,
                name = "Salary",
                type = dev.esbi.mizan.domain.model.Transaction.Type.INCOME,
                parentId = null,
                iconName = "ic_salary",
                color = "#10B981",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 10
            ),
            CategoryEntity(
                id = 11,
                name = "Freelance",
                type = dev.esbi.mizan.domain.model.Transaction.Type.INCOME,
                parentId = null,
                iconName = "ic_freelance",
                color = "#00BCD4",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 11
            ),
            CategoryEntity(
                id = 12,
                name = "Investment",
                type = dev.esbi.mizan.domain.model.Transaction.Type.INCOME,
                parentId = null,
                iconName = "ic_investment",
                color = "#FFD700",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 12
            ),
            CategoryEntity(
                id = 13,
                name = "Bonus",
                type = dev.esbi.mizan.domain.model.Transaction.Type.INCOME,
                parentId = null,
                iconName = "ic_bonus",
                color = "#8B5CF6",
                budgetLimit = null,
                isArchived = false,
                orderIndex = 13
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
        val transactions = mutableListOf<TransactionEntity>()
        val calendar = Calendar.getInstance()
        
        // Go back 12 months
        calendar.add(Calendar.MONTH, -11)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 10)
        calendar.set(Calendar.MINUTE, 0)

        // Financial Recovery Scenario:
        // Months 1-4: High expenses, low savings (struggling)
        // Months 5-8: Expenses stabilize, income slightly increases
        // Months 9-12: Good savings rate, controlled expenses

        for (monthOffset in 0 until 12) {
            val phase = when (monthOffset) {
                in 0..3 -> FinancialPhase.STRUGGLING
                in 4..7 -> FinancialPhase.STABILIZING
                else -> FinancialPhase.THRIVING
            }

            // Generate monthly salary (1st of month)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            transactions.add(createSalaryTransaction(calendar.timeInMillis, phase))

            // Generate rent (5th of month)
            calendar.set(Calendar.DAY_OF_MONTH, 5)
            transactions.add(createRentTransaction(calendar.timeInMillis, phase))

            // Generate bills (10th of month)
            calendar.set(Calendar.DAY_OF_MONTH, 10)
            transactions.add(createBillsTransaction(calendar.timeInMillis, phase))

            // Generate weekly groceries (4 times per month)
            for (week in 0..3) {
                calendar.set(Calendar.DAY_OF_MONTH, 7 + (week * 7).coerceAtMost(23))
                transactions.add(createGroceriesTransaction(calendar.timeInMillis, phase))
            }

            // Generate daily food transactions (15-25 per month)
            val foodDays = (15..25).random()
            val usedDays = mutableSetOf<Int>()
            repeat(foodDays) {
                var day = (1..28).random()
                while (usedDays.contains(day)) day = (1..28).random()
                usedDays.add(day)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                transactions.add(createFoodTransaction(calendar.timeInMillis, phase))
            }

            // Generate transport transactions (8-15 per month)
            val transportDays = (8..15).random()
            repeat(transportDays) {
                calendar.set(Calendar.DAY_OF_MONTH, (1..28).random())
                transactions.add(createTransportTransaction(calendar.timeInMillis, phase))
            }

            // Generate shopping (2-5 per month, more in struggling phase)
            val shoppingCount = when (phase) {
                FinancialPhase.STRUGGLING -> (4..7).random()
                FinancialPhase.STABILIZING -> (2..4).random()
                FinancialPhase.THRIVING -> (1..3).random()
            }
            repeat(shoppingCount) {
                calendar.set(Calendar.DAY_OF_MONTH, (1..28).random())
                transactions.add(createShoppingTransaction(calendar.timeInMillis, phase))
            }

            // Generate entertainment (1-4 per month)
            val entertainmentCount = when (phase) {
                FinancialPhase.STRUGGLING -> (3..5).random()
                FinancialPhase.STABILIZING -> (2..3).random()
                FinancialPhase.THRIVING -> (1..2).random()
            }
            repeat(entertainmentCount) {
                calendar.set(Calendar.DAY_OF_MONTH, (1..28).random())
                transactions.add(createEntertainmentTransaction(calendar.timeInMillis, phase))
            }

            // Generate healthcare (0-2 per month)
            if (Random.nextFloat() < 0.4f) {
                calendar.set(Calendar.DAY_OF_MONTH, (1..28).random())
                transactions.add(createHealthcareTransaction(calendar.timeInMillis))
            }

            // Generate freelance income (more in later phases)
            val freelanceChance = when (phase) {
                FinancialPhase.STRUGGLING -> 0.2f
                FinancialPhase.STABILIZING -> 0.5f
                FinancialPhase.THRIVING -> 0.7f
            }
            if (Random.nextFloat() < freelanceChance) {
                calendar.set(Calendar.DAY_OF_MONTH, (15..25).random())
                transactions.add(createFreelanceTransaction(calendar.timeInMillis, phase))
            }

            // Generate bonus (occasional, more in thriving phase)
            if (phase == FinancialPhase.THRIVING && Random.nextFloat() < 0.3f) {
                calendar.set(Calendar.DAY_OF_MONTH, (20..28).random())
                transactions.add(createBonusTransaction(calendar.timeInMillis))
            }

            // Move to next month
            calendar.add(Calendar.MONTH, 1)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
        }

        // Shuffle and insert all transactions
        transactions.shuffled().forEach { transactionsDao.insertTransaction(it) }
    }

    private enum class FinancialPhase {
        STRUGGLING, STABILIZING, THRIVING
    }

    // ========== INCOME TRANSACTIONS ==========

    private fun createSalaryTransaction(date: Long, phase: FinancialPhase): TransactionEntity {
        val amount = when (phase) {
            FinancialPhase.STRUGGLING -> (2_800_000..3_200_000).random().toDouble()
            FinancialPhase.STABILIZING -> (3_200_000..3_600_000).random().toDouble()
            FinancialPhase.THRIVING -> (3_600_000..4_200_000).random().toDouble()
        }
        val notes = listOf("Monthly salary", "Salary payment", "Work salary", "Paycheck")
        return createTransaction(
            type = dev.esbi.mizan.domain.model.Transaction.Type.INCOME,
            amount = amount,
            date = date,
            note = notes.random(),
            categoryId = 10,
            accountId = 1
        )
    }

    private fun createFreelanceTransaction(date: Long, phase: FinancialPhase): TransactionEntity {
        val amount = when (phase) {
            FinancialPhase.STRUGGLING -> (200_000..500_000).random().toDouble()
            FinancialPhase.STABILIZING -> (400_000..800_000).random().toDouble()
            FinancialPhase.THRIVING -> (600_000..1_500_000).random().toDouble()
        }
        val notes = listOf(
            "Freelance project", "Design work", "Website project",
            "Logo design", "App development", "Consulting fee",
            "Project payment", "Client work"
        )
        return createTransaction(
            type = dev.esbi.mizan.domain.model.Transaction.Type.INCOME,
            amount = amount,
            date = date,
            note = notes.random(),
            categoryId = 11,
            accountId = if (Random.nextBoolean()) 1 else 2
        )
    }

    private fun createBonusTransaction(date: Long): TransactionEntity {
        val amount = (500_000..1_500_000).random().toDouble()
        val notes = listOf("Performance bonus", "Year-end bonus", "Project bonus", "Quarterly bonus")
        return createTransaction(
            type = dev.esbi.mizan.domain.model.Transaction.Type.INCOME,
            amount = amount,
            date = date,
            note = notes.random(),
            categoryId = 13,
            accountId = 1
        )
    }

    // ========== EXPENSE TRANSACTIONS ==========

    private fun createRentTransaction(date: Long, phase: FinancialPhase): TransactionEntity {
        val amount = when (phase) {
            FinancialPhase.STRUGGLING -> 1_200_000.0
            FinancialPhase.STABILIZING -> 1_200_000.0
            FinancialPhase.THRIVING -> 1_000_000.0 // Moved to cheaper place
        }
        return createTransaction(
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount,
            date = date,
            note = "Monthly rent",
            categoryId = 7,
            accountId = 1
        )
    }

    private fun createBillsTransaction(date: Long, phase: FinancialPhase): TransactionEntity {
        val baseAmount = when (phase) {
            FinancialPhase.STRUGGLING -> (350_000..500_000).random()
            FinancialPhase.STABILIZING -> (280_000..400_000).random()
            FinancialPhase.THRIVING -> (200_000..320_000).random()
        }
        val notes = listOf(
            "Electric bill", "Internet bill", "Gas bill",
            "Water bill", "Utilities", "Phone bill"
        )
        return createTransaction(
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = baseAmount.toDouble(),
            date = date,
            note = notes.random(),
            categoryId = 5,
            accountId = 1
        )
    }

    private fun createGroceriesTransaction(date: Long, phase: FinancialPhase): TransactionEntity {
        val amount = when (phase) {
            FinancialPhase.STRUGGLING -> (250_000..400_000).random()
            FinancialPhase.STABILIZING -> (200_000..350_000).random()
            FinancialPhase.THRIVING -> (180_000..300_000).random()
        }
        val notes = listOf(
            "Weekly groceries", "Makro shopping", "Korzinka",
            "Grocery shopping", "Food supplies", "Supermarket"
        )
        return createTransaction(
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount.toDouble(),
            date = date,
            note = notes.random(),
            categoryId = 8,
            accountId = 1
        )
    }

    private fun createFoodTransaction(date: Long, phase: FinancialPhase): TransactionEntity {
        val amount = when (phase) {
            FinancialPhase.STRUGGLING -> (40_000..120_000).random()
            FinancialPhase.STABILIZING -> (35_000..90_000).random()
            FinancialPhase.THRIVING -> (30_000..75_000).random()
        }
        val notes = listOf(
            "Lunch at cafe", "Coffee break", "Dinner out",
            "Lunch with Ali", "Pizza night", "Restaurant",
            "Fast food", "Breakfast", "Snacks",
            "Coffee with friends", "Team lunch", "Quick bite",
            "Sushi dinner", "Lunch meeting", ""
        )
        return createTransaction(
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount.toDouble(),
            date = date,
            note = notes.random(),
            categoryId = 1,
            accountId = if (Random.nextFloat() < 0.7f) 1 else 2
        )
    }

    private fun createTransportTransaction(date: Long, phase: FinancialPhase): TransactionEntity {
        val amount = when (phase) {
            FinancialPhase.STRUGGLING -> (15_000..80_000).random()
            FinancialPhase.STABILIZING -> (10_000..60_000).random()
            FinancialPhase.THRIVING -> (8_000..45_000).random()
        }
        val notes = listOf(
            "Taxi to work", "Yandex taxi", "Metro",
            "Bus fare", "Uber ride", "Taxi home",
            "Petrol", "Gas station", "Parking",
            "Taxi to meeting", "", "Transport"
        )
        return createTransaction(
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount.toDouble(),
            date = date,
            note = notes.random(),
            categoryId = 2,
            accountId = 1
        )
    }

    private fun createShoppingTransaction(date: Long, phase: FinancialPhase): TransactionEntity {
        val amount = when (phase) {
            FinancialPhase.STRUGGLING -> (150_000..600_000).random()
            FinancialPhase.STABILIZING -> (100_000..400_000).random()
            FinancialPhase.THRIVING -> (80_000..300_000).random()
        }
        val notes = listOf(
            "New clothes", "Shoes", "Electronics",
            "Home decor", "Amazon order", "Online shopping",
            "Gift for friend", "Household items", "",
            "Tech gadget", "Book purchase", "Accessories"
        )
        return createTransaction(
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount.toDouble(),
            date = date,
            note = notes.random(),
            categoryId = 3,
            accountId = if (Random.nextFloat() < 0.5f) 1 else 2
        )
    }

    private fun createEntertainmentTransaction(date: Long, phase: FinancialPhase): TransactionEntity {
        val amount = when (phase) {
            FinancialPhase.STRUGGLING -> (80_000..250_000).random()
            FinancialPhase.STABILIZING -> (60_000..180_000).random()
            FinancialPhase.THRIVING -> (50_000..150_000).random()
        }
        val notes = listOf(
            "Movie night", "Netflix subscription", "Spotify",
            "Concert tickets", "Game purchase", "Bowling",
            "Streaming service", "Gaming", "",
            "Night out", "Party", "Weekend fun"
        )
        return createTransaction(
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount.toDouble(),
            date = date,
            note = notes.random(),
            categoryId = 6,
            accountId = if (Random.nextFloat() < 0.6f) 1 else 2
        )
    }

    private fun createHealthcareTransaction(date: Long): TransactionEntity {
        val amount = (100_000..500_000).random().toDouble()
        val notes = listOf(
            "Doctor visit", "Pharmacy", "Medicine",
            "Dentist", "Health checkup", "Vitamins",
            "Medical supplies", ""
        )
        return createTransaction(
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount,
            date = date,
            note = notes.random(),
            categoryId = 4,
            accountId = 1
        )
    }

    // ========== HELPER ==========

    private fun createTransaction(
        type: dev.esbi.mizan.domain.model.Transaction.Type,
        amount: Double,
        date: Long,
        note: String,
        categoryId: Long,
        accountId: Long
    ): TransactionEntity {
        // Add some time randomness to the date
        val randomizedDate = date + (0..TimeUnit.HOURS.toMillis(12)).random()
        
        return TransactionEntity(
            id = 0,
            type = type,
            amount = amount,
            currencyCode = "UZS",
            exchangeRate = 1.0,
            targetAmount = null,
            date = randomizedDate,
            note = note.ifEmpty { null },
            description = null,
            photoPaths = emptyList(),
            accountId = accountId,
            categoryId = categoryId,
            targetAccountId = null,
            isBookmarked = Random.nextFloat() < 0.05f,
            recurrenceRule = null,
            isInstallment = false,
            installmentTotalMonths = null,
            installmentCurrentMonth = null,
            parentTransactionId = null
        )
    }
}
