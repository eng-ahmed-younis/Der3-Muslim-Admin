package com.der3.der3admin.domain.repo

import com.der3.der3admin.domain.NotificationType
import com.der3.der3admin.domain.models.BroadcastNotification
import kotlinx.coroutines.flow.Flow

interface BroadcastRepository {
    fun getBroadcastHistory(): Flow<List<BroadcastNotification>>
    suspend fun sendBroadcastToAll(
        title: String,
        description: String,
        image: String?,
        type: NotificationType = NotificationType.GENERAL,
        additionalData: Map<String, String>? = null
    ): Result<BroadcastNotification>
    suspend fun clearHistory()
    suspend fun deleteBroadcast(id: String)
}