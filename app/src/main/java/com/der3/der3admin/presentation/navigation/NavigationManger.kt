package com.der3.der3admin.presentation.navigation


import android.annotation.SuppressLint
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.der3.screens.Screens
import kotlin.reflect.KClass

object NavigationManager {
    private fun NavController.setPayloadOnPreviousScreen(payload: Map<String, String>?) {
        payload?.entries?.forEach { entry ->
            previousBackStackEntry?.savedStateHandle?.set(
                entry.key,
                entry.value
            )
        }
    }

    @SuppressLint("RestrictedApi")
    private fun NavController.setPayloadOnBackStackEntry(payload: Map<String, String>?, stepsBack: Int) {
        if (payload == null) return
        val backStackList = currentBackStack.value
        val targetIndex = backStackList.size - 1 - stepsBack
        if (targetIndex >= 0 && targetIndex < backStackList.size) {
            val targetEntry = backStackList[targetIndex]
            payload.entries.forEach { entry ->
                targetEntry.savedStateHandle[entry.key] = entry.value
            }
        }
    }

    @SuppressLint("RestrictedApi")
    fun NavController.navigateKeepingOnly(
        targetScreen: Screens,
        keepScreen: KClass<out Screens>
    ) {
        val keepEntry = currentBackStack.value.findLast {
            it.destination.hasRoute(route = keepScreen)
        }

        navigate(targetScreen) {
            if (keepEntry != null) {
                //  case 1: the screen we want to keep is in the stack
                popUpTo(keepEntry.destination.id) {
                    inclusive = false
                }
            } else {
                //  case 2: it's NOT in the stack
                // fall back to "keep only the start of the graph" (usually Home)
                popUpTo(graph.startDestinationId) {
                    inclusive = false   // keep home
                }
            }
            launchSingleTop = true
        }
    }

    @SuppressLint("RestrictedApi")
    fun NavController.navigateToOrOpen(destination: Screens) {
        val destinationClass = destination::class
        val existingEntry = currentBackStack.value.findLast {
            it.destination.hasRoute(route = destinationClass)
        }

        if (existingEntry != null) {
            popBackStack(existingEntry.destination.id, inclusive = false)
        } else {
            navigate(destination)
        }
    }


    @SuppressLint("RestrictedApi")
    fun NavController.navigateTo(screen: Screens) {
        when (screen) {
            is Screens.Back -> {
                if (screen.count <= 1) {
                    if (screen.payload != null && screen.payload?.isNotEmpty() != false) {
                        setPayloadOnPreviousScreen(screen.payload)
                    }
                    popBackStack()
                } else {
                    if (screen.payload != null && screen.payload?.isNotEmpty() != false) {
                        setPayloadOnBackStackEntry(screen.payload, screen.count)
                    }
                    repeat(screen.count) {
                        popBackStack()
                    }
                }
            }

            is Screens.BackTo -> {
                //[HomeScreen] → [DocumentListScreen] → [ReviewIDInfoScreen] → [UploadPhotoScreen]
                //                                            ↑
                //                                    (target screen)
                //if exclusive is true, ReviewIDInfoScreen will also be popped
                // Example: [Home] → [List] → [Details] → [Edit] → [Confirm]
                //                   ↑ (want to go here)
                // Screens.BackTo(
                //        screen = ListScreen(), // The target screen to go back to
                //        exclusive = false,      // Keep ListScreen in stack
                //        payload = mapOf("result" to "success")
                //    )
                // From Confirm screen, go back to List screen  exclusive = false,// Keep ListScreen in stack
                // If exclusive = true, it will also remove ListScreen and land on Home
                val targetScreenClass = screen.screen::class
                val backStackEntry = this.currentBackStack.value.findLast {
                    it.destination.hasRoute(route = targetScreenClass)
                }

                if (backStackEntry != null) {
                    if (screen.payload != null) {
                        screen.payload?.forEach { (key, value) ->
                            backStackEntry.savedStateHandle[key] = value
                        }
                    }
                    popBackStack(backStackEntry.destination.id, inclusive = screen.exclusive)
                } else {
                    println("NavigationManager: Warning - Could not find back stack entry for BackTo target screen.")
                    // Fallback to old behavior or just pop, to avoid getting stuck.
                    try {
                        if (currentBackStack.value.size > 1) popBackStack()
                    } catch (e: Exception) {
                        println("NavigationManager: Error during fallback popBackStack in BackTo - ${e.message}")
                    }
                }
            }

            is Screens.NavigateToRoot -> {
                try {
                    navigate(screen.screen) {
                        //Before: [HomeScreen] → [CarListScreen] → [BookingScreen] → [ChecklistScreen]
                        //After:  [BookingRateScreen]  // Everything else is cleared
                        popUpTo(0) { inclusive = true }  // Clears everything
                        launchSingleTop = true
                    }
                } catch (e: Exception) {
                    println("NavigationManager: Error during navigate in NavigateToRoot - ${e.message}")
                }
            }

            is Screens.Replace -> {
                // Replaces current screen with new one
                try {
                    val oldRoute = screen.oldScreen.qualifiedName ?: return
                    navigate(screen.newScreen) {
                        popUpTo(oldRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                } catch (e: Exception) {
                    println("NavigationManager: Error during replace navigation - ${e.message}")
                }
            }

            is Screens.NavigateKeepingOnly -> {
                navigateKeepingOnly(
                    targetScreen = screen.targetScreen,
                    keepScreen = screen.keepScreen
                )
            }

          /*  is ChauffeurScreens.ChauffeurMainScreen -> {
                navigateToOrOpen(screen)
            }*/

            else -> {
                try {
                    navigate(screen)
                } catch (e: Exception) {
                    println("NavigationManager: Error during forward navigation - ${e.message}")
                }
            }
        }
    }
}


