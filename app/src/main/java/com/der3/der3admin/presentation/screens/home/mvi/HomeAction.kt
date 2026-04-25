package com.der3.der3admin.presentation.screens.home.mvi

import com.der3.der3admin.domain.HomeMenuItem
import com.der3.der3admin.mvi.MviAction

sealed interface HomeAction : MviAction {
    data class SetLoading(val isLoading: Boolean) : HomeAction
    data class SetMenuItems(val menuItems: List<HomeMenuItem>) : HomeAction
}
