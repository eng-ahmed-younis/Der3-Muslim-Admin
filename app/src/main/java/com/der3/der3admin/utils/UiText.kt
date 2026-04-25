package com.der3.der3admin.utils

import android.content.Context

sealed class UiText {
    data class DynamicError(val message: String) : UiText()
    data class ResourceError(val messageId: Int) : UiText()
}


fun UiText.asString(context: Context): String {
    return when (this) {
        is UiText.DynamicError -> message
        is UiText.ResourceError -> context.getString(messageId)
    }
}
