package dev.esbi.mizan.feature.calc

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class MizanCalculatorViewModel @Inject constructor(
    private val calculatorStore: CalculatorStore,
//    private val transactionStore: TransactionStore
) : ViewModel() {

    // 1. Store State-larini StateFlow ko'rinishida ochiqlaymiz (Compose uchun)
    val calculatorState: StateFlow<CalculatorStore.State> = calculatorStore.stateFlow
//    val transactionState: StateFlow<TransactionStore.State> = transactionStore.stateFlow

    init {
        // 2. Binding mantiqi: Store-larni bir-biriga yoki tashqi hodisalarga bog'lash
        bind(Dispatchers.Main.immediate) {
            // Masalan: Kalkulyatordagi natija o'zgarganda uni TransactionStore'ga yuborish
            calculatorStore.states.bindTo { state ->
                if (state.isResultShown) {
//                    transactionStore.accept(TransactionStore.Intent.UpdateAmount(state.currentValue))
                }
            }
        }
    }

    // 3. UI'dan kelayotgan Intent'larni qabul qilish funksiyalari
    fun onCalculatorIntent(intent: CalculatorStore.Intent) {
        calculatorStore.accept(intent)
    }

//    fun onTransactionIntent(intent: TransactionStore.Intent) {
//        transactionStore.accept(intent)
//    }

    // 4. Tozalash (Memory Management)
    override fun onCleared() {
        super.onCleared()
        // ViewModel o'chirilganda Store'larni bekor qilamiz
        calculatorStore.dispose()
//        transactionStore.dispose()
    }
}