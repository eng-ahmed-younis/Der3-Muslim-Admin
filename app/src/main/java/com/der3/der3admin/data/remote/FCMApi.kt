package com.der3.der3admin.data.remote



import com.der3.der3admin.data.remote.models.FCMBroadcastRequest
import com.der3.der3admin.data.remote.models.FCMBroadcastResponse
import retrofit2.Response
import retrofit2.http.*

interface FCMApi {
    /**
     * Send broadcast notification to all users via topic
     */
    @POST("v1/projects/{projectId}/messages:send")
    suspend fun sendBroadcast(
        @Path("projectId") projectId: String,
        @Header("Authorization") authorization: String,
        @Body request: FCMBroadcastRequest
    ): Response<FCMBroadcastResponse>
}