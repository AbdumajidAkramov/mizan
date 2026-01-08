package dev.esbi.mizan.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.esbi.mizan.data.local.MizanDatabase
import dev.esbi.mizan.data.local.dao.BudgetDao
import dev.esbi.mizan.data.local.dao.DashboardDao
import dev.esbi.mizan.data.local.dao.FinancialMirrorDao
import dev.esbi.mizan.data.local.dao.TransactionsDao
import javax.inject.Singleton

@Module
class DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(context: Context): MizanDatabase {
        return Room.databaseBuilder(
            context,
            MizanDatabase::class.java,
            "mizan_database"
        )
            .fallbackToDestructiveMigration()
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
}
