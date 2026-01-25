package dev.esbi.mizan.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.esbi.mizan.di.provider.MizanViewModelFactory
import dev.esbi.mizan.feature.addtransaction.presentation.AddTransactionViewModel

@Module
internal abstract class ViewModelModule {

    // Umumiy Factoryni bind qilish
    @Binds
    abstract fun bindViewModelFactory(factory: MizanViewModelFactory): ViewModelProvider.Factory

    // AddTransactionViewModel - kept for backward compatibility
    @Binds
    @IntoMap
    @ViewModelKey(AddTransactionViewModel::class)
    abstract fun bindAddTransactionViewModel(viewModel: AddTransactionViewModel): ViewModel

    // Note: All main screen ViewModels are now provided by their respective
    // screen subcomponents with @ScreenScope:
    // - DashboardViewModel -> DashboardComponent
    // - BudgetViewModel -> BudgetComponent
    // - FinancialMirrorViewModel -> FinancialMirrorComponent
    // - ProfileViewModel -> ProfileComponent
    // - StatisticsViewModel -> StatisticsComponent
    // - TransactionsViewModel -> TransactionsComponent
    // - AmountInputViewModel -> AmountInputComponent
    // - CategorySelectViewModel -> CategorySelectComponent
}