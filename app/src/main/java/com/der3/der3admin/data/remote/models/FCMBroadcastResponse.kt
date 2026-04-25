package com.der3.der3admin.data.remote.models

import com.google.gson.annotations.SerializedName

data class FCMBroadcastResponse(
    @SerializedName("name")
    val messageId: String?
)

data class FCMErrorResponse(
    val error: ErrorDetail
)

data class ErrorDetail(
    val code: Int,
    val message: String,
    val status: String
)