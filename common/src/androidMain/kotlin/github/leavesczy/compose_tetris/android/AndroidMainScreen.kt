package github.leavesczy.compose_tetris.android

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = Color.Transparent,
        darkIcons = true
    )

    val tetrisViewModel = viewModel<TetrisViewModel>()
    fun dispatchAction(action: Action) {
        tetrisViewModel.dispatch(action = action)
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(key1 = Unit) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                dispatchAction(action = Action.Resume)
            }

            override fun onPause(owner: LifecycleOwner) {
                dispatchAction(action = Action.Background)
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
    ProvideWindowInsets {
        MainScreen(
            modifier = Modifier.systemBarsPadding().padding(top = 10.dp),
            tetrisLogic = tetrisViewModel
        )
    }
    LaunchedEffect(key1 = Unit) {
        dispatchAction(action = Action.Welcome)
    }
}