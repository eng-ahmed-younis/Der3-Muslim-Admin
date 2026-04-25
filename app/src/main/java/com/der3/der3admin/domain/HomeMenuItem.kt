package com.der3.der3admin.domain

import androidx.compose.ui.graphics.vector.ImageVector

data class HomeMenuItem(
    val type: GridItemType,
    val title: String,
    val icon: ImageVector
)