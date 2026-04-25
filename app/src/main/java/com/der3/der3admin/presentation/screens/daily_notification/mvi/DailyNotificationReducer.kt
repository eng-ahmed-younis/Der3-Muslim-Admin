package com.der3.der3admin.presentation.screens.daily_notification.mvi

import com.der3.der3admin.mvi.Reducer
import javax.inject.Inject

class DailyNotificationReducer @Inject constructor() : Reducer<DailyNotificationAction, DailyNotificationState> {
    override fun reduce(action: DailyNotificationAction, state: DailyNotificationState): DailyNotificationState {
        return when (action) {
            is DailyNotificationAction.SetTitle -> state.copy(title = action.title)
            is DailyNotificationAction.SetMessage -> state.copy(message = action.message)
            is DailyNotificationAction.SetImageUri -> state.copy(imageUri = action.imageUri)
            is DailyNotificationAction.SetLoading -> state.copy(isLoading = action.isLoading)
            is DailyNotificationAction.SetSuccessMessage -> state.copy(successMessage = action.message)
            is DailyNotificationAction.SetError -> state.copy(error = action.error)
        }
    }
}
