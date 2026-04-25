package com.der3.der3admin.presentation.screens.home.mvi

import androidx.compose.runtime.Immutable
import com.der3.der3admin.domain.HomeMenuItem
import com.der3.der3admin.mvi.MviState

@Immutable
data class HomeState(
    val isLoading: Boolean = false,
    val menuItems: List<HomeMenuItem> = emptyList()
) : MviState