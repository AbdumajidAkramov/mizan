package dev.esbi.mizan.feature.newtransaction.store

import com.arkivanov.mvikotlin.core.store.Reducer
import dev.esbi.mizan.feature.newtransaction.categorychooser.CategoryChooserState

internal object NewTransactionReducer : Reducer<AmountInputState, NewTransactionStore.Message> {
    override fun AmountInputState.reduce(msg: NewTransactionStore.Message): AmountInputState {
        return when (msg) {
            is NewTransactionStore.Message.UpdateTransactionType -> copy(
                transactionType = msg.type,
                categoryChooserState = CategoryChooserState(msg.type)
            )

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

            is NewTransactionStore.Message.UpdateNote -> copy(note = msg.note)
            is NewTransactionStore.Message.UpdateDate -> copy(transactionDate = msg.date)
            is NewTransactionStore.Message.UpdateSelectedAccount -> copy(selectedAccountId = msg.accountId)
            is NewTransactionStore.Message.UpdateTargetAccount -> copy(targetAccountId = msg.accountId)
            is NewTransactionStore.Message.UpdateAccounts -> copy(accounts = msg.accounts)
            is NewTransactionStore.Message.SetAccountSheetVisible -> copy(isAccountSheetVisible = msg.visible)
            is NewTransactionStore.Message.SetTypeSelectorVisible -> 
                copy(isTypeSelectorVisible = msg.visible)
            
            is NewTransactionStore.Message.SetCategorySheetVisible -> 
                copy(isCategorySheetVisible = msg.visible)
            
            is NewTransactionStore.Message.SetSaveAsTemplate -> 
                copy(saveAsTemplate = msg.saveAsTemplate)
            
            is NewTransactionStore.Message.SetLoading -> copy(isLoading = msg.isLoading)
            is NewTransactionStore.Message.SetError -> copy(error = msg.error)

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
                    selectedParentId = msg.category?.id,
                    selectedCategory = msg.category,
                )
            )

            is NewTransactionStore.CategoryChooserMessage.NavigateToParent -> copy(
                categoryChooserState = categoryChooserState.copy(
                    selectedParentId = null
                )
            )

            is NewTransactionStore.CategoryChooserMessage.SubCategorySelected -> copy(
                categoryChooserState = categoryChooserState.copy(
                    selectedCategory = msg.category,
                    selectedChildId = msg.category.id,
                    selectedParentId = msg.category.parentId
                )
            )
        }
    }
}
