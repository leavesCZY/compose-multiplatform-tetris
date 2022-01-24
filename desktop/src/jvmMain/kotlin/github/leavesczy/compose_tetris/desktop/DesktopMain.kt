package github.leavesczy.compose_tetris.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:23
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
fun main() = application {
    val windowState = rememberWindowState(width = 1200.dp, height = 900.dp)
    Window(
        title = "compose_tetris",
        resizable = false,
        focusable = true,
        state = windowState,
        onCloseRequest = ::exitApplication
    ) {
        DesktopMainScreen()
    }
}