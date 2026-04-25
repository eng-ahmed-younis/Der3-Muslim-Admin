package com.der3.der3admin.presentation.screens.daily_notification.mvi

import android.net.Uri
import com.der3.der3admin.presentation.mvi.MviAction

sealed class DailyNotificationAction : MviAction {
    data class SetTitle(val title: String) : DailyNotificationAction()
    data class SetMessage(val message: String) : DailyNotificationAction()
    data class SetImageUri(val imageUri: Uri?) : DailyNotificationAction()
    data class SetLoading(val isLoading: Boolean) : DailyNotificationAction()
    data class SetSuccessMessage(val message: String?) : DailyNotificationAction()
    data class SetError(val error: String?) : DailyNotificationAction()
}
