package com.der3.der3admin.presentation.screens.broadcast.mvi

import android.net.Uri
import com.der3.der3admin.domain.NotificationType
import com.der3.der3admin.domain.models.BroadcastNotification
import com.der3.der3admin.mvi.MviAction

sealed class BroadcastAction : MviAction {
    data class UpdateBroadcasts(val broadcasts: List<BroadcastNotification>) : BroadcastAction()
    data class SetLoading(val isLoading: Boolean) : BroadcastAction()
    data class SetSending(val isSending: Boolean) : BroadcastAction()
    data class SetShowBroadcastDialog(val show: Boolean) : BroadcastAction()
    data class SetShowHistoryDialog(val show: Boolean) : BroadcastAction()
    data class SetError(val error: String?) : BroadcastAction()
    data class SetSuccessMessage(val message: String?) : BroadcastAction()
    data class SetTokenStatus(val status: TokenStatus) : BroadcastAction()

    data class SetTitle(val title: String) : BroadcastAction()
    data class SetMessage(val message: String) : BroadcastAction()
    data class SetImageUrl(val imageUrl: String) : BroadcastAction()
    data class SetImageUri(val imageUri: Uri?) : BroadcastAction()
    data class SetType(val type: NotificationType) : BroadcastAction()
}
