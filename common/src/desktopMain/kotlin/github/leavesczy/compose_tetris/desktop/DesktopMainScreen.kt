package github.leavesczy.compose_tetris.desktop

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import github.leavesczy.compose_tetris.common.logic.Action
import github.leavesczy.compose_tetris.common.logic.TetrisLogic
import github.leavesczy.compose_tetris.common.logic.TransformationType
import github.leavesczy.compose_tetris.common.ui.MainScreen

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:09
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
@Composable
fun DesktopMainScreen() {
    val tetrisLogic = remember {
        TetrisLogic(
            coroutineScope = DesktopCoroutineScope,
            soundPlayer = DesktopSoundPlayer()
        )
    }
    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
    MainScreen(
        modifier = Modifier
            .padding(top = 30.dp)
            .focusRequester(focusRequester = focusRequester)
            .focusable(enabled = true)
            .onKeyEvent(onKeyEvent = {
                dispatchKeyEvent(tetrisLogic = tetrisLogic, keyEvent = it)
            }),
        tetrisLogic = tetrisLogic
    )
}

private fun dispatchKeyEvent(tetrisLogic: TetrisLogic, keyEvent: KeyEvent): Boolean {
    if (keyEvent.type == KeyEventType.KeyDown) {
        when (keyEvent.key) {
            Key.Enter -> {
                tetrisLogic.dispatch(action = Action.Start)
            }

            Key.DirectionLeft, Key.A, Key.NumPad4 -> {
                tetrisLogic.dispatch(
                    action = Action.Transformation(
                        TransformationType.Left
                    )
                )
            }

            Key.DirectionRight, Key.D, Key.NumPad6 -> {
                tetrisLogic.dispatch(
                    action = Action.Transformation(
                        TransformationType.Right
                    )
                )
            }

            Key.DirectionUp, Key.W, Key.NumPad8 -> {
                tetrisLogic.dispatch(
                    action = Action.Transformation(
                        TransformationType.Rotate
                    )
                )
            }

            Key.DirectionDown, Key.S, Key.NumPad5 -> {
                tetrisLogic.dispatch(
                    action = Action.Transformation(
                        TransformationType.Fall
                    )
                )
            }
        }
    }
    return true
}