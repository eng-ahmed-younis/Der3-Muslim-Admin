package com.der3.der3admin.data.repo

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import com.der3.der3admin.data.remote.FCMApi
import com.der3.der3admin.data.remote.models.FCMBroadcastRequest
import com.der3.der3admin.domain.NotificationType
import com.der3.der3admin.domain.models.BroadcastNotification
import com.der3.der3admin.domain.models.BroadcastStatus
import com.der3.der3admin.domain.repo.BroadcastRepository
import com.der3.der3admin.domain.repo.TokenRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.filter

/**
 * Implementation of [BroadcastRepository] that handles sending FCM broadcasts
 * and maintaining a local history of sent broadcasts.
 */
@Singleton
class BroadcastRepositoryImpl @Inject constructor(
    private val fcmApi: FCMApi,
    private val tokenRepository: TokenRepository
) : BroadcastRepository {

    private val TAG = "BroadcastRepository"
    
    /**
     * Internal state flow holding the list of broadcast notifications.
     */
    private val _broadcasts = MutableStateFlow<List<BroadcastNotification>>(emptyList())

    /**
     * Returns a flow of the broadcast history.
     */
    override fun getBroadcastHistory(): Flow<List<BroadcastNotification>> =
        _broadcasts.asStateFlow()

    /**
     * Sends a broadcast notification to all users via FCM.
     * 
     * This method:
     * 1. Creates a pending [BroadcastNotification].
     * 2. Checks and refreshes the auth token if necessary.
     * 3. Sends the request via [FCMApi].
     * 4. Updates the local history with the result (success or failure).
     *
     * @param title The title of the notification.
     * @param description The body text of the notification.
     * @param additionalData Optional map of data to include in the FCM payload.
     * @return [Result] containing the final [BroadcastNotification] state.
     */
    override suspend fun sendBroadcastToAll(
        title: String,
        description: String,
        image: String?,
        type: NotificationType,
        additionalData: Map<String, String>?
    ): Result<BroadcastNotification> {
        Log.d(TAG, "Sending broadcast to ALL users: $title")

        // Create pending notification
        val pendingBroadcast = BroadcastNotification(
            id = UUID.randomUUID().toString(),
            title = title,
            body = description,
            status = com.der3.der3admin.domain.models.BroadcastStatus.PENDING
        )

        // Add to list immediately
        _broadcasts.value = listOf(pendingBroadcast) + _broadcasts.value

        return try {
            // Check token
            if (!tokenRepository.isValidToken()) {
                Log.d(TAG, "Token invalid, refreshing...")
                val refreshResult = tokenRepository.refreshToken()
                if (refreshResult.isFailure) {
                    throw Exception("Failed to refresh authentication token")
                }
            }

            val tokenResult = tokenRepository.getToken().firstOrNull()
                ?: throw Exception("No valid token available")

            // Create and send request
            val request = FCMBroadcastRequest.createBroadcast(
                title = title,
                description = description,
                type = type,
                image = image,
                additionalData = additionalData
            )

            val response = fcmApi.sendBroadcast(
                projectId = tokenResult.projectId,
                authorization = "Bearer ${tokenResult.accessToken}",
                request = request
            )

            val finalBroadcast = if (response.isSuccessful) {
                val messageId = response.body()?.messageId
                Log.d(TAG, "Broadcast sent successfully. Message ID: $messageId")

                pendingBroadcast.copy(
                    status = BroadcastStatus.SENT,
                    messageId = messageId
                )
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Broadcast failed: ${response.code()} - $errorBody")

                pendingBroadcast.copy(
                    status = BroadcastStatus.FAILED,
                    errorMessage = "Error ${response.code()}"
                )
            }

            // Update the broadcast
            _broadcasts.value = _broadcasts.value.map {
                if (it.id == pendingBroadcast.id) finalBroadcast else it
            }

            Result.success(finalBroadcast)

        } catch (e: Exception) {
            Log.e(TAG, "Exception during broadcast", e)

            val failedBroadcast = pendingBroadcast.copy(
                status = BroadcastStatus.FAILED,
                errorMessage = e.message
            )

            _broadcasts.value = _broadcasts.value.map {
                if (it.id == pendingBroadcast.id) failedBroadcast else it
            }

            Result.failure(e)
        }
    }

    /**
     * Clears all broadcast notifications from the history.
     */
    override suspend fun clearHistory() {
        _broadcasts.value = emptyList()
    }

    /**
     * Deletes a specific broadcast notification by its ID.
     *
     * @param id The unique identifier of the broadcast to delete.
     */
    override suspend fun deleteBroadcast(id: String) {
        _broadcasts.value = _broadcasts.value.filter { it.id != id }
    }
}