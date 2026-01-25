package dev.esbi.mizan.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.esbi.mizan.di.provider.MizanViewModelFactory
import dev.esbi.mizan.feature.addtransaction.presentation.AddTransactionViewModel
import dev.esbi.mizan.feature.budget.presentation.BudgetViewModel
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModel
import dev.esbi.mizan.feature.financialmirror.presentation.FinancialMirrorViewModel
import dev.esbi.mizan.feature.newtransaction.amountinput.AmountInputViewModel
import dev.esbi.mizan.feature.profile.presentation.ProfileViewModel
import dev.esbi.mizan.feature.statistics.presentation.StatisticsViewModel
import dev.esbi.mizan.feature.transactions.presentation.TransactionsViewModel

@Module
internal abstract class ViewModelModule {

    // Umumiy Factoryni bind qilish
    @Binds
    abstract fun bindViewModelFactory(factory: MizanViewModelFactory): ViewModelProvider.Factory

    // 1. DashboardViewModel ni bind qilish
    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(viewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddTransactionViewModel::class)
    abstract fun bindAddTransactionViewModel(viewModel: AddTransactionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BudgetViewModel::class)
    abstract fun bindBudgetViewModel(viewModel: BudgetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FinancialMirrorViewModel::class)
    abstract fun bindFinancialMirrorViewModel(viewModel: FinancialMirrorViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatisticsViewModel::class)
    abstract fun bindStatisticsViewModel(viewModel: StatisticsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionsViewModel::class)
    abstract fun bindTransactionsViewModel(viewModel: TransactionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AmountInputViewModel::class)
    abstract fun bindAmountInputViewModel(viewModel: AmountInputViewModel): ViewModel

}