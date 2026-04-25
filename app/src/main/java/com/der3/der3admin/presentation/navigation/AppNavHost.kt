package com.der3.der3admin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.der3.der3admin.presentation.navigation.NavigationManager.navigateTo
import com.der3.der3admin.presentation.navigation.screens.Der3NavigationRoute
import com.der3.der3admin.presentation.screens.broadcast.BroadcastRoute
import com.der3.der3admin.presentation.screens.daily_notification.DailyNotificationRoute
import com.der3.der3admin.presentation.screens.home.HomeRoute


@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Der3NavigationRoute.HomeScreen,
        modifier = modifier
    ) {
        composable<Der3NavigationRoute.HomeScreen> {
            HomeRoute { screen ->
                navController.navigateTo(screen)
            }
        }


        composable<Der3NavigationRoute.BroadcastScreen> {
            BroadcastRoute { screen ->
                navController.navigateTo(screen)
            }
        }

        composable<Der3NavigationRoute.DailyNotificationScreen> {
            DailyNotificationRoute {
                navController.popBackStack()
            }
        }
    }
}