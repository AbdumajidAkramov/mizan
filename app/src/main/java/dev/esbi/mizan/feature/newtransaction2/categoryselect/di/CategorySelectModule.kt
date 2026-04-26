package dev.esbi.mizan.feature.newtransaction2.categoryselect.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.newtransaction2.categoryselect.CategorySelectViewModel
import dev.esbi.mizan.feature.newtransaction2.categoryselect.store.CategorySelectExecutor
import dev.esbi.mizan.feature.newtransaction2.categoryselect.store.CategorySelectStoreFactory
import dev.esbi.mizan.presentation.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher

@Module
internal object CategorySelectModule {

    @Provides
    @ScreenScope
    fun provideCategorySelectExecutorFactory(
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ): CategorySelectExecutor.Factory {
        return object : CategorySelectExecutor.Factory {
            override fun create(): CategorySelectExecutor {
                return CategorySelectExecutor(mainDispatcher)
            }
        }
    }

    @Provides
    @ScreenScope
    fun provideCategorySelectStoreFactory(
        storeFactory: StoreFactory,
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
    ): CategorySelectStoreFactory {
        return CategorySelectStoreFactory(
            storeFactory,
            mainDispatcher
        )
    }

    @Provides
    @ScreenScope
    fun provideCategorySelectViewModel(
        storeFactory: CategorySelectStoreFactory
    ): CategorySelectViewModel {
        return CategorySelectViewModel(storeFactory)
    }
}
