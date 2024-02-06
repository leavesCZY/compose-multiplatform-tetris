package github.leavesczy.compose_tetris.common.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val BackgroundColorLight = Color(color = 0xF2FFD600)
val OnBackgroundColorLight = Color(color = 0xff9ead86)
val BrickColorAlpha = Color.Black.copy(alpha = 0.2f)
val BrickColorFill = Color.Black.copy(alpha = 0.9f)

val ButtonNormalColor = Brush.linearGradient(
    colors = listOf(
        Color(color = 0xFFF81E0E),
        Color(color = 0xFFE01D0E),
        Color(color = 0xFFC2180C)
    )
)

val ButtonOnPressedColor = Brush.linearGradient(
    colors = listOf(
        Color(color = 0xFFC03306),
        Color(color = 0xFFA22C06),
        Color(color = 0xFF812406)
    )
)

val ButtonDisabledColor = Brush.linearGradient(
    colors = listOf(
        Color(color = 0xFF0C6F64),
        Color(color = 0xFF0B7469),
        Color(color = 0xFF047E72)
    )
)