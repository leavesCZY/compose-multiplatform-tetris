package github.leavesczy.compose_tetris.android

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import github.leavesczy.compose_tetris.common.logic.Action
import github.leavesczy.compose_tetris.common.ui.MainScreen

/**
 * @Author: leavesCZY
 * @Date: 2022/1/17 23:12
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Composable
fun AndroidMainScreen() {
    val tetrisViewModel = viewModel<AndroidTetrisViewModel>()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(key1 = Unit) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                tetrisViewModel.dispatch(action = Action.Resume)
            }

            override fun onPause(owner: LifecycleOwner) {
                tetrisViewModel.dispatch(action = Action.Background)
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
    MainScreen(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(top = 20.dp),
        tetrisLogic = tetrisViewModel.tetrisLogic
    )
}