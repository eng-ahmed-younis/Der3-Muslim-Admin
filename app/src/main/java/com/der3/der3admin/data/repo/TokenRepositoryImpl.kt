package com.der3.der3admin.data.repo


import com.der3.der3admin.data.local.TokenDataStore
import com.der3.der3admin.data.remote.FirebaseAuthDataSource
import com.der3.der3admin.domain.models.TokenResult
import com.der3.der3admin.domain.repo.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [TokenRepository] that coordinates between [TokenDataStore] for persistence
 * and [FirebaseAuthDataSource] for fetching new tokens.
 */
@Singleton
class TokenRepositoryImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val authDataSource: FirebaseAuthDataSource
) : TokenRepository {

    /**
     * Returns a flow of the current token data.
     */
    override fun getToken(): Flow<TokenResult?> = tokenDataStore.tokenData

    /**
     * Refreshes the access token using the auth data source and saves it if successful.
     */
    override suspend fun refreshToken(): Result<TokenResult> {
        return try {
            val result = authDataSource.getAccessToken()
            result.onSuccess { tokenResult ->
                tokenDataStore.saveToken(tokenResult)
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Checks if the current cached token is still valid.
     */
    override suspend fun isValidToken(): Boolean {
        return tokenDataStore.isValidToken()
    }

    /**
     * Removes the token from local storage.
     */
    override suspend fun clearToken() {
        tokenDataStore.clearToken()
    }
}