package com.der3.der3admin.presentation.screens.broadcast.mvi

import com.der3.der3admin.mvi.Reducer
import javax.inject.Inject

class BroadcastReducer @Inject constructor() : Reducer<BroadcastAction, BroadcastState> {
    override fun reduce(action: BroadcastAction, state: BroadcastState): BroadcastState {
        return when (action) {
            is BroadcastAction.UpdateBroadcasts -> state.copy(broadcasts = action.broadcasts)
            is BroadcastAction.SetLoading -> state.copy(isLoading = action.isLoading)
            is BroadcastAction.SetSending -> state.copy(isSending = action.isSending)
            is BroadcastAction.SetError -> state.copy(error = action.error)
            is BroadcastAction.SetSuccessMessage -> state.copy(successMessage = action.message)
            is BroadcastAction.SetTokenStatus -> state.copy(tokenStatus = action.status)
            is BroadcastAction.SetShowBroadcastDialog -> state.copy(showBroadcastDialog = action.show)
            is BroadcastAction.SetShowHistoryDialog -> state.copy(showHistoryDialog = action.show)
            is BroadcastAction.SetTitle -> state.copy(title = action.title)
            is BroadcastAction.SetMessage -> state.copy(message = action.message)
            is BroadcastAction.SetImageUrl -> state.copy(imageUrl = action.imageUrl)
            is BroadcastAction.SetImageUri -> state.copy(imageUri = action.imageUri)
            is BroadcastAction.SetType -> state.copy(notificationType = action.type)
        }
    }
}
