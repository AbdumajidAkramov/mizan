package dev.esbi.mizan.data.local

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.esbi.mizan.data.local.entity.AccountEntity
import dev.esbi.mizan.data.local.entity.CategoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class MizanDatabaseCallback : RoomDatabase.Callback() {
    
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Pre-populate the database with default data
        // Note: This is called when the database is first created
    }
    
    fun populateDatabase(context: Context, database: MizanDatabase) {
        val scope = CoroutineScope(Dispatchers.IO)
        
        scope.launch {
            // Clear existing data
            database.categoryDao().clearAllCategories()
            database.accountDao().clearAllAccounts()
            
            // Seed Categories
            seedCategories(database.categoryDao())
            
            // Seed Accounts
            seedAccounts(database.accountDao())
        }
    }
    
    private suspend fun seedCategories(categoryDao: dev.esbi.mizan.data.local.dao.CategoryDao) {
        val expenseCategories = listOf(
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Food & Dining",
                iconName = "restaurant",
                type = "EXPENSE",
                color = "red"
            ),
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Transportation",
                iconName = "car",
                type = "EXPENSE",
                color = "blue"
            ),
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Shopping",
                iconName = "shopping_bag",
                type = "EXPENSE",
                color = "purple"
            ),
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Entertainment",
                iconName = "movie",
                type = "EXPENSE",
                color = "pink"
            ),
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Bills & Utilities",
                iconName = "receipt",
                type = "EXPENSE",
                color = "orange"
            ),
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Healthcare",
                iconName = "medical",
                type = "EXPENSE",
                color = "green"
            ),
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Education",
                iconName = "school",
                type = "EXPENSE",
                color = "indigo"
            ),
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Other",
                iconName = "more_horiz",
                type = "EXPENSE",
                color = "gray"
            )
        )
        
        val incomeCategories = listOf(
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Salary",
                iconName = "work",
                type = "INCOME",
                color = "emerald"
            ),
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Freelance",
                iconName = "laptop",
                type = "INCOME",
                color = "cyan"
            ),
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Investment",
                iconName = "trending_up",
                type = "INCOME",
                color = "teal"
            ),
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Business",
                iconName = "business",
                type = "INCOME",
                color = "blue"
            ),
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Gift",
                iconName = "card_giftcard",
                type = "INCOME",
                color = "pink"
            ),
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = "Other",
                iconName = "more_horiz",
                type = "INCOME",
                color = "gray"
            )
        )
        
        categoryDao.insertCategories(expenseCategories + incomeCategories)
    }
    
    private suspend fun seedAccounts(accountDao: dev.esbi.mizan.data.local.dao.AccountDao) {
        val defaultAccounts = listOf(
            AccountEntity(
                id = UUID.randomUUID().toString(),
                name = "Cash",
                iconName = "wallet",
                currentBalance = 0.0,
                currency = "USD"
            ),
            AccountEntity(
                id = UUID.randomUUID().toString(),
                name = "Debit Card",
                iconName = "credit_card",
                currentBalance = 0.0,
                currency = "USD"
            ),
            AccountEntity(
                id = UUID.randomUUID().toString(),
                name = "Credit Card",
                iconName = "credit_card",
                currentBalance = 0.0,
                currency = "USD"
            ),
            AccountEntity(
                id = UUID.randomUUID().toString(),
                name = "Bank Account",
                iconName = "account_balance",
                currentBalance = 0.0,
                currency = "USD"
            ),
            AccountEntity(
                id = UUID.randomUUID().toString(),
                name = "Savings",
                iconName = "savings",
                currentBalance = 0.0,
                currency = "USD"
            )
        )
        
        accountDao.insertAccounts(defaultAccounts)
    }
}
