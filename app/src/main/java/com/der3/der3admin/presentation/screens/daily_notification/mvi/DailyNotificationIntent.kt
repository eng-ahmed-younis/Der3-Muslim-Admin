package com.der3.der3admin.presentation.screens.daily_notification.mvi

import android.net.Uri
import com.der3.der3admin.presentation.mvi.MviIntent

sealed class DailyNotificationIntent : MviIntent {
    data class OnTitleChange(val title: String) : DailyNotificationIntent()
    data class OnMessageChange(val message: String) : DailyNotificationIntent()
    data class OnImageChange(val uri: Uri?) : DailyNotificationIntent()
    data object SendDailyNotification : DailyNotificationIntent()
    data object ClearError : DailyNotificationIntent()
    data object ClearSuccess : DailyNotificationIntent()
}
