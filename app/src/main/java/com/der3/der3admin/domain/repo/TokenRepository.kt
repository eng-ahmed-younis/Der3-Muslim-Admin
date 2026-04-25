package com.der3.der3admin.domain.repo

import com.der3.der3admin.domain.models.TokenResult
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    /**
     * Get current token flow
     */
    fun getToken(): Flow<TokenResult?>

    /**
     * Refresh FCM token
     */
    suspend fun refreshToken(): Result<TokenResult>

    /**
     * Check if current token is valid
     */
    suspend fun isValidToken(): Boolean

    /**
     * Clear stored token
     */
    suspend fun clearToken()
}