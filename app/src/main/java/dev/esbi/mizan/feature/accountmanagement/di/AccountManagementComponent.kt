package dev.esbi.mizan.feature.accountmanagement.di

import dagger.Subcomponent
import dev.esbi.mizan.feature.accountmanagement.AccountManagementViewModel

@Subcomponent(modules = [AccountManagementModule::class])
interface AccountManagementComponent {

    val viewModel: AccountManagementViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): AccountManagementComponent
    }
}
