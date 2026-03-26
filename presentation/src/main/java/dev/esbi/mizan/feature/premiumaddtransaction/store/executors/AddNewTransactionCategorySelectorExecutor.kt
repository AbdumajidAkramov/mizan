package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.domain.repository.TransactionRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.CategoryRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.TemplateRepository
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AddNewTransactionCategorySelectorExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val categoryRepository: CategoryRepository,
    private val currencyRepository: CurrencyRepository,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val templateRepository: TemplateRepository

) : CoroutineExecutor<AddNewTransactionStore.Intent,
        AddNewTransactionStore.Action,
        AddNewTransactionStore.State,
        AddNewTransactionStore.Message,
        AddNewTransactionStore.Label>() {

    override fun executeAction(action: AddNewTransactionStore.Action) {
        when (action) {
            is AddNewTransactionStore.Action.InitCategories -> {
                fetchCategories()
            }

            else -> Unit
        }
    }

    override fun executeIntent(intent: AddNewTransactionStore.Intent) {
        when (intent) {
            is AddNewTransactionStore.Intent.OnCategorySelect -> {
                dispatch(AddNewTransactionStore.Message.UpdateSelectedCategory(intent.category))

                val nonSubCategory = state().categories.none { intent.category.id == it.parentId }
                if (nonSubCategory) {
                    dispatch(AddNewTransactionStore.Message.UpdateSelectedSubCategory(null))
                    forward(AddNewTransactionStore.Action.CheckAndConfirm)
                    dispatch(AddNewTransactionStore.Message.UpdateCategoriesBottomSheet(false))
                }
                if (state().selectedSubCategory?.parentId != intent.category.id) {
                    dispatch(AddNewTransactionStore.Message.UpdateSelectedSubCategory(null))
                }
            }

            is AddNewTransactionStore.Intent.OnSubCategorySelect -> {
                dispatch(AddNewTransactionStore.Message.UpdateSelectedSubCategory(intent.subCategory))
                dispatch(AddNewTransactionStore.Message.UpdateCategoriesBottomSheet(false))
                forward(AddNewTransactionStore.Action.CheckAndConfirm)
            }

            is AddNewTransactionStore.Intent.OpenCategoryManageScreen -> {
                publish(AddNewTransactionStore.Label.OpenCategoryManageScreen)
            }

            else -> Unit
        }
    }

    private fun fetchCategories() {
        categoryRepository.getAllCategories()
            .onEach { categories ->
                dispatch(AddNewTransactionStore.Message.UpdateAllCategories(categories))
            }
            .launchIn(scope)
    }
}
