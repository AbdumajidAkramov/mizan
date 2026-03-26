package dev.esbi.mizan.feature.accountselector.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.accountselector.AccountSelectorViewModel

@ScreenScope
@Subcomponent(modules = [AccountSelectorModule::class])
interface AccountSelectorComponent {

    val viewModel: AccountSelectorViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): AccountSelectorComponent
    }
}
