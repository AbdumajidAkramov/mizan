package dev.esbi.mizan.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dev.esbi.mizan.data.local.DatabaseSeedingManager
import dev.esbi.mizan.data.util.MockCurrencyConverter
import dev.esbi.mizan.domain.util.CurrencyConverter
import dev.esbi.mizan.data.local.MizanDatabase
import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.data.local.dao.AccountGroupDao
import dev.esbi.mizan.data.local.dao.BudgetDao
import dev.esbi.mizan.data.local.dao.CategoryDao
import dev.esbi.mizan.data.local.dao.CurrencyDao
import dev.esbi.mizan.data.local.dao.DashboardDao
import dev.esbi.mizan.data.local.dao.FinancialMirrorDao
import dev.esbi.mizan.data.local.dao.GoalDao
import dev.esbi.mizan.data.local.dao.SubCurrencyDao
import dev.esbi.mizan.data.local.dao.SubscriptionDao
import dev.esbi.mizan.data.local.dao.TemplateDao
import dev.esbi.mizan.data.local.dao.TransactionsDao
import dev.esbi.mizan.data.local.seeder.MockDataSeeder
import javax.inject.Singleton

@Module
class DatabaseModule {

    private val migration1to2 = object : androidx.room.migration.Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `templates` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `name` TEXT NOT NULL,
                    `amount` REAL NOT NULL,
                    `iconName` TEXT,
                    `transactionType` TEXT NOT NULL,
                    `categoryId` INTEGER
                )
                """.trimIndent()
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_templates_categoryId` ON `templates` (`categoryId`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_templates_transactionType` ON `templates` (`transactionType`)")
        }
    }

    private val migration2to3 = object : androidx.room.migration.Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE `transactions` ADD COLUMN `currencyCode` TEXT NOT NULL DEFAULT 'UZS'")
            db.execSQL("ALTER TABLE `transactions` ADD COLUMN `exchangeRate` REAL NOT NULL DEFAULT 1.0")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_currencyCode` ON `transactions` (`currencyCode`)")
        }
    }

    private val migration3to4 = object : androidx.room.migration.Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `goals` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `name` TEXT NOT NULL,
                    `targetAmount` REAL NOT NULL,
                    `currentAmount` REAL NOT NULL,
                    `deadline` INTEGER,
                    `icon` TEXT NOT NULL,
                    `color` TEXT NOT NULL
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `subscriptions` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `name` TEXT NOT NULL,
                    `amount` REAL NOT NULL,
                    `billingCycle` TEXT NOT NULL,
                    `nextRenewalDate` INTEGER NOT NULL,
                    `icon` TEXT NOT NULL,
                    `color` TEXT NOT NULL,
                    `category` TEXT NOT NULL DEFAULT ''
                )
                """.trimIndent()
            )
        }
    }

    private val migration4to5 = object : androidx.room.migration.Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE `accounts` ADD COLUMN `is_deleted` INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val migration5to6 = object : androidx.room.migration.Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Account groups isSystemGroup column
            db.execSQL("ALTER TABLE `account_groups` ADD COLUMN `isSystemGroup` INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val migration6to7 = object : androidx.room.migration.Migration(6, 7) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `sub_currencies` (
                    `code` TEXT NOT NULL PRIMARY KEY,
                    `name` TEXT NOT NULL,
                    `symbol` TEXT NOT NULL,
                    `exchange_rate` TEXT NOT NULL,
                    `unit_position` TEXT NOT NULL DEFAULT 'FRONT',
                    `decimal_digits` INTEGER NOT NULL DEFAULT 2,
                    `order_index` INTEGER NOT NULL DEFAULT 0,
                    `is_main_currency` INTEGER NOT NULL DEFAULT 0
                )
                """.trimIndent()
            )
        }
    }

    private val migration7to8 = object : androidx.room.migration.Migration(7, 8) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE `sub_currencies` ADD COLUMN `is_user_defined` INTEGER NOT NULL DEFAULT 0")
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): MizanDatabase {
        return Room.databaseBuilder(
            context,
            MizanDatabase::class.java,
            "mizan_database"
        )
//            .createFromAsset("mizan.db") // Assets papkasidagi fayl nomi
            .addMigrations(migration1to2, migration2to3, migration3to4, migration4to5, migration5to6, migration6to7, migration7to8)
//            .addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    // Database is created, but we'll seed it on first access
//                }
//            })
            .build()
    }

    @Provides
    @Singleton
    fun provideDashboardDao(database: MizanDatabase): DashboardDao {
        return database.dashboardDao()
    }

    @Provides
    @Singleton
    fun provideFinancialMirrorDao(database: MizanDatabase): FinancialMirrorDao {
        return database.financialMirrorDao()
    }

    @Provides
    @Singleton
    fun provideBudgetDao(database: MizanDatabase): BudgetDao {
        return database.budgetDao()
    }

    @Provides
    @Singleton
    fun provideTransactionsDao(database: MizanDatabase): TransactionsDao {
        return database.transactionsDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: MizanDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    @Singleton
    fun provideAccountDao(database: MizanDatabase): AccountDao {
        return database.accountDao()
    }

    @Provides
    @Singleton
    fun provideAccountGroupDao(database: MizanDatabase): AccountGroupDao {
        return database.accountGroupDao()
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(database: MizanDatabase): CurrencyDao {
        return database.currencyDao()
    }

    @Provides
    @Singleton
    fun provideTemplateDao(database: MizanDatabase): TemplateDao {
        return database.templateDao()
    }

    @Provides
    @Singleton
    fun provideGoalDao(database: MizanDatabase): GoalDao {
        return database.goalDao()
    }

    @Provides
    @Singleton
    fun provideSubscriptionDao(database: MizanDatabase): SubscriptionDao {
        return database.subscriptionDao()
    }

    @Provides
    @Singleton
    fun provideSubCurrencyDao(database: MizanDatabase): SubCurrencyDao {
        return database.subCurrencyDao()
    }

    @Provides
    @Singleton
    fun provideDatabaseSeedingManager(database: MizanDatabase): DatabaseSeedingManager {
        return DatabaseSeedingManager(database)
    }

    @Provides
    @Singleton
    fun provideCurrencyConverter(): CurrencyConverter {
        return MockCurrencyConverter()
    }

    @Provides
    @Singleton
    fun provideMockDataSeeder(
        currencyDao: CurrencyDao,
        subCurrencyDao: SubCurrencyDao,
        accountDao: AccountDao,
        accountGroupDao: AccountGroupDao,
        categoryDao: CategoryDao,
        transactionsDao: TransactionsDao
    ): MockDataSeeder {
        return MockDataSeeder(
            currencyDao,
            subCurrencyDao,
            accountDao,
            accountGroupDao,
            categoryDao,
            transactionsDao
        )
    }
}
