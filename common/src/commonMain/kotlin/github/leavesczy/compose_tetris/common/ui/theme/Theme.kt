package github.leavesczy.compose_tetris.common.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    background = BackgroundColorLight, onBackground = OnBackgroundColorLight
)

@Composable
fun ComposeTetrisTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme, typography = LightTypography, content = content
    )
}