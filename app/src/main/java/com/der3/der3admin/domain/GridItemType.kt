package com.der3.der3admin.domain

interface GridItemType

sealed interface NotificationItemType : GridItemType {
    data object DailyNotification : NotificationItemType
    data object NormalNotification : NotificationItemType
    data object BroadcastNotification : NotificationItemType
    data object OtherSettings : NotificationItemType
}