package com.der3.ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalDer3Colors = compositionLocalOf<Colors> {
    error("No Colors provided")
}

object AppColors {
    val green900: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.green900
    val green800: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.green800
    val green700: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.green700
    val green500: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.green500
    val green400: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.green400
    val green100: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.green100
    val green50: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.green50
    val green25: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.green25

    val gold700: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.gold700
    val gold600: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.gold600
    val gold500: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.gold500
    val gold400: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.gold400
    val white: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.white

    val gray900Text: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.gray900Text
    val gray500: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.gray500
    val gray400: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.gray400
    val gray300: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.gray300
    val blueGray400: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.blueGray400
    val gray100: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.gray100
    val gray200: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.gray200
    val gray50: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.gray50

    val red900: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.red900
    val red50: Color @Composable @ReadOnlyComposable get() = LocalDer3Colors.current.red50
}

@Composable
fun Der3MuslimTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) darkColors else lightColors

    CompositionLocalProvider(
        LocalDer3Colors provides colors,
        content = content
    )
}
