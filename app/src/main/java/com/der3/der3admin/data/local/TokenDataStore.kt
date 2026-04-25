package com.der3.der3admin.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.der3.der3admin.domain.models.TokenResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_prefs")

/**
 * DataStore implementation for persisting authentication tokens and related metadata.
 * Uses Jetpack DataStore Preferences for lightweight local storage.
 */
@Singleton
class TokenDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val TOKEN_EXPIRY_KEY = stringPreferencesKey("token_expiry")
        private val PROJECT_ID_KEY = stringPreferencesKey("project_id")
    }

    /**
     * Flow that emits the current [TokenResult] or null if no token is stored.
     */
    val tokenData: Flow<TokenResult?> = context.dataStore.data
        .map { preferences ->
            val token = preferences[ACCESS_TOKEN_KEY] ?: return@map null
            val expiry = preferences[TOKEN_EXPIRY_KEY]?.toLongOrNull() ?: return@map null
            val projectId = preferences[PROJECT_ID_KEY] ?: return@map null

            TokenResult(
                accessToken = token,
                expiryTime = expiry,
                projectId = projectId
            )
        }

    /**
     * Saves the [TokenResult] to persistent storage.
     */
    suspend fun saveToken(tokenResult: TokenResult) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = tokenResult.accessToken
            preferences[TOKEN_EXPIRY_KEY] = tokenResult.expiryTime.toString()
            preferences[PROJECT_ID_KEY] = tokenResult.projectId
        }
    }

    /**
     * Clears all stored token information.
     */
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(TOKEN_EXPIRY_KEY)
            preferences.remove(PROJECT_ID_KEY)
        }
    }

    /**
     * Checks if the current token is valid and not close to expiration.
     * Includes a 5-minute buffer.
     */
    suspend fun isValidToken(): Boolean {
        val prefs = context.dataStore.data.firstOrNull() ?: return false
        val expiry = prefs[TOKEN_EXPIRY_KEY]?.toLongOrNull() ?: return false
        return System.currentTimeMillis() < (expiry - 300000) // 5 minutes buffer
    }
}