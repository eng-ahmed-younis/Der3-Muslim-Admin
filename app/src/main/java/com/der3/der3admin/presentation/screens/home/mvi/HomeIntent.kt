package com.der3.der3admin.presentation.screens.home.mvi

import com.der3.der3admin.domain.GridItemType
import com.der3.der3admin.mvi.MviIntent

sealed interface HomeIntent : MviIntent {
    data class OnMenuItemClick(val type: GridItemType) : HomeIntent
}