package github.leavesczy.compose_tetris

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import github.leavesczy.compose_tetris.base.logic.GameAction
import github.leavesczy.compose_tetris.base.logic.SoundPlayer
import github.leavesczy.compose_tetris.base.logic.TetrisViewModel
import github.leavesczy.compose_tetris.base.ui.TetrisPage
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {

    private class TetrisViewModelFactory(
        private val soundPlayer: SoundPlayer
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            if (modelClass.java.isAssignableFrom(TetrisViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TetrisViewModel(soundPlayer = soundPlayer) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
        }

    }

    private val tetrisViewModel by viewModels<TetrisViewModel> {
        TetrisViewModelFactory(
            soundPlayer = AndroidSoundPlayer(application = application)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT,
                detectDarkMode = {
                    false
                }
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT,
                detectDarkMode = {
                    false
                }
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this@MainActivity)
            TetrisPage(
                modifier = Modifier,
                windowSizeClass = windowSizeClass,
                viewModel = tetrisViewModel
            )
        }
    }

    override fun onPause() {
        super.onPause()
        tetrisViewModel.dispatch(action = GameAction.EnterBackground)
    }

}