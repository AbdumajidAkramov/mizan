package dev.esbi.mizan.presentation.feature.managecategories.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.presentation.feature.addtransaction.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Factory for creating ManageCategoriesStore
 */
class ManageCategoriesStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val categoryRepository: CategoryRepository
) {

    fun create(): ManageCategoriesStore =
        object : ManageCategoriesStore,
            Store<ManageCategoriesStore.Intent, ManageCategoriesStore.State, ManageCategoriesStore.Label> by storeFactory.create(
                name = "ManageCategoriesStore",
                initialState = ManageCategoriesStore.State(),
                bootstrapper = CoroutineBootstrapperImpl(),
                executorFactory = {
                    ManageCategoriesExecutor(
                        mainDispatcher = mainDispatcher,
                        categoryRepository = categoryRepository
                    )
                },
                reducer = ManageCategoriesReducer()
            ) {}

    private class CoroutineBootstrapperImpl : CoroutineBootstrapper<Unit>() {
        override fun invoke() {
            dispatch(Unit)
        }
    }
}
