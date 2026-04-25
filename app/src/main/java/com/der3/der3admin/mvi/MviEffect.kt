package com.der3.der3admin.mvi


import com.der3.der3admin.utils.UiText
import com.der3.screens.Screens


interface MviEffect {

    data class Navigate(val screen: Screens) : MviEffect

    data class OnErrorDialog(val error: UiText) : MviEffect

    data class OnSuccessDialog(val success: String) : MviEffect

    data object CaptureAndShareImage : MviEffect


}
