import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import compose_multiplatform_tetris.composeapp.generated.resources.Res
import compose_multiplatform_tetris.composeapp.generated.resources.desktop_launch_icon
import github.leavesczy.compose_tetris.DesktopSoundPlayer
import github.leavesczy.compose_tetris.logic.Action
import github.leavesczy.compose_tetris.logic.TetrisLogic
import github.leavesczy.compose_tetris.logic.TransformationType
import github.leavesczy.compose_tetris.ui.TetrisPage
import org.jetbrains.compose.resources.painterResource
import java.awt.Toolkit

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:23
 * @Desc:
 */
fun main() = application {
    val coroutineScope = rememberCoroutineScope()
    val tetrisLogic = remember {
        TetrisLogic(
            coroutineScope = coroutineScope,
            soundPlayer = DesktopSoundPlayer(coroutineScope = coroutineScope)
        )
    }
    Window(
        title = "compose-multiplatform-tetris",
        resizable = false,
        icon = painterResource(Res.drawable.desktop_launch_icon),
        state = rememberWindowState(
            size = preferredWindowSize(),
            position = WindowPosition.Aligned(alignment = Alignment.Center)
        ),
        onKeyEvent = {
            tetrisLogic.dispatchKeyEvent(keyEvent = it)
        },
        onCloseRequest = ::exitApplication
    ) {
        TetrisPage(
            modifier = Modifier,
            tetrisLogic = tetrisLogic
        )
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

private fun TetrisLogic.dispatchKeyEvent(keyEvent: KeyEvent): Boolean {
    if (keyEvent.type == KeyEventType.KeyDown) {
        when (keyEvent.key) {
            Key.Enter -> {
                dispatch(action = Action.Start)
            }

            Key.DirectionLeft, Key.A, Key.NumPad4 -> {
                dispatch(action = Action.Transformation(TransformationType.Left))
            }

            Key.DirectionRight, Key.D, Key.NumPad6 -> {
                dispatch(action = Action.Transformation(TransformationType.Right))
            }

            Key.DirectionUp, Key.W, Key.NumPad8 -> {
                dispatch(action = Action.Transformation(TransformationType.Rotate))
            }

            Key.DirectionDown, Key.S, Key.NumPad5 -> {
                dispatch(action = Action.Transformation(TransformationType.Fall))
            }
        }
    }
    return true
}