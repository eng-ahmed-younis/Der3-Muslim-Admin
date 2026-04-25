package com.der3.der3admin.data.remote

import android.content.Context
import com.der3.der3admin.domain.models.TokenResult
import com.google.auth.oauth2.GoogleCredentials
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DataSource responsible for obtaining OAuth2 access tokens for Firebase services.
 * It uses a service account JSON file located in the raw resources to authenticate.
 */
@Singleton
class FirebaseAuthDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Fetches a new access token for Firebase Cloud Messaging.
     * Runs on Dispatchers.IO as it involves file I/O and network requests.
     *
     * @return [Result] containing [TokenResult] on success, or an error on failure.
     */
    suspend fun getAccessToken(): Result<TokenResult> = withContext(Dispatchers.IO) {
        return@withContext try {
            // Load service account from res/raw/service_account.json
            val inputStream: InputStream = context.resources.openRawResource(
                context.resources.getIdentifier(
                    "service_account",
                    "raw",
                    context.packageName
                )
            )

            val credentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(
                    listOf("https://www.googleapis.com/auth/firebase.messaging")
                )

            credentials.refreshIfExpired()

            val accessToken = credentials.accessToken?.tokenValue
                ?: throw Exception("Failed to get access token")

            val expiryTime = credentials.accessToken?.expirationTime?.time
                ?: (System.currentTimeMillis() + 3600000)

            val projectId = extractProjectId(credentials) ?: throw Exception("Failed to get project ID")

            Result.success(
                TokenResult(
                    accessToken = accessToken,
                    expiryTime = expiryTime,
                    projectId = projectId
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Helper method to extract the project ID from GoogleCredentials using reflection.
     */
    private fun extractProjectId(credentials: GoogleCredentials): String? {
        return try {
            val field = credentials.javaClass.getDeclaredField("projectId")
            field.isAccessible = true
            field.get(credentials) as? String
        } catch (e: Exception) {
            null
        }
    }
}