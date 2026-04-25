package com.der3.der3admin.presentation.navigation.screens

import com.der3.screens.Screens
import kotlinx.serialization.Serializable

@Serializable
sealed interface Der3NavigationRoute : Screens {

    @Serializable
    data object HomeScreen : Der3NavigationRoute

    @Serializable
    data object DailyNotificationScreen : Der3NavigationRoute

    @Serializable
    data object NormalNotificationScreen : Der3NavigationRoute

    @Serializable
    data object BroadcastScreen : Der3NavigationRoute

    @Serializable
    data object OtherSettingsScreen : Der3NavigationRoute
}

