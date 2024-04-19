package github.leavesczy.compose_tetris.common.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * @Author: leavesCZY
 * @Date: 2024/4/19 22:34
 * @Desc:
 */
internal val BackgroundColorLight = Color(color = 0xF2FFD600)
internal val OnBackgroundColorLight = Color(color = 0xff9ead86)
internal val BrickColorAlpha = Color.Black.copy(alpha = 0.2f)
internal val BrickColorFill = Color.Black.copy(alpha = 0.9f)

internal val ButtonNormalColor = Brush.linearGradient(
    colors = listOf(
        Color(color = 0xFFF81E0E),
        Color(color = 0xFFE01D0E),
        Color(color = 0xFFC2180C)
    )
)

internal val ButtonOnPressedColor = Brush.linearGradient(
    colors = listOf(
        Color(color = 0xFFC03306),
        Color(color = 0xFFA22C06),
        Color(color = 0xFF812406)
    )
)

internal val ButtonDisabledColor = Brush.linearGradient(
    colors = listOf(
        Color(color = 0xFF0C6F64),
        Color(color = 0xFF0B7469),
        Color(color = 0xFF047E72)
    )
)

private val LightColorScheme = lightColorScheme(
    background = BackgroundColorLight,
    onBackground = OnBackgroundColorLight
)

private val LightTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 26.0.sp,
        letterSpacing = 0.8.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 19.sp,
        lineHeight = 24.0.sp,
        letterSpacing = 0.8.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 22.0.sp,
        letterSpacing = 0.8.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 22.0.sp,
        letterSpacing = 0.8.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.8.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.0.sp,
        letterSpacing = 0.8.sp
    )
)

@Composable
internal fun TetrisTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = LightTypography,
        content = content
    )
}