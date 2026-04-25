package com.der3.der3admin.presentation.screens.daily_notification

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.der3.der3admin.presentation.components.Der3TopAppBar
import com.der3.der3admin.presentation.screens.daily_notification.mvi.DailyNotificationIntent
import com.der3.ui.themes.AppColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyPushNotificationScreen(
    onBackClick: () -> Unit,
    viewModel: DailyPushNotificationViewModel = hiltViewModel()
) {
    val state = viewModel.viewState
    val scrollState = rememberScrollState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        viewModel.onIntent(DailyNotificationIntent.OnImageChange(uri))
    }

    Scaffold(
        topBar = {
            Der3TopAppBar(
                title = "Daily Notification",
                onBackClick = onBackClick,
                backgroundColor = AppColors.white
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = state.title,
                    onValueChange = { viewModel.onIntent(DailyNotificationIntent.OnTitleChange(it)) },
                    label = { Text("Title") },
                    placeholder = { Text("Enter notification title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.message,
                    onValueChange = { viewModel.onIntent(DailyNotificationIntent.OnMessageChange(it)) },
                    label = { Text("Message") },
                    placeholder = { Text("Enter notification message") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Text(
                    text = "Image (Optional)",
                    style = MaterialTheme.typography.labelLarge
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (state.imageUri != null) {
                        AsyncImage(
                            model = state.imageUri,
                            contentDescription = "Selected image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { viewModel.onIntent(DailyNotificationIntent.OnImageChange(null)) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remove image",
                                tint = Color.White
                            )
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text("Tap to select from Gallery")
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { viewModel.onIntent(DailyNotificationIntent.SendDailyNotification) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !state.isLoading && state.title.isNotBlank() && state.message.isNotBlank()
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Send Notification")
                    }
                }
            }
        }
    }

    // Handle feedback effects (e.g., using a SnackbarHost or simple LaunchedEffect)
    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            // Show success feedback
            viewModel.onIntent(DailyNotificationIntent.ClearSuccess)
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            // Show error feedback
            viewModel.onIntent(DailyNotificationIntent.ClearError)
        }
    }
}
