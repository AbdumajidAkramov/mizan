package dev.esbi.mizan.feature.calc.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStore
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStoreFactory

@Module
class MizanCalculatorModule {
    @Provides
    @ScreenScope
    fun provideCalculatorStoreFactory(
        storeFactory: StoreFactory
    ): CalculatorStoreFactory {
        return CalculatorStoreFactory(storeFactory)
    }

    @Provides
    @ScreenScope
    fun provideCalculatorStore(
        calculatorStoreFactory: CalculatorStoreFactory
    ): CalculatorStore = calculatorStoreFactory.create()
}
