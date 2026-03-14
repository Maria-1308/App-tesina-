package com.example.poderjudicialkotlin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = MagentaPurpuraPJ,
    secondary = BorgonaPJ,
    background = BlancoPerla,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onBackground = GrisOscuroPJ,
    onSurface = GrisOscuroPJ
)

@Composable
fun PoderJudicialKotlinTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        content = content
    )
}