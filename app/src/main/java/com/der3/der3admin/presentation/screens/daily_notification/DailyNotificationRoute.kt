package com.der3.der3admin.presentation.screens.daily_notification

import androidx.compose.runtime.Composable

@Composable
fun DailyNotificationRoute(
    onBackClick: () -> Unit
) {
    DailyPushNotificationScreen(
        onBackClick = onBackClick
    )
}
