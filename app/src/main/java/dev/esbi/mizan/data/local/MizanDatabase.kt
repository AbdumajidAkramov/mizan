package dev.esbi.mizan.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.data.local.dao.BudgetDao
import dev.esbi.mizan.data.local.dao.CategoryDao
import dev.esbi.mizan.data.local.dao.DashboardDao
import dev.esbi.mizan.data.local.dao.FinancialMirrorDao
import dev.esbi.mizan.data.local.dao.TransactionsDao
import dev.esbi.mizan.data.local.entity.AccountEntity
import dev.esbi.mizan.data.local.entity.CategoryBudgetEntity
import dev.esbi.mizan.data.local.entity.CategorySpendingEntity
import dev.esbi.mizan.data.local.entity.CategoryEntity
import dev.esbi.mizan.data.local.entity.DashboardSummaryEntity
import dev.esbi.mizan.data.local.entity.FinancialProjectionEntity
import dev.esbi.mizan.data.local.entity.InvestmentOpportunityEntity
import dev.esbi.mizan.data.local.entity.RiskFactorEntity
import dev.esbi.mizan.data.local.entity.TimeMachineScenarioEntity
import dev.esbi.mizan.data.local.entity.TransactionDetailEntity
import dev.esbi.mizan.data.local.entity.TransactionEntity
import dev.esbi.mizan.data.local.entity.WeeklySpendingEntity

@Database(
    entities = [
        DashboardSummaryEntity::class,
        CategorySpendingEntity::class,
        WeeklySpendingEntity::class,
        TransactionEntity::class,
        TransactionDetailEntity::class,
        FinancialProjectionEntity::class,
        RiskFactorEntity::class,
        TimeMachineScenarioEntity::class,
        InvestmentOpportunityEntity::class,
        CategoryBudgetEntity::class,
        CategoryEntity::class,
        AccountEntity::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MizanDatabase : RoomDatabase() {
    abstract fun dashboardDao(): DashboardDao
    abstract fun financialMirrorDao(): FinancialMirrorDao
    abstract fun budgetDao(): BudgetDao
    abstract fun transactionsDao(): TransactionsDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
}
