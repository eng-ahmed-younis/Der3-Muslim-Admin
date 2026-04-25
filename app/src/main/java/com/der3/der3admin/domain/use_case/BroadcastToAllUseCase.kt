package com.der3.der3admin.domain.use_case

import com.der3.der3admin.domain.NotificationType
import com.der3.der3admin.domain.models.BroadcastNotification
import com.der3.der3admin.domain.repo.BroadcastRepository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BroadcastToAllUseCase @Inject constructor(
    private val broadcastRepository: BroadcastRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        image: String?,
        type: NotificationType,
    ): Result<BroadcastNotification> {
        return when {
            title.isBlank() -> Result.failure(IllegalArgumentException("Title cannot be empty"))
            description.isBlank() -> Result.failure(IllegalArgumentException("Message cannot be empty"))
            else -> broadcastRepository.sendBroadcastToAll(
                title = title,
                description = description,
                image = image,
                type = type
            )
        }
    }

    suspend fun sendWithData(
        title: String,
        description: String,
        image: String?,
        type: NotificationType = NotificationType.GENERAL,
        data: Map<String, String> = emptyMap()
    ): Result<BroadcastNotification> {
        return broadcastRepository.sendBroadcastToAll(
            title = title,
            description = description,
            image = image,
            type = type,
            additionalData = data
        )
    }
}
