package dev.esbi.mizan.data.local.seeder

import android.util.Log
import dev.esbi.mizan.data.local.entity.account.AccountEntity
import dev.esbi.mizan.data.local.entity.category.CategoryEntity
import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity
import dev.esbi.mizan.data.local.entity.transaction.TransactionEntity
import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.data.local.dao.AccountGroupDao
import dev.esbi.mizan.data.local.dao.CategoryDao
import dev.esbi.mizan.data.local.dao.CurrencyDao
import dev.esbi.mizan.data.local.dao.SubCurrencyDao
import dev.esbi.mizan.data.local.dao.TransactionsDao
import dev.esbi.mizan.data.local.entity.currency.SubCurrencyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class MockDataSeeder @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val subCurrencyDao: SubCurrencyDao,
    private val accountDao: AccountDao,
    private val accountGroupDao: AccountGroupDao,
    private val categoryDao: CategoryDao,
    private val transactionsDao: TransactionsDao
) {

    suspend fun seedData() = withContext(Dispatchers.IO) {
        // Essential core data check (currencies and groups)
        if (accountGroupDao.getGroupCount() > 0) return@withContext

        Log.d(TAG, "Starting database seeding...")
        seedCurrencies()
        seedSubCurrencies()
        seedAccountGroups()
        seedCategories()
        seedAccounts()
//        seedHighDensityTransactions()
        Log.d(TAG, "Database seeding complete!")
    }

    companion object {
        private const val TAG = "MockDataSeeder"
        
        // Category IDs
        const val CAT_FOOD = 1L
        const val CAT_TRANSPORT = 2L
        const val CAT_SHOPPING = 3L
        const val CAT_HEALTHCARE = 4L
        const val CAT_BILLS = 5L
        const val CAT_ENTERTAINMENT = 6L
        const val CAT_RENT = 7L
        const val CAT_GROCERIES = 8L
        const val CAT_SALARY = 10L
        const val CAT_FREELANCE = 11L
        const val CAT_INVESTMENT = 12L
        const val CAT_BONUS = 13L

        // Account IDs
        const val ACC_CASH = 1L
        const val ACC_CARD = 2L
        const val ACC_SAVINGS = 3L

        // System Group IDs
        const val GROUP_GENERAL = 1L
        const val GROUP_CASH = 2L
        const val GROUP_CREDIT_CARD = 3L
    }

    private suspend fun seedCurrencies() {
        val currencies = listOf(
            CurrencyEntity(
                code = "UZS",
                name = "O'zbek so'mi",
                symbol = "so'm",
                rateToBase = BigDecimal.ONE,
                isBaseCurrency = true
            ),
            CurrencyEntity(
                code = "USD",
                name = "US Dollar",
                symbol = "$",
                rateToBase = 12800.0.toBigDecimal(),
                isBaseCurrency = false
            )
        )
        currencies.forEach { currencyDao.insert(it) }
    }

    private suspend fun seedSubCurrencies() {
        if (subCurrencyDao.getCount() > 0) return
        val subCurrencies = listOf(
            SubCurrencyEntity(
                code = "UZS",
                name = "O'zbek so'mi",
                symbol = "so'm",
                exchangeRate = "1",
                unitPosition = "END",
                decimalDigits = 0,
                orderIndex = 0,
                isMainCurrency = true,
                isUserDefined = false
            ),
            SubCurrencyEntity(
                code = "USD",
                name = "US Dollar",
                symbol = "$",
                exchangeRate = "12800",
                unitPosition = "FRONT",
                decimalDigits = 2,
                orderIndex = 1,
                isMainCurrency = false,
                isUserDefined = false
            ),
            SubCurrencyEntity(
                code = "EUR",
                name = "Euro",
                symbol = "€",
                exchangeRate = "13800",
                unitPosition = "FRONT",
                decimalDigits = 2,
                orderIndex = 2,
                isMainCurrency = false,
                isUserDefined = false
            ),
            SubCurrencyEntity(
                code = "RUB",
                name = "Russian Ruble",
                symbol = "₽",
                exchangeRate = "135",
                unitPosition = "END",
                decimalDigits = 2,
                orderIndex = 3,
                isMainCurrency = false,
                isUserDefined = false
            ),
            SubCurrencyEntity(
                code = "XAU",
                name = "Gold (Troy Ounce)",
                symbol = "Au",
                exchangeRate = "33000000",
                unitPosition = "END",
                decimalDigits = 4,
                orderIndex = 4,
                isMainCurrency = false,
                isUserDefined = true
            )
        )
        subCurrencyDao.insertAll(subCurrencies)
    }

    private suspend fun seedAccountGroups() {
        val accountGroups = listOf(
            dev.esbi.mizan.data.local.entity.account.AccountGroupEntity(
                id = GROUP_GENERAL,
                name = "General",
                iconName = "ic_accounts",
                orderIndex = 0,
                type = dev.esbi.mizan.domain.model.AccountGroupType.DEFAULT,
                isSystemGroup = true
            ),
            dev.esbi.mizan.data.local.entity.account.AccountGroupEntity(
                id = GROUP_CASH,
                name = "Cash",
                iconName = "ic_cash",
                orderIndex = 1,
                type = dev.esbi.mizan.domain.model.AccountGroupType.DEFAULT,
                isSystemGroup = true
            ),
            dev.esbi.mizan.data.local.entity.account.AccountGroupEntity(
                id = GROUP_CREDIT_CARD,
                name = "Credit card",
                iconName = "ic_credit_card",
                orderIndex = 2,
                type = dev.esbi.mizan.domain.model.AccountGroupType.CREDIT_CARD,
                isSystemGroup = true
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
                id = 100,
                name = "Eating out",
                type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
                parentId = 1,
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
                parentId = 1,
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
                id = ACC_CASH,
                groupId = 1,
                name = "Cash Wallet",
                balance = BigDecimal(2_500_000.0),
                currencyCode = "UZS",
                isArchived = false,
                excludeFromTotal = false,
                description = "Cash at hand"
            ),
            AccountEntity(
                id = ACC_CARD,
                groupId = 1,
                name = "Visa Gold",
                balance = BigDecimal(8_500_000.0),
                currencyCode = "UZS",
                isArchived = false,
                excludeFromTotal = false,
                description = "Visa Gold Debit Card"
            ),
            AccountEntity(
                id = ACC_SAVINGS,
                groupId = 1,
                name = "Savings",
                balance = BigDecimal(15_000_000.0),
                currencyCode = "UZS",
                isArchived = false,
                excludeFromTotal = false,
                description = "Emergency fund"
            )
        )
        accounts.forEach { accountDao.insertAccount(it) }
    }

    /**
     * High-density transaction generation for 3 years of data
     * Generates ~5,000+ transactions with realistic patterns
     */
    private suspend fun seedHighDensityTransactions() {
        val transactions = mutableListOf<TransactionEntity>()
        
        val endDate = LocalDate.now()
        val startDate = endDate.minusYears(3)
        
        var currentDate = startDate
        var transactionId = 1L

        Log.d(TAG, "Generating transactions from $startDate to $endDate...")

        while (!currentDate.isAfter(endDate)) {
            val dayOfMonth = currentDate.dayOfMonth
            val dayOfWeek = currentDate.dayOfWeek

            // ============ DAILY EXPENSES (Every day) ============
            val dailyExpenseCount = (1..3).random()
            repeat(dailyExpenseCount) {
                transactions.add(
                    createDailyExpense(currentDate, transactionId++)
                )
            }

            // ============ WEEKLY GROCERIES (Every Sunday) ============
            if (dayOfWeek == DayOfWeek.SUNDAY) {
                transactions.add(
                    createWeeklyGroceries(currentDate, transactionId++)
                )
            }

            // ============ MONTHLY OBLIGATIONS (1st of month) ============
            if (dayOfMonth == 1) {
                // Rent
                transactions.add(
                    createMonthlyRent(currentDate, transactionId++)
                )
                // Utilities
                transactions.add(
                    createMonthlyUtilities(currentDate, transactionId++)
                )
            }

            // ============ INCOME STREAMS (7th, 14th, 21st, 28th) ============
            if (dayOfMonth in listOf(7, 14, 21, 28)) {
                transactions.add(
                    createWeeklyIncome(currentDate, transactionId++)
                )
            }

            // ============ OCCASIONAL EXPENSES ============
            // Shopping (2-3 times per week on weekends)
            if (dayOfWeek in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY) && Random.nextFloat() < 0.4f) {
                transactions.add(
                    createShoppingExpense(currentDate, transactionId++)
                )
            }

            // Entertainment (Friday/Saturday evenings)
            if (dayOfWeek in listOf(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY) && Random.nextFloat() < 0.3f) {
                transactions.add(
                    createEntertainmentExpense(currentDate, transactionId++)
                )
            }

            // Healthcare (random, ~2-3 times per month)
            if (Random.nextFloat() < 0.08f) {
                transactions.add(
                    createHealthcareExpense(currentDate, transactionId++)
                )
            }

            // Freelance income (random, ~2-4 times per month)
            if (Random.nextFloat() < 0.1f) {
                transactions.add(
                    createFreelanceIncome(currentDate, transactionId++)
                )
            }

            currentDate = currentDate.plusDays(1)
        }

        Log.d(TAG, "Generated ${transactions.size} transactions. Inserting in batch...")
        
        // Batch insert for performance
        transactionsDao.insertTransactions(transactions)
        
        Log.d(TAG, "Batch insert complete!")
    }

    // ============ DAILY EXPENSE GENERATORS ============

    private fun createDailyExpense(date: LocalDate, id: Long): TransactionEntity {
        val isFood = Random.nextFloat() < 0.7f
        
        return if (isFood) {
            createFoodExpense(date, id)
        } else {
            createTransportExpense(date, id)
        }
    }

    private fun createFoodExpense(date: LocalDate, id: Long): TransactionEntity {
        // Amount: 25,000 - 250,000 UZS (~$2-$20)
        val amount = (25_000..250_000).random().toDouble()
        
        val restaurants = listOf("Osmos", "Rayhon", "Milliy Taomlar", "Evos", "KFC", "Burger House", "Sushi Master", "Doner Kebab")
        val foodTypes = listOf("Lunch", "Coffee", "Breakfast", "Dinner", "Snack")
        val companions = listOf("", " with Ali", " with Sardor", " with colleagues", " solo", " with team")
        
        val note = "${foodTypes.random()} at ${restaurants.random()}${companions.random()}"
        
        return createTransactionEntity(
            id = id,
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount.toBigDecimal(),
            date = date,
            hour = (8..20).random(),
            note = note,
            categoryId = CAT_FOOD,
            accountId = if (Random.nextFloat() < 0.6f) ACC_CASH else ACC_CARD
        )
    }

    private fun createTransportExpense(date: LocalDate, id: Long): TransactionEntity {
        // Amount: 10,000 - 100,000 UZS (~$1-$8)
        val amount = (10_000..100_000).random().toDouble()
        
        val transportTypes = listOf(
            "Yandex taxi", "Uber ride", "Metro", "Bus fare",
            "Taxi to work", "Taxi home", "Taxi to meeting",
            "Petrol", "Parking fee"
        )
        val destinations = listOf("", " to office", " to mall", " to meeting", " downtown", " to airport")
        
        val note = "${transportTypes.random()}${destinations.random()}"
        
        return createTransactionEntity(
            id = id,
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount.toBigDecimal(),
            date = date,
            hour = (7..22).random(),
            note = note,
            categoryId = CAT_TRANSPORT,
            accountId = ACC_CASH
        )
    }

    // ============ WEEKLY EXPENSE GENERATORS ============

    private fun createWeeklyGroceries(date: LocalDate, id: Long): TransactionEntity {
        // Amount: 1,000,000 - 2,500,000 UZS (~$80-$200)
        val amount = (1_000_000..2_500_000).random().toDouble()
        
        val stores = listOf("Makro", "Korzinka", "Havas", "Carrefour", "Mega Planet")
        val note = "Weekly groceries at ${stores.random()}"
        
        return createTransactionEntity(
            id = id,
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount.toBigDecimal(),
            date = date,
            hour = (10..18).random(),
            note = note,
            categoryId = CAT_GROCERIES,
            accountId = ACC_CARD
        )
    }

    // ============ MONTHLY EXPENSE GENERATORS ============

    private fun createMonthlyRent(date: LocalDate, id: Long): TransactionEntity {
        // Fixed rent: ~15,000,000 UZS (~$1,200)
        val amount = (14_500_000..15_500_000).random().toDouble()
        
        return createTransactionEntity(
            id = id,
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount.toBigDecimal(),
            date = date,
            hour = 10,
            note = "Monthly rent payment",
            categoryId = CAT_RENT,
            accountId = ACC_CARD
        )
    }

    private fun createMonthlyUtilities(date: LocalDate, id: Long): TransactionEntity {
        // Utilities: 1,250,000 - 1,875,000 UZS (~$100-$150)
        val amount = (1_250_000..1_875_000).random().toDouble()
        
        val utilityTypes = listOf(
            "Electric bill", "Gas bill", "Water bill",
            "Internet bill", "Phone bill", "Utilities combined"
        )
        
        return createTransactionEntity(
            id = id,
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount.toBigDecimal(),
            date = date,
            hour = 11,
            note = utilityTypes.random(),
            categoryId = CAT_BILLS,
            accountId = ACC_CARD
        )
    }

    // ============ INCOME GENERATORS ============

    private fun createWeeklyIncome(date: LocalDate, id: Long): TransactionEntity {
        // Weekly salary portion: 6,250,000 - 10,000,000 UZS (~$500-$800)
        val amount = (6_250_000..10_000_000).random().toDouble()
        
        val notes = listOf(
            "Weekly salary", "Salary payment", "Paycheck",
            "Work income", "Company transfer"
        )
        
        return createTransactionEntity(
            id = id,
            type = dev.esbi.mizan.domain.model.Transaction.Type.INCOME,
            amount = amount.toBigDecimal(),
            date = date,
            hour = 14,
            note = notes.random(),
            categoryId = CAT_SALARY,
            accountId = ACC_CARD
        )
    }

    private fun createFreelanceIncome(date: LocalDate, id: Long): TransactionEntity {
        // Freelance: 1,250,000 - 6,250,000 UZS (~$100-$500)
        val amount = (1_250_000..6_250_000).random().toDouble()
        
        val projectTypes = listOf(
            "Website project", "App development", "Design work",
            "Logo design", "Consulting fee", "Translation work",
            "Video editing", "Content writing", "SEO project"
        )
        val clients = listOf("", " for TechCorp", " for StartupXYZ", " for local client", " for overseas client")
        
        val note = "${projectTypes.random()}${clients.random()}"
        
        return createTransactionEntity(
            id = id,
            type = dev.esbi.mizan.domain.model.Transaction.Type.INCOME,
            amount = amount.toBigDecimal(),
            date = date,
            hour = (10..18).random(),
            note = note,
            categoryId = CAT_FREELANCE,
            accountId = if (Random.nextBoolean()) ACC_CARD else ACC_CASH
        )
    }

    // ============ OCCASIONAL EXPENSE GENERATORS ============

    private fun createShoppingExpense(date: LocalDate, id: Long): TransactionEntity {
        // Shopping: 250,000 - 2,500,000 UZS (~$20-$200)
        val amount = (250_000..2_500_000).random().toDouble()
        
        val items = listOf(
            "New clothes", "Shoes", "Electronics", "Home decor",
            "Kitchen items", "Books", "Accessories", "Tech gadget",
            "Gift for friend", "Household items", "Sports gear"
        )
        val stores = listOf("", " at Samarkand Darvoza", " at Next", " at Mega Planet", " online")
        
        val note = "${items.random()}${stores.random()}"
        
        return createTransactionEntity(
            id = id,
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount.toBigDecimal(),
            date = date,
            hour = (11..20).random(),
            note = note,
            categoryId = CAT_SHOPPING,
            accountId = ACC_CARD
        )
    }

    private fun createEntertainmentExpense(date: LocalDate, id: Long): TransactionEntity {
        // Entertainment: 125,000 - 625,000 UZS (~$10-$50)
        val amount = (125_000..625_000).random().toDouble()
        
        val activities = listOf(
            "Movie night", "Bowling", "Concert tickets", "Comedy show",
            "Night out", "Karaoke", "Game center", "Escape room",
            "Billiards", "Netflix subscription", "Spotify"
        )
        val companions = listOf("", " with friends", " with family", " solo", " date night")
        
        val note = "${activities.random()}${companions.random()}"
        
        return createTransactionEntity(
            id = id,
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount.toBigDecimal(),
            date = date,
            hour = (18..23).random(),
            note = note,
            categoryId = CAT_ENTERTAINMENT,
            accountId = if (Random.nextFloat() < 0.5f) ACC_CASH else ACC_CARD
        )
    }

    private fun createHealthcareExpense(date: LocalDate, id: Long): TransactionEntity {
        // Healthcare: 125,000 - 1,250,000 UZS (~$10-$100)
        val amount = (125_000..1_250_000).random().toDouble()
        
        val types = listOf(
            "Doctor visit", "Pharmacy", "Medicine",
            "Dentist", "Health checkup", "Vitamins",
            "Eye exam", "Lab tests", "Physiotherapy"
        )
        val clinics = listOf("", " at Akfa Medline", " at Premium Clinic", " at local pharmacy")
        
        val note = "${types.random()}${clinics.random()}"
        
        return createTransactionEntity(
            id = id,
            type = dev.esbi.mizan.domain.model.Transaction.Type.EXPENSE,
            amount = amount.toBigDecimal(),
            date = date,
            hour = (9..17).random(),
            note = note,
            categoryId = CAT_HEALTHCARE,
            accountId = ACC_CARD
        )
    }

    // ============ HELPER ============

    private fun createTransactionEntity(
        id: Long,
        type: dev.esbi.mizan.domain.model.Transaction.Type,
        amount: BigDecimal,
        date: LocalDate,
        hour: Int,
        note: String,
        categoryId: Long,
        accountId: Long
    ): TransactionEntity {
        val dateTime = LocalDateTime.of(
            date.year, date.month, date.dayOfMonth,
            hour, (0..59).random(), (0..59).random()
        )
        val epochMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        
        return TransactionEntity(
            id = 0, // Auto-generate
            type = type,
            amount = amount,
            currencyCode = "UZS",
            exchangeRate = 1.0,
            targetAmount = null,
            date = epochMillis,
            note = note.trim().ifEmpty { null },
            description = null,
            photoPaths = emptyList(),
            accountId = accountId,
            categoryId = categoryId,
            targetAccountId = null,
            isBookmarked = Random.nextFloat() < 0.02f,
            recurrenceRule = null,
            isInstallment = false,
            installmentTotalMonths = null,
            installmentCurrentMonth = null,
            parentTransactionId = null
        )
    }
}
