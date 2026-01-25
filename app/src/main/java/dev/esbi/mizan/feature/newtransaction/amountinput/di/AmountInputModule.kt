package dev.esbi.mizan.feature.newtransaction.amountinput.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.CameraScannerHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.ManualInputHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.NavigationHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputObserver
import dev.esbi.mizan.feature.newtransaction.amountinput.store.executors.AmountInputExecutor
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
internal object AmountInputModule {

    @Provides
    @Singleton
    fun provideAmountInputExecutor(
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
        manualInputHandler: ManualInputHandler,
        navigationHandler: NavigationHandler
    ): AmountInputExecutor {
        return AmountInputExecutor(
            mainDispatcher = mainDispatcher,
            manualInputHandler = manualInputHandler,
            navigationHandler = navigationHandler
        )
    }

    @Provides
    @Singleton
    fun provideCameraScannerHandler(): CameraScannerHandler {
        return CameraScannerHandler()
    }

    @Provides
    @Singleton
    fun provideCalculatorHandler(): ManualInputHandler {
        return ManualInputHandler()
    }

    @Provides
    @Singleton
    fun provideNavigationHandler(): NavigationHandler {
        return NavigationHandler()
    }

    @Provides
    @Singleton
    fun provideAmountInputObserver(): AmountInputObserver {
        return AmountInputObserver()
    }
}
