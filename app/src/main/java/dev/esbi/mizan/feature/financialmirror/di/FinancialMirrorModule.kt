package dev.esbi.mizan.feature.financialmirror.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.financialmirror.presentation.FinancialMirrorViewModel
import dev.esbi.mizan.feature.financialmirror.presentation.store.FinancialMirrorStoreFactory

@Module
object FinancialMirrorModule {

    @Provides
    @ScreenScope
    fun provideFinancialMirrorViewModel(
        storeFactory: FinancialMirrorStoreFactory
    ): FinancialMirrorViewModel {
        return FinancialMirrorViewModel(storeFactory)
    }
}
