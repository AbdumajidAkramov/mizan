package dev.esbi.mizan.feature.newtransaction2.categoryselect.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.presentation.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher

internal class DefaultCategorySelectStore(
    storeFactory: StoreFactory,
    initialState: CategorySelectStore.State,
//    executorFactory: CategorySelectExecutor.Factory,
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : CategorySelectStore,
    Store<CategorySelectStore.Intent, CategorySelectStore.State, CategorySelectStore.Label> by storeFactory.create(
        name = "CategorySelectStore",
        initialState = initialState,
        bootstrapper = SimpleBootstrapper(),
        executorFactory = { CategorySelectExecutor(mainDispatcher) },
        reducer = CategorySelectReducer
    )
