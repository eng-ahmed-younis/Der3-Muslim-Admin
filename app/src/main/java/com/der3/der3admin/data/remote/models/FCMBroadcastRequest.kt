package com.der3.der3admin.data.remote.models

import com.der3.der3admin.domain.NotificationType
import com.google.gson.annotations.SerializedName

data class FCMBroadcastRequest(
    val message: Message
) {
    data class Message(
        val topic: String,
        val notification: Notification,
        val data: Map<String, String>? = null,
        val android: AndroidConfig? = null
    )

    data class Notification(
        val title: String,
        val body: String,
        val image: String? = null
    )

    data class AndroidConfig(
        val priority: String = "high",
        val notification: AndroidNotification
    )

    data class AndroidNotification(
        @SerializedName("click_action")
        val clickAction: String = "OPEN_BROADCAST",
        val sound: String = "default",
        @SerializedName("channel_id")
        val channelId: String = "broadcast_channel"
    )

    companion object {
        fun createBroadcast(
            title: String,
            description: String,
            image: String? = null,
            type: NotificationType = NotificationType.GENERAL,
            additionalData: Map<String, String>? = null
        ): FCMBroadcastRequest {
            val dataPayload = mutableMapOf(
                "type" to type.value
            )
            if (image != null) {
                dataPayload["image"] = image
            }
            additionalData?.let { dataPayload.putAll(it) }

            return FCMBroadcastRequest(
                message = Message(
                    topic = "all",
                    notification = Notification(
                        title = title,
                        body = description,
                        image = image
                    ),
                    data = dataPayload,
                    android = AndroidConfig(
                        priority = "high",
                        notification = AndroidNotification()
                    )
                )
            )
        }
    }
}
