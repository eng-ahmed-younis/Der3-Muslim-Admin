package com.der3.der3admin.presentation.screens.daily_notification

import android.content.Context
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import com.der3.der3admin.domain.NotificationType
import com.der3.der3admin.domain.repo.StorageRepository
import com.der3.der3admin.domain.use_case.BroadcastToAllUseCase
import com.der3.der3admin.presentation.screens.daily_notification.mvi.*
import com.der3.der3admin.mvi.MviBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DailyPushNotificationViewModel @Inject constructor(
    private val broadcastToAllUseCase: BroadcastToAllUseCase,
    private val storageRepository: StorageRepository,
    @ApplicationContext private val context: Context,
    reducer: DailyNotificationReducer
) : MviBaseViewModel<DailyNotificationState, DailyNotificationAction, DailyNotificationIntent>(
    initialState = DailyNotificationState(),
    reducer = reducer
) {

    override fun handleIntent(intent: DailyNotificationIntent) {
        when (intent) {
            is DailyNotificationIntent.OnTitleChange -> onAction(DailyNotificationAction.SetTitle(intent.title))
            is DailyNotificationIntent.OnMessageChange -> onAction(DailyNotificationAction.SetMessage(intent.message))
            is DailyNotificationIntent.OnImageChange -> {
                onAction(DailyNotificationAction.SetImageUri(intent.uri))
                // Example of getting bitmap from URI if needed
                intent.uri?.let { uri ->
                    try {
                        val bitmap = if (Build.VERSION.SDK_INT < 28) {
                            @Suppress("DEPRECATION")
                            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                        } else {
                            val source = ImageDecoder.createSource(context.contentResolver, uri)
                            ImageDecoder.decodeBitmap(source)
                        }
                        // Use bitmap here (e.g., upload to Firebase)
                    } catch (e: Exception) {
                        onAction(DailyNotificationAction.SetError("Failed to load image: ${e.message}"))
                    }
                }
            }
            DailyNotificationIntent.SendDailyNotification -> sendDailyNotification()
            DailyNotificationIntent.ClearError -> onAction(DailyNotificationAction.SetError(null))
            DailyNotificationIntent.ClearSuccess -> onAction(DailyNotificationAction.SetSuccessMessage(null))
        }
    }

    private fun sendDailyNotification() {
        val title = viewState.title
        val message = viewState.message
        val imageUri = viewState.imageUri

        if (title.isBlank() || message.isBlank()) {
            onAction(DailyNotificationAction.SetError("Title and Message are required"))
            return
        }

        viewModelScope.launch {
            onAction(DailyNotificationAction.SetLoading(true))
            onAction(DailyNotificationAction.SetError(null))

            var imageUrl: String? = null
            if (imageUri != null) {
                val uploadResult = storageRepository.uploadImage(
                    uri = imageUri,
                    path = "notifications/${UUID.randomUUID()}.jpg"
                )
                if (uploadResult.isSuccess) {
                    imageUrl = uploadResult.getOrNull()
                } else {
                    onAction(DailyNotificationAction.SetLoading(false))
                    onAction(DailyNotificationAction.SetError("Failed to upload image: ${uploadResult.exceptionOrNull()?.message}"))
                    return@launch
                }
            }

            broadcastToAllUseCase.sendWithData(
                title = title,
                description = message,
                image = imageUrl,
                type = NotificationType.DAILY
            ).onSuccess {
                onAction(DailyNotificationAction.SetLoading(false))
                onAction(DailyNotificationAction.SetSuccessMessage("Daily notification sent!"))
                onAction(DailyNotificationAction.SetTitle(""))
                onAction(DailyNotificationAction.SetMessage(""))
                onAction(DailyNotificationAction.SetImageUri(null))
            }.onFailure { error ->
                onAction(DailyNotificationAction.SetLoading(false))
                onAction(DailyNotificationAction.SetError("Failed to send: ${error.message}"))
            }
        }
    }
}
