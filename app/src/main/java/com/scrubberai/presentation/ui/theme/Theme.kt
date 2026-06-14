package com.scrubberai.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF1E88E5),
    onPrimary = androidx.compose.ui.graphics.Color.White
)

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF90CAF9),
    onPrimary = androidx.compose.ui.graphics.Color.Black
)

@Composable
fun ScrubberAITheme(content: @Composable () -> Unit) {
    val colors = LightColors
    MaterialTheme(colorScheme = colors, content = content)
}
