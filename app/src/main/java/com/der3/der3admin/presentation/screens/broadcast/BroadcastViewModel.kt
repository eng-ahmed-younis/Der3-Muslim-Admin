package com.der3.der3admin.presentation.screens.broadcast

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.der3.der3admin.domain.NotificationType
import com.der3.der3admin.domain.use_case.BroadcastToAllUseCase
import com.der3.der3admin.domain.use_case.GetBroadcastHistoryUseCase
import com.der3.der3admin.domain.use_case.RefreshTokenUseCase
import com.der3.der3admin.presentation.mvi.MviBaseViewModel
import com.der3.der3admin.presentation.screens.broadcast.mvi.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class BroadcastViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val broadcastToAllUseCase: BroadcastToAllUseCase,
    private val getBroadcastHistoryUseCase: GetBroadcastHistoryUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    reducer: BroadcastReducer
) : MviBaseViewModel<BroadcastState, BroadcastAction, BroadcastIntent>(
    initialState = BroadcastState(),
    reducer = reducer
) {

    init {
        onIntent(BroadcastIntent.LoadBroadcasts)
        onIntent(BroadcastIntent.RefreshToken)
    }

    override fun handleIntent(intent: BroadcastIntent) {
        when (intent) {
            is BroadcastIntent.SendBroadcast -> sendBroadcast(
                intent.title,
                intent.message,
                intent.imageUri,
                intent.type
            )

            BroadcastIntent.ToggleBroadcastDialog -> onAction(
                BroadcastAction.SetShowBroadcastDialog(
                    !viewState.showBroadcastDialog
                )
            )

            BroadcastIntent.ToggleHistoryDialog -> onAction(BroadcastAction.SetShowHistoryDialog(!viewState.showHistoryDialog))
            BroadcastIntent.RefreshToken -> refreshToken()
            BroadcastIntent.ClearHistory -> clearHistory()
            is BroadcastIntent.DeleteBroadcast -> deleteBroadcast(intent.id)
            BroadcastIntent.ClearError -> onAction(BroadcastAction.SetError(null))
            BroadcastIntent.ClearSuccess -> onAction(BroadcastAction.SetSuccessMessage(null))
            BroadcastIntent.LoadBroadcasts -> observeBroadcasts()
            is BroadcastIntent.OnTitleChange -> onAction(BroadcastAction.SetTitle(intent.title))
            is BroadcastIntent.OnMessageChange -> onAction(BroadcastAction.SetMessage(intent.message))
            is BroadcastIntent.OnImageUrlChange -> onAction(BroadcastAction.SetImageUrl(intent.imageUrl))
            is BroadcastIntent.OnImageUriChange -> {
                onAction(BroadcastAction.SetImageUri(intent.imageUri))
            }

            is BroadcastIntent.OnTypeChange -> onAction(BroadcastAction.SetType(intent.type))
        }
    }

    private fun observeBroadcasts() {
        getBroadcastHistoryUseCase()
            .onEach { broadcasts ->
                onAction(BroadcastAction.UpdateBroadcasts(broadcasts))
            }
            .launchIn(viewModelScope)
    }

    @SuppressLint("UseKtx")
    private fun sendBroadcast(
        title: String,
        description: String,
        imageUri: Uri?,
        type: NotificationType
    ) {
        viewModelScope.launch(Dispatchers.Main) {  // Explicit Main dispatcher
            onAction(BroadcastAction.SetSending(true))
            onAction(BroadcastAction.SetError(null))

            // Process image completely on IO thread
            val imageBase64 = if (imageUri != null) {
                withContext(Dispatchers.IO) {
                    try {
                        // Load bitmap with inSampleSize for memory efficiency
                        val bitmap = runCatching {
                            if (Build.VERSION.SDK_INT < 28) {
                                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                            } else {
                                val source = ImageDecoder.createSource(context.contentResolver, imageUri)
                                ImageDecoder.decodeBitmap(source)
                            }
                        }.getOrNull()

                        bitmap?.let { bmp ->
                            // Resize to max 800px
                            val maxSize = 800
                            val finalBitmap = if (bmp.width > maxSize || bmp.height > maxSize) {
                                val scale = minOf(maxSize.toFloat() / bmp.width, maxSize.toFloat() / bmp.height)
                                Bitmap.createScaledBitmap(
                                    bmp,
                                    (bmp.width * scale).toInt(),
                                    (bmp.height * scale).toInt(),
                                    true
                                ).also {
                                    // Recycle original if different
                                    if (it != bmp) bmp.recycle()
                                }
                            } else {
                                bmp
                            }

                            // Convert to JPEG Base64
                            val outputStream = ByteArrayOutputStream()
                            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                            val result = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)

                            finalBitmap.recycle()
                            result
                        }
                    } catch (e: Exception) {
                        Log.e("Broadcast", "Image processing failed", e)
                        null
                    }
                }
            } else {
                null
            }

            val messageData = MessageData(
                message = description,
                type = type.value,
                image = imageBase64
            )

            val json = Json.encodeToString(messageData)

            broadcastToAllUseCase(
                title = title,
                description = description,
                image = imageBase64,
                type = type
            ).onSuccess {
                onAction(BroadcastAction.SetSending(false))
                onAction(BroadcastAction.SetShowBroadcastDialog(false))
                onAction(BroadcastAction.SetSuccessMessage("Broadcast sent to all users!"))
            }.onFailure { error ->
                onAction(BroadcastAction.SetSending(false))
                onAction(BroadcastAction.SetError("Failed: ${error.message}"))
            }
        }
    }

    private fun refreshToken() {
        viewModelScope.launch {
            onAction(BroadcastAction.SetLoading(true))
            onAction(BroadcastAction.SetTokenStatus(TokenStatus.REFRESHING))

            refreshTokenUseCase()
                .onSuccess {
                    onAction(BroadcastAction.SetLoading(false))
                    onAction(BroadcastAction.SetTokenStatus(TokenStatus.VALID))
                    onAction(BroadcastAction.SetSuccessMessage("Token ready"))
                }
                .onFailure { error ->
                    onAction(BroadcastAction.SetLoading(false))
                    onAction(BroadcastAction.SetTokenStatus(TokenStatus.EXPIRED))
                    onAction(BroadcastAction.SetError("Token refresh failed"))
                }
        }
    }

    private fun clearHistory() {
        viewModelScope.launch {
            onAction(BroadcastAction.SetSuccessMessage("History cleared"))
        }
    }

    private fun deleteBroadcast(id: String) {
        viewModelScope.launch {
        }
    }

}

@Serializable
data class MessageData(
    val message: String,
    val type: String,
    val image: String? = null
)