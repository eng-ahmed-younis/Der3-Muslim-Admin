package com.der3.der3admin.presentation.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import com.der3.der3admin.domain.HomeMenuItem
import com.der3.der3admin.domain.NotificationItemType
import com.der3.der3admin.mvi.MviBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import com.der3.der3admin.mvi.MviEffect
import com.der3.der3admin.presentation.navigation.screens.Der3NavigationRoute
import com.der3.der3admin.presentation.screens.home.mvi.HomeAction
import com.der3.der3admin.presentation.screens.home.mvi.HomeIntent
import com.der3.der3admin.presentation.screens.home.mvi.HomeReducer
import com.der3.der3admin.presentation.screens.home.mvi.HomeState

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    homeReducer: HomeReducer
) : MviBaseViewModel<HomeState, HomeAction, HomeIntent>(
    initialState = HomeState(),
    reducer = homeReducer
) {

    init {
        onAction(action = HomeAction.SetMenuItems(menuItems = menuItems()))
    }


    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.OnMenuItemClick -> {
                when(intent.type){
                    NotificationItemType.DailyNotification ->{
                        onEffect(MviEffect.Navigate(Der3NavigationRoute.DailyNotificationScreen))
                    }
                    NotificationItemType.NormalNotification ->{
                        onEffect(MviEffect.Navigate(Der3NavigationRoute.NormalNotificationScreen))
                    }
                    NotificationItemType.BroadcastNotification -> {
                        onEffect(MviEffect.Navigate(Der3NavigationRoute.BroadcastScreen))
                    }
                    else -> {}
                }
            }
        }
    }


    private fun menuItems(): List<HomeMenuItem> = listOf(
        HomeMenuItem(
            type = NotificationItemType.DailyNotification,
            title = "Daily Push Notification",
            icon = Icons.Default.Schedule,
        ),
        HomeMenuItem(
            type = NotificationItemType.NormalNotification,
            title = "Normal Notification",
            icon = Icons.Default.Notifications,
        ),
        HomeMenuItem(
            type = NotificationItemType.BroadcastNotification,
            title = "Broadcast Notification",
            icon = Icons.Default.Notifications,
        ),
        HomeMenuItem(
            type = NotificationItemType.OtherSettings,
            title = "Other Settings",
            icon = Icons.Default.Settings,
        )
    )

}
