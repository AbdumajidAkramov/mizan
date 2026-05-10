package dev.esbi.mizan.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dev.esbi.mizan.di.provider.MizanViewModelFactory

@Module
internal abstract class ViewModelModule {

    // Umumiy Factoryni bind qilish
    @Binds
    abstract fun bindViewModelFactory(factory: MizanViewModelFactory): ViewModelProvider.Factory
}