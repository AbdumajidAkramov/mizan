package dev.esbi.mizan.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dev.esbi.mizan.data.local.DatabaseSeedingManager
import dev.esbi.mizan.data.local.MizanDatabase
import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.data.local.dao.BudgetDao
import dev.esbi.mizan.data.local.dao.CategoryDao
import dev.esbi.mizan.data.local.dao.DashboardDao
import dev.esbi.mizan.data.local.dao.FinancialMirrorDao
import dev.esbi.mizan.data.local.dao.TemplateDao
import dev.esbi.mizan.data.local.dao.TransactionsDao
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

    @Provides
    @Singleton
    fun provideDatabase(context: Context): MizanDatabase {
        return Room.databaseBuilder(
            context,
            MizanDatabase::class.java,
            "mizan_database"
        )
            .createFromAsset("mizan.db") // Assets papkasidagi fayl nomi
            .addMigrations(migration1to2)
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
    fun provideTemplateDao(database: MizanDatabase): TemplateDao {
        return database.templateDao()
    }

    @Provides
    @Singleton
    fun provideDatabaseSeedingManager(database: MizanDatabase): DatabaseSeedingManager {
        return DatabaseSeedingManager(database)
    }
}
