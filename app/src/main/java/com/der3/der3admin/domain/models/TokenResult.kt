package com.der3.der3admin.domain.models

data class TokenResult(
    val accessToken: String,
    val expiryTime: Long,
    val projectId: String
) {
    val isValid: Boolean
        get() = System.currentTimeMillis() < (expiryTime - 300000) // 5 min buffer

    val expiresInMinutes: Long
        get() = (expiryTime - System.currentTimeMillis()) / 60000
}

