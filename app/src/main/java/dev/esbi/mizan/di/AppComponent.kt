package dev.esbi.mizan.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dev.esbi.mizan.MainActivity
import dev.esbi.mizan.feature.budget.di.BudgetComponent
import dev.esbi.mizan.feature.dashboard.di.DashboardComponent
import dev.esbi.mizan.feature.financialmirror.di.FinancialMirrorComponent
import dev.esbi.mizan.feature.newtransaction.amountinput.di.AmountInputComponent
import dev.esbi.mizan.feature.newtransaction.categoryselect.di.CategorySelectComponent
import dev.esbi.mizan.feature.profile.di.ProfileComponent
import dev.esbi.mizan.feature.statistics.di.StatisticsComponent
import dev.esbi.mizan.feature.transactions.di.TransactionsComponent
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DatabaseModule::class,
        DashboardModule::class,
        FinancialMirrorModule::class,
        BudgetModule::class,
        TransactionsModule::class,
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
    fun categorySelectComponent(): CategorySelectComponent.Factory

    // Subcomponent factories for Main Screens
    fun budgetComponent(): BudgetComponent.Factory
    fun dashboardComponent(): DashboardComponent.Factory
    fun financialMirrorComponent(): FinancialMirrorComponent.Factory
    fun profileComponent(): ProfileComponent.Factory
    fun statisticsComponent(): StatisticsComponent.Factory
    fun transactionsComponent(): TransactionsComponent.Factory

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
