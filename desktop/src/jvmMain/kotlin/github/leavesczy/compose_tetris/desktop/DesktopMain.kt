package github.leavesczy.compose_tetris.desktop

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import java.awt.Dimension
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
        focusable = true,
        alwaysOnTop = true,
        state = WindowState(
            size = getPreferredWindowSize(desiredWidth = 1200, desiredHeight = 900),
            position = WindowPosition.Aligned(Alignment.Center)
        ),
        onCloseRequest = ::exitApplication
    ) {
        DesktopMainScreen()
    }
}

private fun getPreferredWindowSize(desiredWidth: Int, desiredHeight: Int): DpSize {
    val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
    val preferredWidth: Int = (screenSize.width * 0.8f).toInt()
    val preferredHeight: Int = (screenSize.height * 0.8f).toInt()
    val width: Int = if (desiredWidth < preferredWidth) desiredWidth else preferredWidth
    val height: Int = if (desiredHeight < preferredHeight) desiredHeight else preferredHeight
    return DpSize(width.dp, height.dp)
}