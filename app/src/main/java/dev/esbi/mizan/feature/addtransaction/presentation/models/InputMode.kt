package dev.esbi.mizan.feature.addtransaction.presentation.models

// Tiplar va Enumlar
enum class InputMode { Manual, Voice, Scan }
enum class FlowState { Amount, Type, Details, Confirm }
enum class TransactionType { Expense, Income, Transfer }
