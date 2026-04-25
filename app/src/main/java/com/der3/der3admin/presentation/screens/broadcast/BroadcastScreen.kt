package com.der3.der3admin.presentation.screens.broadcast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.der3.der3admin.domain.models.BroadcastNotification
import com.der3.der3admin.domain.models.BroadcastStatus
import com.der3.der3admin.presentation.components.EmptyState
import com.der3.der3admin.presentation.components.LoadingIndicator
import com.der3.der3admin.presentation.mvi.MviEffect
import com.der3.der3admin.presentation.screens.broadcast.mvi.BroadcastIntent
import com.der3.der3admin.presentation.screens.broadcast.mvi.BroadcastState
import com.der3.der3admin.presentation.screens.broadcast.mvi.TokenStatus
import com.der3.der3admin.presentation.theme.Der3AdminTheme
import com.der3.der3admin.utils.asString
import com.der3.screens.Screens
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun BroadcastRoute(
    onNavigate: (Screens) -> Unit
) {
    val viewModel: BroadcastViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effects.onEach {
            when (it) {
                is MviEffect.Navigate -> onNavigate(it.screen)
                is MviEffect.OnErrorDialog -> {
                    errorMessage = it.error.asString(context)
                    showErrorDialog = true
                }

                else -> {}
            }
        }.launchIn(scope)
    }

    BroadcastScreen(
        state = viewModel.viewState,
        onIntent = viewModel::onIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BroadcastScreen(
    state: BroadcastState,
    onIntent: (BroadcastIntent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle success messages
    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            onIntent(BroadcastIntent.ClearSuccess)
        }
    }

    // Handle errors
    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(
                message = it,
                actionLabel = "Dismiss"
            )
            onIntent(BroadcastIntent.ClearError)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Admin Broadcast Panel")
                        if (state.tokenStatus == TokenStatus.VALID) {
                            Text(
                                text = "Ready to broadcast",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { onIntent(BroadcastIntent.ToggleHistoryDialog) }) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = "History",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = { onIntent(BroadcastIntent.RefreshToken) }) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Refresh Token",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onIntent(BroadcastIntent.ToggleBroadcastDialog) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Send Broadcast",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.broadcasts.isEmpty() && !state.isSending) {
                EmptyState(
                    message = "No broadcasts sent yet",
                    subMessage = "Tap the + button to send your first broadcast"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.broadcasts) { broadcast ->
                        BroadcastCard(broadcast = broadcast)
                    }
                }
            }

            if (state.isSending) {
                LoadingIndicator(message = "Sending broadcast to all users...")
            }
        }
    }

    // Broadcast Dialog
    if (state.showBroadcastDialog) {
        BroadcastDialog(
            onDismiss = { onIntent(BroadcastIntent.ToggleBroadcastDialog) },
            onSend = { title, message, imageUrl, imageUri, type ->
                onIntent(BroadcastIntent.SendBroadcast(title, message, imageUrl, imageUri, type))
            },
            isSending = state.isSending,
            initialTitle = state.title,
            initialMessage = state.message,
            initialImageUrl = state.imageUrl,
            initialImageUri = state.imageUri,
            initialType = state.notificationType,
            onTitleChange = { onIntent(BroadcastIntent.OnTitleChange(it)) },
            onMessageChange = { onIntent(BroadcastIntent.OnMessageChange(it)) },
            onImageUrlChange = { onIntent(BroadcastIntent.OnImageUrlChange(it)) },
            onImageUriChange = {
                onIntent(BroadcastIntent.OnImageUriChange(imageUri = it))
            },
            onTypeChange = { onIntent(BroadcastIntent.OnTypeChange(it)) }
        )
    }

    // History Dialog
    if (state.showHistoryDialog) {
        AlertDialog(
            onDismissRequest = { onIntent(BroadcastIntent.ToggleHistoryDialog) },
            title = { Text("Broadcast History") },
            text = {
                if (state.broadcasts.isEmpty()) {
                    Text("No broadcasts sent yet")
                } else {
                    Column {
                        Text("Last ${state.broadcasts.size} broadcasts:")
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(
                            modifier = Modifier.height(300.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.broadcasts.take(10)) { broadcast ->
                                Text(
                                    text = "${broadcast.formattedDate} - ${broadcast.title}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { onIntent(BroadcastIntent.ToggleHistoryDialog) }) {
                    Text("Close")
                }
            },
            dismissButton = if (state.broadcasts.isNotEmpty()) {
                {
                    TextButton(onClick = { onIntent(BroadcastIntent.ClearHistory) }) {
                        Text("Clear")
                    }
                }
            } else null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BroadcastScreenPreview() {
    Der3AdminTheme {
        BroadcastScreen(
            state = BroadcastState(
                broadcasts = listOf(
                    BroadcastNotification(
                        title = "Preview Notification 1",
                        body = "This is a preview broadcast message.",
                        status = BroadcastStatus.SENT
                    ),
                    BroadcastNotification(
                        title = "Preview Notification 2",
                        body = "This is another preview broadcast message.",
                        status = BroadcastStatus.FAILED
                    )
                ),
                tokenStatus = TokenStatus.VALID
            ),
            onIntent = {}
        )
    }
}
