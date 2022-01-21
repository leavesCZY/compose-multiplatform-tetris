package github.leavesczy.compose_tetris.desktop

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.leavesczy.compose_tetris.common.logic.Action
import github.leavesczy.compose_tetris.common.ui.MainScreen

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:09
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
@Composable
fun DesktopMainScreen() {
    val tetrisLogic by remember {
        mutableStateOf(DesktopTetrisLogic())
    }
    MainScreen(
        modifier = Modifier.padding(top = 30.dp),
        tetrisLogic = tetrisLogic
    )
    LaunchedEffect(key1 = Unit) {
        tetrisLogic.dispatch(action = Action.Welcome)
    }
}