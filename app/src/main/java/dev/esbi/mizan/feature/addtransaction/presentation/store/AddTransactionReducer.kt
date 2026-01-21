package dev.esbi.mizan.feature.addtransaction.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import dev.esbi.mizan.feature.addtransaction.presentation.models.FlowState
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.ClearText
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateCameraScanError
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateCameraScanningState
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateFlowState
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateInputMode
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateLeftText
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateOperator
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateReceiptScanText
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateRecognizedAmount
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateRightText
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateTransactionCategory
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateTransactionDate
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateTransactionNotes
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateTransactionType
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateVoiceListeningState
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateVoiceRecognitionError
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore.Message.UpdateVoiceRecognitionText

internal object AddTransactionReducer :
    Reducer<AddTransactionStore.State, AddTransactionStore.Message> {
    override fun AddTransactionStore.State.reduce(msg: AddTransactionStore.Message): AddTransactionStore.State =
        when (msg) {
            is UpdateFlowState -> copy(flowState = msg.flowState)
            is UpdateInputMode -> copy(inputMode = msg.inputMode)
            is UpdateOperator -> copy(operator = msg.text)
            is UpdateLeftText -> copy(leftNumber = msg.text)
            is UpdateRightText -> copy(rightNumber = msg.text)
            is ClearText -> copy(leftNumber = "", rightNumber = "", operator = "")
            is UpdateTransactionCategory -> copy(
                selectedCategory = msg.category,
                flowState = FlowState.Confirm
            )

            is UpdateTransactionType -> copy(type = msg.type)
            is UpdateTransactionDate -> copy(date = msg.date)
            is UpdateTransactionNotes -> copy(notes = msg.notes)

            // Voice Recognition Messages
            is UpdateVoiceListeningState -> copy(isVoiceListening = msg.isListening)
            is UpdateVoiceRecognitionText -> copy(voiceRecognitionText = msg.text)
            is UpdateVoiceRecognitionError -> copy(voiceRecognitionError = msg.error)

            // Camera Scan Messages
            is UpdateCameraScanningState -> copy(isCameraScanning = msg.isScanning)
            is UpdateReceiptScanText -> copy(receiptScanText = msg.text)
            is UpdateCameraScanError -> copy(cameraScanError = msg.error)
            
            // CRITICAL: When amount is recognized from camera, update leftNumber and reset calculator state
            is UpdateRecognizedAmount -> copy(
                leftNumber = msg.amount.toString(),
                rightNumber = "",
                operator = "",
                lastRecognizedAmount = msg.amount
            )
        }
}
