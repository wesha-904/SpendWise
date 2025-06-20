package com.example.spendwise.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.ui.graphics.Color


val DuskyBrown = Color(0xFF5D4037)
val Beige = Color(0xFFF5F5DC)
val Maroon = Color(0xFF550000)
val PaperWhite = Color(0xFFFDF6E3)
val InkBlack = Color(0xFF1C1C1C)

private val PaperColorScheme = lightColorScheme(
    primary = Maroon,
    secondary = DuskyBrown,
    background = PaperWhite,
    surface = Beige,
    onPrimary = PaperWhite,
    onSecondary = PaperWhite,
    onBackground = InkBlack,
    onSurface = InkBlack
)

@Composable
fun SpendwiseTheme(content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(PaperWhite, darkIcons = true)
    }

    MaterialTheme(
        colorScheme = PaperColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
