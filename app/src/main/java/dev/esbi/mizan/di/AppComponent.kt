package dev.esbi.mizan.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dev.esbi.mizan.MainActivity
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.feature.accounts.di.AccountsComponent
import dev.esbi.mizan.feature.accountselector.di.AccountSelectorComponent
import dev.esbi.mizan.feature.budget.di.BudgetComponent
import dev.esbi.mizan.feature.calc.di.MizanCalculatorComponent
import dev.esbi.mizan.feature.dashboard.di.DashboardComponent
import dev.esbi.mizan.feature.financialmirror.di.FinancialMirrorComponent
import dev.esbi.mizan.feature.goals.di.GoalsComponent
import dev.esbi.mizan.feature.managecategories.di.ManageCategoriesComponent
import dev.esbi.mizan.feature.profile.di.ProfileComponent
import dev.esbi.mizan.feature.statistics.di.StatisticsComponent
import dev.esbi.mizan.feature.subscriptions.di.SubscriptionsComponent
import dev.esbi.mizan.feature.transactionshub.di.TransactionsHubComponent
import dev.esbi.mizan.features.addtransaction.di.AmountInputComponent
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DatabaseModule::class,
        DashboardModule::class,
        FinancialMirrorModule::class,
        BudgetModule::class,
        GoalsModule::class,
        SubscriptionsModule::class,
        RepositoryModule::class,
        StatisticsModule::class,
        ProfileModule::class,
        ViewModelModule::class,
        SubcomponentsModule::class
    ]
)
internal interface AppComponent {

    fun inject(activity: MainActivity)

    // Subcomponent factories for New Transaction Flow
    fun amountInputComponent(): AmountInputComponent.Factory

    // Subcomponent factories for Main Screens
    fun accountManagementComponent(): AccountsComponent.Factory
    fun accountSelectorComponent(): AccountSelectorComponent.Factory
    fun budgetComponent(): BudgetComponent.Factory
    fun dashboardComponent(): DashboardComponent.Factory
    fun financialMirrorComponent(): FinancialMirrorComponent.Factory
    fun goalsComponent(): GoalsComponent.Factory
    fun subscriptionsComponent(): SubscriptionsComponent.Factory
    fun manageCategoriesComponent(): ManageCategoriesComponent.Factory
    fun profileComponent(): ProfileComponent.Factory
    fun statisticsComponent(): StatisticsComponent.Factory
    fun transactionsHubComponent(): TransactionsHubComponent.Factory

    fun mizanCalculatorComponent(): MizanCalculatorComponent.Factory

    // Provide globally
    val accountRepository: AccountRepository
    val currencyRepository: CurrencyRepository
    val storeFactory: com.arkivanov.mvikotlin.core.store.StoreFactory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }

    companion object {
        operator fun invoke(
            application: Application
        ): AppComponent {
            return DaggerAppComponent.factory().create(
                application
            )
        }
    }
}
