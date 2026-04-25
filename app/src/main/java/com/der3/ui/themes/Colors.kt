package com.der3.ui.themes

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

internal val lightColors = Colors(
    green900 = Color(0xFF0D2B11),
    green800 = Color(0xFF1B5F21),
    green700 = Color(0xFF1F6B2D),
    green500 = Color(0xFF5F8F63),
    green400 = Color(0xFFA4BFA6),
    green100 = Color(0xFFDDE8DF),
    green50 = Color(0xFFE4EDE6),
    green25 = Color(0xFFF4F6F5),

    gray900Text = Color(0xFF111827),
    gray500 = Color(0xFF6B7280),
    gray400 = Color(0xFF9CA3AF),
    gray300 = Color(0xFFD1D5DB),
    gray200 = Color(0xFFE2E8F0),
    gray100 = Color(0xFFCBD5C9),
    gray50 = Color(0xFFF5F6F3),

    blueGray400 = Color(0xFF9BA9BD),

    gold700 = Color(0xFFB8963D),
    gold600 = Color(0xFFC8A951),
    gold500 = Color(0xFFC5A059),
    gold400 = Color(0xFFFFC107),
    white = Color(0xFFFFFFFF),

    red900 = Color(0xFFB91C1C),
    red50 = Color(0xFFFDECEC)
)

internal val darkColors = Colors(
    green900 = Color(0xFF0D2B11),
    green800 = Color(0xFF1B5F21),
    green700 = Color(0xFF1F6B2D),
    green500 = Color(0xFF5F8F63),
    green400 = Color(0xFFA4BFA6),
    green100 = Color(0xFFDDE8DF),
    green50 = Color(0xFFE4EDE6),
    green25 = Color(0xFFF4F6F5),

    gray900Text = Color(0xFF111827),
    gray500 = Color(0xFF6B7280),
    gray400 = Color(0xFF9CA3AF),
    gray300 = Color(0xFFD1D5DB),
    gray200 = Color(0xFFE2E8F0),
    gray100 = Color(0xFFCBD5C9),
    gray50 = Color(0xFFF5F6F3),

    blueGray400 = Color(0xFF9BA9BD),

    gold700 = Color(0xFFB8963D),
    gold600 = Color(0xFFC8A951),
    gold500 = Color(0xFFC5A059),
    gold400 = Color(0xFFFFC107),
    white = Color(0xFFFFFFFF),

    red900 = Color(0xFFB91C1C),
    red50 = Color(0xFFFDECEC)
)

@Stable
data class Colors(
    val green900: Color,
    val green800: Color,
    val green700: Color,
    val green500: Color,
    val green400: Color,
    val green100: Color,
    val green50: Color,
    val green25: Color,

    val gold700: Color,
    val gold600: Color,
    val gold500: Color,
    val gold400: Color,
    val white: Color,

    val gray900Text: Color,
    val gray500: Color,
    val gray400: Color,
    val gray300: Color,
    val blueGray400: Color,
    val gray100: Color,
    val gray200: Color,
    val gray50: Color,

    val red900: Color,
    val red50: Color
)
