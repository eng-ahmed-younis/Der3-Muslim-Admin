package com.der3.der3admin.presentation.screens.broadcast.mvi

import android.net.Uri
import com.der3.der3admin.domain.NotificationType
import com.der3.der3admin.mvi.MviIntent

sealed class BroadcastIntent : MviIntent {
    data object LoadBroadcasts : BroadcastIntent()
    data class SendBroadcast(
        val title: String,
        val message: String,
        val imageUrl: String,
        val imageUri: Uri?,
        val type: NotificationType
    ) : BroadcastIntent()
    data object RefreshToken : BroadcastIntent()
    data object ToggleBroadcastDialog : BroadcastIntent()
    data object ToggleHistoryDialog : BroadcastIntent()
    data object ClearHistory : BroadcastIntent()
    data class DeleteBroadcast(val id: String) : BroadcastIntent()
    data object ClearError : BroadcastIntent()
    data object ClearSuccess : BroadcastIntent()

    data class OnTitleChange(val title: String) : BroadcastIntent()
    data class OnMessageChange(val message: String) : BroadcastIntent()
    data class OnImageUrlChange(val imageUrl: String) : BroadcastIntent()
    data class OnImageUriChange(val imageUri: Uri?) : BroadcastIntent()
    data class OnTypeChange(val type: NotificationType) : BroadcastIntent()
}
