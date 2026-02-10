package dev.esbi.mizan.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.data.local.dao.AccountGroupDao
import dev.esbi.mizan.data.local.dao.BudgetDao
import dev.esbi.mizan.data.local.dao.CategoryDao
import dev.esbi.mizan.data.local.dao.CurrencyDao
import dev.esbi.mizan.data.local.dao.DashboardDao
import dev.esbi.mizan.data.local.dao.FinancialMirrorDao
import dev.esbi.mizan.data.local.dao.GoalDao
import dev.esbi.mizan.data.local.dao.SubscriptionDao
import dev.esbi.mizan.data.local.dao.TemplateDao
import dev.esbi.mizan.data.local.dao.TransactionItemDao
import dev.esbi.mizan.data.local.dao.TransactionsDao
import dev.esbi.mizan.data.local.entity.CategoryBudgetEntity
import dev.esbi.mizan.data.local.entity.GoalEntity
import dev.esbi.mizan.data.local.entity.SubscriptionEntity
import dev.esbi.mizan.data.local.entity.CategorySpendingEntity
import dev.esbi.mizan.data.local.entity.DashboardSummaryEntity
import dev.esbi.mizan.data.local.entity.FinancialProjectionEntity
import dev.esbi.mizan.data.local.entity.InvestmentOpportunityEntity
import dev.esbi.mizan.data.local.entity.RiskFactorEntity
import dev.esbi.mizan.data.local.entity.TimeMachineScenarioEntity
import dev.esbi.mizan.data.local.entity.TransactionDetailEntity
import dev.esbi.mizan.data.local.entity.WeeklySpendingEntity
import dev.esbi.mizan.data.local.entity.account.AccountEntity
import dev.esbi.mizan.data.local.entity.account.AccountGroupEntity
import dev.esbi.mizan.data.local.entity.category.CategoryEntity
import dev.esbi.mizan.data.local.entity.currency.CurrencyEntity
import dev.esbi.mizan.data.local.entity.template.TemplateEntity
import dev.esbi.mizan.data.local.entity.transaction.TransactionEntity
import dev.esbi.mizan.data.local.entity.transaction.TransactionItemEntity

@Database(
    entities = [
        DashboardSummaryEntity::class,
        CategorySpendingEntity::class,
        WeeklySpendingEntity::class,
        TransactionDetailEntity::class,
        FinancialProjectionEntity::class,
        RiskFactorEntity::class,
        TimeMachineScenarioEntity::class,
        InvestmentOpportunityEntity::class,
        CategoryBudgetEntity::class,
        AccountEntity::class,
        TransactionEntity::class,
        TransactionItemEntity::class,
        CategoryEntity::class,
        CurrencyEntity::class,
        AccountGroupEntity::class,
        TemplateEntity::class,
        GoalEntity::class,
        SubscriptionEntity::class
    ],
    version = 4,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MizanDatabase : RoomDatabase() {
    abstract fun dashboardDao(): DashboardDao
    abstract fun financialMirrorDao(): FinancialMirrorDao
    abstract fun budgetDao(): BudgetDao
    abstract fun transactionsDao(): TransactionsDao
    abstract fun transactionItemDao(): TransactionItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
    abstract fun accountGroupDao(): AccountGroupDao
    abstract fun currencyDao(): CurrencyDao
    abstract fun templateDao(): TemplateDao
    abstract fun goalDao(): GoalDao
    abstract fun subscriptionDao(): SubscriptionDao
}
