package dev.esbi.mizan.feature.financialmirror.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.financialmirror.presentation.FinancialMirrorViewModel

@ScreenScope
@Subcomponent(modules = [FinancialMirrorModule::class])
interface FinancialMirrorComponent {

    val viewModel: FinancialMirrorViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): FinancialMirrorComponent
    }
}
