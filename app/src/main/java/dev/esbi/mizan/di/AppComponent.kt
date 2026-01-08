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
        ProfileModule::class
    ]
)
interface AppComponent {
    
    fun inject(activity: MainActivity)
    
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        
        fun build(): AppComponent
    }
}
