import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import github.leavesczy.compose_tetris.desktop.DesktopMainScreen

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
        state = WindowState(width = 1200.dp, height = 900.dp),
        onCloseRequest = ::exitApplication
    ) {
        DesktopMainScreen()
    }
}