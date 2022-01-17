package github.leavesczy.compose_tetris.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColorLight,
    onPrimary = OnPrimaryColorLight,
    primaryContainer = PrimaryContainerColorLight,
    surface = SurfaceColorLight,
    onSurface = OnSurfaceColorLight,
    background = BackgroundColorLight,
    onSecondaryContainer = onSecondaryContainerLight,
    onBackground = OnBackgroundColorLight
)

@Composable
fun ComposeTetrisTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        LightColorScheme
    } else {
        LightColorScheme
    }
    MaterialTheme(
        colorScheme = colors,
        typography = LightTypography,
        content = content
    )
}