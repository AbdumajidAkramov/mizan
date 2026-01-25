package dev.esbi.mizan.feature.newtransaction.amountinput.di

import com.arkivanov.mvikotlin.core.store.Executor
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStoreImpl
import dev.esbi.mizan.feature.newtransaction.amountinput.store.executors.ManualInputExecutor
import dev.esbi.mizan.feature.newtransaction.amountinput.store.executors.VoiceInputExecutor

@Module
internal interface AmountInputModule {

    @IntoSet
    @Binds
    fun bindsManualInputExecutor(
        executor: ManualInputExecutor
    ): Executor<AmountInputStore.Intent, AmountInputStore.Action, AmountInputStore.State, AmountInputStore.Message, AmountInputStore.Label>

    @Binds
    @IntoSet
    fun bindsVoiceInputExecutor(
        executor: VoiceInputExecutor
    ): Executor<AmountInputStore.Intent, AmountInputStore.Action, AmountInputStore.State, AmountInputStore.Message, AmountInputStore.Label>

    @Binds
    fun store(impl: AmountInputStoreImpl): AmountInputStore
}
