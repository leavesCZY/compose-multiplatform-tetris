package github.leavesczy.compose_tetris.desktop

import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import java.awt.Toolkit

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:23
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
fun main() = application {
    Window(
        title = "compose_tetris",
        resizable = false,
        icon = painterResource("/icon_compose_tetris.ico"),
        state = WindowState(
            size = getPreferredWindowSize(desiredWidth = 1200f, desiredHeight = 900f),
            position = WindowPosition.Aligned(alignment = Alignment.Center)
        ),
        onCloseRequest = ::exitApplication
    ) {
        DesktopMainScreen()
    }
}

@Suppress("SameParameterValue")
private fun getPreferredWindowSize(desiredWidth: Float, desiredHeight: Float): DpSize {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val screenWidth = screenSize.width
    val screenHeight = screenSize.height
    val preferredWidth = screenWidth * 0.75f
    val preferredHeight = screenHeight * 0.8f
    val width = minOf(desiredWidth, preferredWidth)
    val height = minOf(desiredHeight, preferredHeight)
    return DpSize(width.dp, height.dp)
}