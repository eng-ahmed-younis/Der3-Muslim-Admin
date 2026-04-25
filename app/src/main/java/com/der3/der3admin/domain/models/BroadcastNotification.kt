package com.der3.der3admin.domain.models

import com.der3.der3admin.domain.NotificationType
import java.text.SimpleDateFormat
import java.util.*

data class BroadcastStats(
    val totalBroadcasts: Int = 0,
    val successfulBroadcasts: Int = 0,
    val failedBroadcasts: Int = 0,
    val lastBroadcastTime: Long? = null
)


data class BroadcastNotification(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val body: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: BroadcastStatus = BroadcastStatus.PENDING,
    val topic: String = "all",
    val messageId: String? = null,
    val errorMessage: String? = null
) {
    val formattedTime: String
        get() = try {
            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
        } catch (e: Exception) {
            timestamp.toString()
        }

    val formattedDate: String
        get() = try {
            SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(timestamp))
        } catch (e: Exception) {
            timestamp.toString()
        }

    val statusDisplay: String
        get() = when (status) {
            BroadcastStatus.SENT -> "Broadcast Sent"
            BroadcastStatus.FAILED -> "Broadcast Failed"
            BroadcastStatus.PENDING -> "Sending..."
        }
}

enum class BroadcastStatus {
    SENT, FAILED, PENDING;

    val displayName: String
        get() = when (this) {
            SENT -> "Sent to All Users"
            FAILED -> "Failed"
            PENDING -> "Sending..."
        }

    val icon: String
        get() = when (this) {
            SENT -> "✓"
            FAILED -> "✗"
            PENDING -> "⋯"
        }
}