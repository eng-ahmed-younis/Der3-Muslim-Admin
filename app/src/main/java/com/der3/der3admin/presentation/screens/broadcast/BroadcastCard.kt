package com.der3.der3admin.presentation.screens.broadcast


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.der3.der3admin.domain.models.BroadcastNotification
import com.der3.der3admin.domain.models.BroadcastStatus
import com.der3.der3admin.presentation.theme.Der3AdminTheme


@Composable
fun BroadcastCard(
    broadcast: BroadcastNotification,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (broadcast.status) {
                BroadcastStatus.SENT -> MaterialTheme.colorScheme.primaryContainer
                BroadcastStatus.FAILED -> MaterialTheme.colorScheme.errorContainer
                BroadcastStatus.PENDING -> MaterialTheme.colorScheme.tertiaryContainer
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Icon
            Icon(
                imageVector = when (broadcast.status) {
                    BroadcastStatus.SENT -> Icons.Default.CheckCircle
                    BroadcastStatus.FAILED -> Icons.Default.Error
                    BroadcastStatus.PENDING -> Icons.Default.HourglassEmpty
                },
                contentDescription = null,
                tint = when (broadcast.status) {
                    BroadcastStatus.SENT -> MaterialTheme.colorScheme.primary
                    BroadcastStatus.FAILED -> MaterialTheme.colorScheme.error
                    BroadcastStatus.PENDING -> MaterialTheme.colorScheme.tertiary
                },
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = broadcast.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = broadcast.body,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = broadcast.formattedTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    StatusBadge(status = broadcast.status)
                }
            }

            // Broadcast indicator
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "ALL",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
fun BroadcastCardPreview() {
    Der3AdminTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BroadcastCard(
                broadcast = BroadcastNotification(
                    title = "Daily Prayer Reminder",
                    body = "Don't forget your daily prayers. Allah is always with you.",
                    status = BroadcastStatus.SENT
                )
            )
            BroadcastCard(
                broadcast = BroadcastNotification(
                    title = "Friday Sermon Update",
                    body = "Join us this Friday for an inspiring sermon on community unity.",
                    status = BroadcastStatus.PENDING
                )
            )
            BroadcastCard(
                broadcast = BroadcastNotification(
                    title = "System Update Failure",
                    body = "The scheduled update failed due to connection issues.",
                    status = BroadcastStatus.FAILED
                )
            )
        }
    }
}

@Composable
fun StatusBadge(
    status: BroadcastStatus,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = when (status) {
            BroadcastStatus.SENT -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            BroadcastStatus.FAILED -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
            BroadcastStatus.PENDING -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
        }
    ) {
        Text(
            text = status.displayName,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = when (status) {
                BroadcastStatus.SENT -> MaterialTheme.colorScheme.primary
                BroadcastStatus.FAILED -> MaterialTheme.colorScheme.error
                BroadcastStatus.PENDING -> MaterialTheme.colorScheme.tertiary
            }
        )
    }
}