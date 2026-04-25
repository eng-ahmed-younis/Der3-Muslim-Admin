package com.der3.der3admin.presentation.screens.home.mvi

import com.der3.der3admin.presentation.mvi.Reducer
import javax.inject.Inject

class HomeReducer @Inject constructor() : Reducer<HomeAction, HomeState> {
    override fun reduce(action: HomeAction, state: HomeState): HomeState {
        return when (action) {
            is HomeAction.SetLoading -> state.copy(isLoading = action.isLoading)
            is HomeAction.SetMenuItems -> {
                state.copy(menuItems = action.menuItems)
            }
        }
    }
}