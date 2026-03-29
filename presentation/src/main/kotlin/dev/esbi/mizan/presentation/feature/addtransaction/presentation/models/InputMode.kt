package dev.esbi.mizan.presentation.feature.addtransaction.presentation.models

import dev.esbi.mizan.domain.model.Transaction

// Tiplar va Enumlar
enum class InputMode { Manual, Voice, Scan }
enum class FlowState { Amount, Type, Details, Confirm }
//enum class TransactionType { Expense, Income, Transfer }
typealias TransactionType = Transaction.Type
