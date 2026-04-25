package com.der3.der3admin.presentation.screens.broadcast.mvi

import android.net.Uri
import com.der3.der3admin.domain.NotificationType
import com.der3.der3admin.domain.models.BroadcastNotification
import com.der3.der3admin.mvi.MviState

data class BroadcastState(
    val title: String = "",
    val message: String = "",
    val imageUrl: String = "",
    val imageUri: Uri? = null,
    val notificationType: NotificationType = NotificationType.GENERAL,
    val broadcasts: List<BroadcastNotification> = emptyList(),
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val showBroadcastDialog: Boolean = false,
    val showHistoryDialog: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val tokenStatus: TokenStatus = TokenStatus.IDLE
) : MviState

enum class TokenStatus {
    IDLE,
    REFRESHING,
    VALID,
    EXPIRED
}
