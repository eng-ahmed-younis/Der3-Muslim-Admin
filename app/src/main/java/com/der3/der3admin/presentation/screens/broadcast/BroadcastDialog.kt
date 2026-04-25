package com.der3.der3admin.presentation.screens.broadcast

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.der3.der3admin.domain.NotificationType
import com.der3.ui.themes.Der3AdminTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BroadcastDialog(
    onDismiss: () -> Unit,
    onSend: (String, String, String, Uri?, NotificationType) -> Unit,
    isSending: Boolean = false,
    initialTitle: String = "",
    initialMessage: String = "",
    initialImageUrl: String = "",
    initialImageUri: Uri? = null,
    initialType: NotificationType = NotificationType.GENERAL,
    onTitleChange: (String) -> Unit = {},
    onMessageChange: (String) -> Unit = {},
    onImageUrlChange: (String) -> Unit = {},
    onImageUriChange: (Uri?) -> Unit = {},
    onTypeChange: (NotificationType) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        onImageUriChange(uri)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Broadcast Notification",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select type and fill details",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Type Selector
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = initialType.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Notification Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        enabled = !isSending
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        NotificationType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name) },
                                onClick = {
                                    onTypeChange(type)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = initialTitle,
                    onValueChange = onTitleChange,
                    label = { Text("Title") },
                    placeholder = { Text("required field") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSending,
                    singleLine = true
                )

                OutlinedTextField(
                    value = initialMessage,
                    onValueChange = onMessageChange,
                    label = { Text("Message") },
                    placeholder = { Text("Type your message here...") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSending,
                    minLines = 3
                )

                Text(
                    text = "Image (Optional)",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable(!isSending) {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (initialImageUri != null) {
                        AsyncImage(
                            model = initialImageUri,
                            contentDescription = "Selected image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { onImageUriChange(null) },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                Icons.Default.Info, // Should be close but using Info as placeholder
                                contentDescription = "Remove image",
                                tint = Color.Red
                            )
                        }
                    } else if (initialImageUrl.isNotBlank()) {
                        AsyncImage(
                            model = initialImageUrl,
                            contentDescription = "Image from URL",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
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
                            Text(
                                "Tap to select an image",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = initialImageUrl,
                    onValueChange = onImageUrlChange,
                    label = { Text("Or provide Image URL") },
                    placeholder = { Text("https://example.com/image.png") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSending && initialImageUri == null,
                    singleLine = true
                )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "This notification will be sent to all users.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSend(initialTitle, initialMessage, initialImageUrl, initialImageUri, initialType) },
                enabled = initialTitle.isNotBlank() && initialMessage.isNotBlank() && !isSending
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Send Broadcast")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isSending
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BroadcastDialogPreview() {
    Der3AdminTheme {
        BroadcastDialog(
            onDismiss = {},
            onSend = { _, _, _, _, _ -> }
        )
    }
}
