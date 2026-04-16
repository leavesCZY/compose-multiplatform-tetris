package github.leavesczy.compose_tetris

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
import github.leavesczy.compose_tetris.base.logic.Action
import github.leavesczy.compose_tetris.base.logic.TetrisViewModel
import github.leavesczy.compose_tetris.base.logic.TransformationType
import github.leavesczy.compose_tetris.base.ui.TetrisPage
import github.leavesczy.compose_tetris.resources.Res
import github.leavesczy.compose_tetris.resources.desktop_launch_icon
import org.jetbrains.compose.resources.painterResource
import java.awt.Toolkit

/**
 * @Author: leavesCZY
 * @Date: 2026/4/16 20:03
 * @Desc:
 */
fun main() = application {
    val coroutineScope = rememberCoroutineScope()
    val tetrisViewModel = remember {
        TetrisViewModel(
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
            tetrisViewModel.dispatchKeyEvent(keyEvent = it)
        },
        onCloseRequest = ::exitApplication
    ) {
        TetrisPage(
            modifier = Modifier,
            tetrisViewModel = tetrisViewModel
        )
    }
}

private fun preferredWindowSize(): DpSize {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val screenWidth = screenSize.width
    val screenHeight = screenSize.height
    val preferredWidth = screenWidth * 0.75f
    val preferredHeight = screenHeight * 0.8f
    val width = minOf(a = 1200f, b = preferredWidth)
    val height = minOf(a = 900f, b = preferredHeight)
    return DpSize(width.dp, height.dp)
}

private fun TetrisViewModel.dispatchKeyEvent(keyEvent: KeyEvent): Boolean {
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