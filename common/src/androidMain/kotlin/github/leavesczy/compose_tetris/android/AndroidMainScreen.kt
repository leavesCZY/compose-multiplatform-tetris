package github.leavesczy.compose_tetris.android

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import github.leavesczy.compose_tetris.common.logic.Action
import github.leavesczy.compose_tetris.common.logic.TetrisLogicImpl
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

    val tetrisViewModel =
        viewModel<AndroidTetrisViewModel>(factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AndroidTetrisViewModel(delegate = TetrisLogicImpl()) as T
            }
        })

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
    MainScreen(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(top = 10.dp),
        tetrisLogic = tetrisViewModel
    )
}