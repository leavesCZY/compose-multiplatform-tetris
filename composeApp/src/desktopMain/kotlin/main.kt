import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import github.leavesczy.compose_tetris.desktop.DesktopMainScreen
import java.awt.Toolkit

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:23
 * @Desc:
 */
fun main() = application {
    Window(
        title = "compose_tetris",
        resizable = false,
        icon = painterResource("/compose_tetris.ico"),
        state = rememberWindowState(
            size = preferredWindowSize(),
            position = WindowPosition.Aligned(alignment = Alignment.Center)
        ),
        onCloseRequest = ::exitApplication
    ) {
        DesktopMainScreen()
    }
}

private fun preferredWindowSize(): DpSize {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val screenWidth = screenSize.width
    val screenHeight = screenSize.height
    val preferredWidth = screenWidth * 0.75f
    val preferredHeight = screenHeight * 0.8f
    val width = minOf(1200f, preferredWidth)
    val height = minOf(900f, preferredHeight)
    return DpSize(width.dp, height.dp)
}