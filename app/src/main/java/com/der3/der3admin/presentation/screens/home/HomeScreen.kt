package com.der3.der3admin.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.der3.der3admin.presentation.components.Der3TopAppBar
import com.der3.der3admin.presentation.screens.home.mvi.HomeIntent
import com.der3.der3admin.presentation.screens.home.mvi.HomeState
import com.der3.der3admin.presentation.theme.AppColors
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.der3.der3admin.domain.GridItemType
import com.der3.der3admin.domain.HomeMenuItem
import com.der3.der3admin.domain.NotificationItemType
import com.der3.der3admin.presentation.mvi.MviEffect
import com.der3.der3admin.presentation.theme.Der3AdminTheme
import com.der3.der3admin.utils.asString
import com.der3.screens.Screens
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@Composable
fun HomeRoute(
    onNavigate: (Screens) -> Unit
) {
    val  viewModel: HomeScreenViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showErrorDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

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



    HomeScreen(
        state = viewModel.viewState,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppColors.gray50)
    ) {
        Der3TopAppBar(
            title = "Der3 Admin",
            showBackButton = false,
            backgroundColor = AppColors.gray50
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.menuItems) { item ->
                HomeMenuCard(item){ type ->
                    onIntent(HomeIntent.OnMenuItemClick(type = type))
                }
            }
        }
    }
}

@Composable
fun HomeMenuCard(
    item: HomeMenuItem,
    onClick: (GridItemType) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick(item.type) },
        colors = CardDefaults.cardColors(
            containerColor = AppColors.white
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                modifier = Modifier.size(48.dp),
                tint = AppColors.green800
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = AppColors.green900,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    Der3AdminTheme {
        HomeScreen(
            state = HomeState(
                menuItems = listOf(
                    HomeMenuItem(
                        type = NotificationItemType.DailyNotification,
                        title = "الإشعارات",
                        icon = Icons.Default.Notifications
                    ),
                    HomeMenuItem(
                        type = NotificationItemType.NormalNotification,
                        title = "الجدول الزمني",
                        icon = Icons.Default.Schedule
                    ),
                    HomeMenuItem(
                        type = NotificationItemType.OtherSettings,
                        title = "الإعدادات",
                        icon = Icons.Default.Settings
                    )
                )
            ),
            onIntent = {}
        )
    }
}
