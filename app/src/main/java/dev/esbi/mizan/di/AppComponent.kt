package dev.esbi.mizan.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dev.esbi.mizan.MainActivity
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
    ]
)
internal interface AppComponent {

    fun inject(activity: MainActivity)

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
