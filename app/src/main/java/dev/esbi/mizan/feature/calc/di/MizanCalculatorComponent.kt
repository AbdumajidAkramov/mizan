package dev.esbi.mizan.feature.calc.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.calc.MizanCalculatorViewModel

@ScreenScope
@Subcomponent(modules = [MizanCalculatorModule::class])
interface MizanCalculatorComponent {

    val viewModel: MizanCalculatorViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): MizanCalculatorComponent
    }
}
