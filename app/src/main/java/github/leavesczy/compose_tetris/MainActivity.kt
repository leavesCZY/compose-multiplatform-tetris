package github.leavesczy.compose_tetris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.view.WindowCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import github.leavesczy.compose_tetris.logic.Action
import github.leavesczy.compose_tetris.logic.TetrisViewModel
import github.leavesczy.compose_tetris.logic.combinedPlayListener
import github.leavesczy.compose_tetris.ui.MainScreen

/**
 * @Author: leavesCZY
 * @Date: 2021/6/3 22:06
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            Tetris()
        }
    }

    @Composable
    private fun Tetris() {
        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
        val tetrisViewModel = viewModel<TetrisViewModel>()
        val lifecycle = LocalLifecycleOwner.current.lifecycle
        DisposableEffect(key1 = Unit) {
            val observer = object : DefaultLifecycleObserver {
                override fun onResume(owner: LifecycleOwner) {
                    tetrisViewModel.dispatch(Action.Resume)
                }

                override fun onPause(owner: LifecycleOwner) {
                    tetrisViewModel.dispatch(Action.Background)
                }
            }
            lifecycle.addObserver(observer)
            onDispose {
                lifecycle.removeObserver(observer)
            }
        }
        val tetrisState by tetrisViewModel.tetrisStateLD.collectAsState()
        val playListener by remember {
            mutableStateOf(
                combinedPlayListener(
                    onStart = {
                        tetrisViewModel.dispatch(Action.Start)
                    },
                    onPause = {
                        tetrisViewModel.dispatch(Action.Pause)
                    },
                    onReset = {
                        tetrisViewModel.dispatch(Action.Reset)
                    },
                    onTransformation = {
                        tetrisViewModel.dispatch(Action.Transformation(it))
                    },
                    onSound = {
                        tetrisViewModel.dispatch(Action.Sound)
                    },
                )
            )
        }
        MainScreen(
            tetrisState = tetrisState, playListener = playListener
        )
        LaunchedEffect(key1 = Unit) {
            tetrisViewModel.dispatch(action = Action.Welcome)
        }
    }
}