package com.der3.der3admin.domain

data class Der3Notification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val color: String = "#4CAF50",
    val image: String = "",
    val type: String = NotificationType.GENERAL.value, // daily, custom, urgent, event
    val timestamp: Long = System.currentTimeMillis(),
    val read: Boolean = false,
    val priority: String = "normal" // high, normal, low
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "title" to title,
            "message" to message,
            "color" to color,
            "image" to image,
            "type" to type,
            "timestamp" to timestamp,
            "read" to read,
            "priority" to priority
        )
    }

    companion object {
        val types = listOf("daily", "custom", "urgent", "event")
        val priorities = listOf("high", "normal", "low")
        val colors = listOf(
            "#4CAF50" to "Green",
            "#2196F3" to "Blue",
            "#F44336" to "Red",
            "#FF9800" to "Orange",
            "#9C27B0" to "Purple",
            "#E91E63" to "Pink"
        )
    }
}