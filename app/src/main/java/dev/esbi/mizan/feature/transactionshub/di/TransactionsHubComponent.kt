package dev.esbi.mizan.feature.transactionshub.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.transactionshub.TransactionsHubViewModel

@ScreenScope
@Subcomponent(modules = [TransactionsHubModule::class])
interface TransactionsHubComponent {

    val viewModel: TransactionsHubViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): TransactionsHubComponent
    }
}
