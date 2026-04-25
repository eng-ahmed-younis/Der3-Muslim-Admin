package com.der3.der3admin.presentation.screens.daily_notification.mvi

import android.net.Uri
import com.der3.der3admin.mvi.MviState

data class DailyNotificationState(
    val title: String = "",
    val message: String = "",
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null
) : MviState
