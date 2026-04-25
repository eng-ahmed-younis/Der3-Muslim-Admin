package com.der3.der3admin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.der3.ui.themes.AppColors
import com.der3.ui.themes.Der3AdminTheme

@Composable
fun Der3TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    backgroundColor: Color,
    titleColor: Color = AppColors.gray900Text,
    subtitleColor: Color = AppColors.gray500,
    navigationIconColor: Color = AppColors.gray900Text,
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {},
    trailingContent: @Composable (() -> Unit)? = null
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 🔹 Navigation Icon
        if (showBackButton) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = "Navigation",
                    tint = navigationIconColor
                )
            }
        } else {
            Spacer(modifier = Modifier.size(48.dp))
        }

        // 🔹 Title & Subtitle
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
            if (subtitle != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = subtitleColor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = AppColors.green500,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        // 🔹 Trailing Content
        if (trailingContent != null) {
            trailingContent()
        } else {
            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Der3TopAppBarPreview() {
    Der3AdminTheme(
    ) {
        Der3TopAppBar(
            title = "Der3 Top App Bar",
            backgroundColor = AppColors.gray50,
            onBackClick = {},
            trailingContent = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = AppColors.gray900Text
                    )
                }
            }
        )
    }
}
