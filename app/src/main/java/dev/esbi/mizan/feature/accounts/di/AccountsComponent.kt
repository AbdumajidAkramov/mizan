package dev.esbi.mizan.feature.accounts.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.accounts.AccountsViewModel

@ScreenScope
@Subcomponent(modules = [AccountsModule::class])
interface AccountsComponent {

    val viewModel: AccountsViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): AccountsComponent
    }
}
