package com.der3.screens

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import kotlin.reflect.KClass
import kotlinx.serialization.Serializable

@Keep
@Stable
interface Screens {

    @Serializable
    data class Back(
        val payload: Map<String, String>? = null,
        val count: Int = 1
    ) : Screens


    @Serializable
    data class BackTo(
        val screen: Screens,
        val exclusive: Boolean = false,
        val payload: Map<String, String>? = null
    ) : Screens


    @Serializable
    data class NavigateToRoot(
        val screen: Screens,
        val exclusive: Boolean = false
    ) : Screens


    @Serializable
    data class Replace(
        val newScreen: Screens,
        val oldScreen: KClass<out Screens>,
        val payload: Map<String, String>? = null
    ) : Screens


    @Stable
    @Serializable
    data class NavigateKeepingOnly(
        val targetScreen: Screens,
        val keepScreen: KClass<out Screens>
    ) : Screens

}