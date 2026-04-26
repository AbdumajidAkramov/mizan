package dev.esbi.mizan.feature.newtransaction2.amountinput.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.newtransaction.AmountInputViewModel
import dev.esbi.mizan.feature.premiumaddtransaction.di.AddNewTransactionDepsModule

/**
 * Dagger Subcomponent for AmountInput screen.
 * This component is scoped to the screen lifecycle, ensuring that
 * each time the screen is entered, a new instance of scoped dependencies is created.
 */
@ScreenScope
@Subcomponent(modules = [AmountInputModule::class, AddNewTransactionDepsModule::class])
internal interface AmountInputComponent {

    val viewModel: AmountInputViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): AmountInputComponent
    }
}
