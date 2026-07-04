package dev.esbi.mizan.data.local

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.data.local.dao.CategoryDao
import dev.esbi.mizan.data.local.entity.account.AccountEntity
import dev.esbi.mizan.data.local.entity.category.CategoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
//            seedCategories(database.categoryDao())

            // Seed Accounts
//            seedAccounts(database.accountDao())
        }
    }

    /*
        private suspend fun seedCategories(categoryDao: CategoryDao) {
            // Main Categories first
            val mainCategories = listOf(
                // Expense Main Categories
                CategoryEntity(
                    id = "food_main",
                    name = "Food",
                    iconName = "food",
                    type = "EXPENSE",
                    color = "red",
                    parentId = null
                ),
                CategoryEntity(
                    id = "transport_main",
                    name = "Transport",
                    iconName = "car",
                    type = "EXPENSE",
                    color = "blue",
                    parentId = null
                ),
                CategoryEntity(
                    id = "shopping_main",
                    name = "Shopping",
                    iconName = "bag",
                    type = "EXPENSE",
                    color = "purple",
                    parentId = null
                ),
                CategoryEntity(
                    id = "housing_main",
                    name = "Housing",
                    iconName = "home",
                    type = "EXPENSE",
                    color = "green",
                    parentId = null
                ),
                CategoryEntity(
                    id = "personal_main",
                    name = "Personal",
                    iconName = "user",
                    type = "EXPENSE",
                    color = "orange",
                    parentId = null
                ),

                // Income Main Categories
                CategoryEntity(
                    id = "salary_main",
                    name = "Salary",
                    iconName = "cash",
                    type = "INCOME",
                    color = "emerald",
                    parentId = null
                ),
                CategoryEntity(
                    id = "transfers_main",
                    name = "Transfers",
                    iconName = "arrow_down",
                    type = "INCOME",
                    color = "cyan",
                    parentId = null
                )
            )

            // Insert main categories first
            categoryDao.insertCategories(mainCategories)

            // Subcategories for Food
            val foodSubcategories = listOf(
                CategoryEntity(
                    id = "food_groceries",
                    name = "Groceries",
                    iconName = "shopping_cart",
                    type = "EXPENSE",
                    color = "red",
                    parentId = "food_main"
                ),
                CategoryEntity(
                    id = "food_restaurants",
                    name = "Restaurants",
                    iconName = "restaurant",
                    type = "EXPENSE",
                    color = "red",
                    parentId = "food_main"
                ),
                CategoryEntity(
                    id = "food_fast",
                    name = "Fast Food",
                    iconName = "fastfood",
                    type = "EXPENSE",
                    color = "red",
                    parentId = "food_main"
                )
            )

            // Subcategories for Transport
            val transportSubcategories = listOf(
                CategoryEntity(
                    id = "transport_taxi",
                    name = "Taxi",
                    iconName = "local_taxi",
                    type = "EXPENSE",
                    color = "blue",
                    parentId = "transport_main"
                ),
                CategoryEntity(
                    id = "transport_public",
                    name = "Bus/Metro",
                    iconName = "directions_bus",
                    type = "EXPENSE",
                    color = "blue",
                    parentId = "transport_main"
                ),
                CategoryEntity(
                    id = "transport_fuel",
                    name = "Fuel",
                    iconName = "local_gas_station",
                    type = "EXPENSE",
                    color = "blue",
                    parentId = "transport_main"
                ),
                CategoryEntity(
                    id = "transport_maintenance",
                    name = "Maintenance",
                    iconName = "build",
                    type = "EXPENSE",
                    color = "blue",
                    parentId = "transport_main"
                )
            )

            // Subcategories for Shopping
            val shoppingSubcategories = listOf(
                CategoryEntity(
                    id = "shopping_clothes",
                    name = "Clothes",
                    iconName = "checkroom",
                    type = "EXPENSE",
                    color = "purple",
                    parentId = "shopping_main"
                ),
                CategoryEntity(
                    id = "shopping_electronics",
                    name = "Electronics",
                    iconName = "devices",
                    type = "EXPENSE",
                    color = "purple",
                    parentId = "shopping_main"
                ),
                CategoryEntity(
                    id = "shopping_home",
                    name = "Home",
                    iconName = "home",
                    type = "EXPENSE",
                    color = "purple",
                    parentId = "shopping_main"
                )
            )

            // Subcategories for Housing
            val housingSubcategories = listOf(
                CategoryEntity(
                    id = "housing_rent",
                    name = "Rent",
                    iconName = "apartment",
                    type = "EXPENSE",
                    color = "green",
                    parentId = "housing_main"
                ),
                CategoryEntity(
                    id = "housing_utilities",
                    name = "Utilities",
                    iconName = "bolt",
                    type = "EXPENSE",
                    color = "green",
                    parentId = "housing_main"
                ),
                CategoryEntity(
                    id = "housing_internet",
                    name = "Internet",
                    iconName = "wifi",
                    type = "EXPENSE",
                    color = "green",
                    parentId = "housing_main"
                )
            )

            // Subcategories for Personal
            val personalSubcategories = listOf(
                CategoryEntity(
                    id = "personal_haircut",
                    name = "Haircut",
                    iconName = "content_cut",
                    type = "EXPENSE",
                    color = "orange",
                    parentId = "personal_main"
                ),
                CategoryEntity(
                    id = "personal_gym",
                    name = "Gym",
                    iconName = "fitness_center",
                    type = "EXPENSE",
                    color = "orange",
                    parentId = "personal_main"
                ),
                CategoryEntity(
                    id = "personal_health",
                    name = "Health",
                    iconName = "medical_services",
                    type = "EXPENSE",
                    color = "orange",
                    parentId = "personal_main"
                )
            )

            // Subcategories for Salary
            val salarySubcategories = listOf(
                CategoryEntity(
                    id = "salary_main_job",
                    name = "Main Job",
                    iconName = "work",
                    type = "INCOME",
                    color = "emerald",
                    parentId = "salary_main"
                ),
                CategoryEntity(
                    id = "salary_part_time",
                    name = "Part-time",
                    iconName = "schedule",
                    type = "INCOME",
                    color = "emerald",
                    parentId = "salary_main"
                ),
                CategoryEntity(
                    id = "salary_bonus",
                    name = "Bonus",
                    iconName = "card_giftcard",
                    type = "INCOME",
                    color = "emerald",
                    parentId = "salary_main"
                )
            )

            // Subcategories for Transfers
            val transfersSubcategories = listOf(
                CategoryEntity(
                    id = "transfers_gift",
                    name = "Gift",
                    iconName = "card_giftcard",
                    type = "INCOME",
                    color = "cyan",
                    parentId = "transfers_main"
                ),
                CategoryEntity(
                    id = "transfers_refund",
                    name = "Refund",
                    iconName = "replay",
                    type = "INCOME",
                    color = "cyan",
                    parentId = "transfers_main"
                )
            )

            // Insert all subcategories
            categoryDao.insertCategories(
                foodSubcategories + transportSubcategories + shoppingSubcategories +
                housingSubcategories + personalSubcategories + salarySubcategories + transfersSubcategories
            )
        }

        private suspend fun seedAccounts(accountDao: AccountDao) {
            val defaultAccounts = listOf(
                AccountEntity(
                    id = "cash_account",
                    name = "Cash",
                    iconName = "wallet",
                    currentBalance = 0.0,
                    currency = "UZS"
                ),
                AccountEntity(
                    id = "card_account",
                    name = "Card",
                    iconName = "card",
                    currentBalance = 0.0,
                    currency = "UZS"
                )
            )

            accountDao.insertAccounts(defaultAccounts)
        }
    */
}
