package github.leavesczy.compose_tetris.desktop

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import github.leavesczy.compose_tetris.common.logic.Action
import github.leavesczy.compose_tetris.common.logic.TransformationType
import github.leavesczy.compose_tetris.common.ui.MainScreen

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:09
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DesktopMainScreen() {
    val tetrisLogic by remember {
        mutableStateOf(DesktopTetrisLogic())
    }
    val onKeyEvent: (KeyEvent) -> Boolean = remember {
        {
            when (it.key) {
                Key.DirectionLeft -> {
                    if (it.type == KeyEventType.KeyDown) {
                        tetrisLogic.dispatch(action = Action.Transformation(TransformationType.Left))
                    }
                }
                Key.DirectionRight -> {
                    if (it.type == KeyEventType.KeyDown) {
                        tetrisLogic.dispatch(action = Action.Transformation(TransformationType.Right))
                    }
                }
                Key.DirectionUp -> {
                    if (it.type == KeyEventType.KeyUp) {
                        tetrisLogic.dispatch(action = Action.Transformation(TransformationType.Rotate))
                    }
                }
                Key.DirectionDown -> {
                    if (it.type == KeyEventType.KeyUp) {
                        tetrisLogic.dispatch(action = Action.Transformation(TransformationType.Fall))
                    }
                }
            }
            true
        }
    }
    val focusRequester = remember {
        FocusRequester()
    }
    MainScreen(
        modifier = Modifier.padding(top = 30.dp)
            .focusRequester(focusRequester)
            .focusable(enabled = true)
            .onKeyEvent(onKeyEvent = onKeyEvent),
        tetrisLogic = tetrisLogic
    )
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
        tetrisLogic.dispatch(action = Action.Welcome)
    }
}