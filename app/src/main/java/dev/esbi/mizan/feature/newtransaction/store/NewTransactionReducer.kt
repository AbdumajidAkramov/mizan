package dev.esbi.mizan.feature.newtransaction.store

import com.arkivanov.mvikotlin.core.store.Reducer

internal object NewTransactionReducer : Reducer<AmountInputState, NewTransactionStore.Message> {
    override fun AmountInputState.reduce(msg: NewTransactionStore.Message): AmountInputState {
        return when (msg) {
            is NewTransactionStore.Message.UpdateTransactionType -> copy(transactionType = msg.type)
            is NewTransactionStore.Message.UpdateKeypadState -> copy(keypadState = msg.state)
            is NewTransactionStore.Message.UpdateVoiceInputStateState -> copy(voiceInputState = msg.state)
            is NewTransactionStore.Message.UpdateMode -> copy(inputMode = msg.mode)
            is NewTransactionStore.Message.UpdateCameraInputState -> copy(cameraInputState = msg.state)
            is NewTransactionStore.Message.QrCodeDetected -> copy(
                cameraInputState = cameraInputState.copy(
                    qrtext = msg.text,
                    isScanning = false
                )
            )

            is NewTransactionStore.Message.UpdateListeningState -> copy(
                voiceInputState = voiceInputState.copy(isListening = msg.isListening)
            )

            is NewTransactionStore.Message.UpdateVoiceError -> copy(
                voiceInputState = voiceInputState.copy(voiceRecognitionError = msg.error)
            )

            is NewTransactionStore.Message.UpdateVoiceResult -> copy(
                voiceInputState = voiceInputState.copy(voiceResult = msg.result)
            )

            is NewTransactionStore.Message.UpdateTransactionStep -> copy(currentPage = msg.step)

            // NewTransactionStore.CategoryChooserMessage
            is NewTransactionStore.CategoryChooserMessage.CategoriesLoaded -> copy(
                categoryChooserState = categoryChooserState.copy(
                    categories = msg.categories,
                    isLoading = false,
                    error = null
                )
            )

            is NewTransactionStore.CategoryChooserMessage.LoadingChanged -> copy(
                categoryChooserState = categoryChooserState.copy(
                    isLoading = msg.isLoading,
                )
            )

            is NewTransactionStore.CategoryChooserMessage.ErrorChanged -> copy(
                categoryChooserState = categoryChooserState.copy(
                    error = msg.error,
                    isLoading = false
                )
            )

            is NewTransactionStore.CategoryChooserMessage.ParentCategorySelected -> copy(
                categoryChooserState = categoryChooserState.copy(
                    selectedParentId = msg.parentId
                )
            )

            is NewTransactionStore.CategoryChooserMessage.NavigateToParent -> copy(
                categoryChooserState = categoryChooserState.copy(
                    selectedParentId = null
                )
            )

            is NewTransactionStore.CategoryChooserMessage.TransactionTypeChanged -> copy(
                categoryChooserState = categoryChooserState.copy(
                    transactionType = msg.transactionType,
                    selectedParentId = null,
                    selectedCategory = null
                )
            )

            is NewTransactionStore.CategoryChooserMessage.CategorySelected -> copy(
                categoryChooserState = categoryChooserState.copy(
                    selectedCategory = msg.category
                )
            )
        }
    }
}
