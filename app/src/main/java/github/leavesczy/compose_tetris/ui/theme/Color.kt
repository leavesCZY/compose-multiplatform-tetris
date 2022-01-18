package github.leavesczy.compose_tetris.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val ScreenBackground = Color(0xff9ead86)

val BackgroundColorLight = Color(0xF2FFD600)
val OnBackgroundColorLight = Color(0xff9ead86)

val BrickAlpha = Color.Black.copy(alpha = 0.2f)
val BrickFill = Color.Black.copy(alpha = 0.9f)

val ButtonColor = Brush.linearGradient(
    colors = listOf(
        Color(0xFFE53935),
        Color(0xFFF02A26),
        Color(0xFFF3413D)
    )
)