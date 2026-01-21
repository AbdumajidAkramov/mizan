package dev.esbi.mizan.feature.newtransaction.root

import com.arkivanov.mvikotlin.core.store.Store

interface NewTransactionStore : Store<NewTransactionIntent, NewTransactionState, NewTransactionAction>
