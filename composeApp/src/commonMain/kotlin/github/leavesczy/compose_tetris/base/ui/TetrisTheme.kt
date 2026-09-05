package github.leavesczy.compose_tetris.base.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Shared by game LCD plate and control pad. */
internal val PanelCornerRadius = 16.dp

internal val PanelShape = RoundedCornerShape(size = PanelCornerRadius)

/** Same horizontal inset for LCD plate and control pad in portrait. */
internal val PanelHorizontalPadding = 12.dp

private val ShellTop = Color(color = 0xFFFFD54F)

private val ShellBottom = Color(color = 0xFFFF9800)

/** Olive LCD playfield fill. */
internal val LcdBoard = Color(color = 0xFF9EAD86)

/** Primary ink on the LCD (text, filled cells, chrome). */
internal val LcdInk = Color(color = 0xE6000000)

internal val LcdInkMuted = Color(color = 0x99000000)

internal val ShellGradient = Brush.verticalGradient(
    colors = listOf(ShellTop, ShellBottom)
)

internal val CellColorEmpty = Color(color = 0x59000000)

internal val CellColorFilled = LcdInk

internal val CellColorClearing = Color(color = 0xF2FFFFFF)

internal val ButtonMoveTop = Color(color = 0xFFFF5252)

internal val ButtonMoveBottom = Color(color = 0xFFD32F2F)

internal val ButtonControlTop = Color(color = 0xFFFF8A65)

internal val ButtonControlBottom = Color(color = 0xFFE64A19)

internal val ButtonMutedTop = Color(color = 0xFF64B5F6)

internal val ButtonMutedBottom = Color(color = 0xFF1565C0)

internal val ButtonMoveBrush = Brush.verticalGradient(
    colors = listOf(ButtonMoveTop, ButtonMoveBottom)
)

internal val ButtonControlBrush = Brush.verticalGradient(
    colors = listOf(ButtonControlTop, ButtonControlBottom)
)

internal val ButtonMutedBrush = Brush.verticalGradient(
    colors = listOf(ButtonMutedTop, ButtonMutedBottom)
)

internal val PanelFill = Color(color = 0xFFFFE082).copy(alpha = 0.40f)

internal val PanelRim = Color(color = 0xFFFFE082).copy(alpha = 0.55f)

internal val PanelStroke = Color(color = 0xFFE65100).copy(alpha = 0.35f)

internal val LabelOnShell = Color(color = 0xFF4E342E)

private val LightColorScheme = lightColorScheme(
    background = ShellTop,
    onBackground = LcdInk,
    primary = ButtonMoveTop,
    onPrimary = Color.White,
    secondary = ButtonMutedTop,
    onSecondary = Color.White
)

private val LightTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 26.0.sp,
        letterSpacing = 1.2.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 19.sp,
        lineHeight = 24.0.sp,
        letterSpacing = 1.0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 22.0.sp,
        letterSpacing = 0.8.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 22.0.sp,
        letterSpacing = 0.6.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        lineHeight = 12.0.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
fun TetrisTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = LightTypography,
        content = content
    )
}
