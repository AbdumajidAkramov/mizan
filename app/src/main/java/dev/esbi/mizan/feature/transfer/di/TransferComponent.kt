package dev.esbi.mizan.feature.transfer.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.transfer.presentation.TransferViewModel

@ScreenScope
@Subcomponent(modules = [TransferModule::class])
interface TransferComponent {

    val viewModel: TransferViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): TransferComponent
    }
}
