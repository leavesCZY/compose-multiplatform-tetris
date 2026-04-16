package github.leavesczy.compose_tetris

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import github.leavesczy.compose_tetris.base.logic.Action
import github.leavesczy.compose_tetris.base.logic.SoundPlayer
import github.leavesczy.compose_tetris.base.logic.TetrisViewModel
import github.leavesczy.compose_tetris.base.ui.TetrisPage
import kotlin.reflect.KClass

/**
 * @Author: leavesCZY
 * @Date: 2026/4/16 20:02
 * @Desc:
 */
class MainActivity : AppCompatActivity() {

    private class TetrisViewModelFactory(
        private val soundPlayer: SoundPlayer
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            if (modelClass.java.isAssignableFrom(TetrisViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TetrisViewModel(soundPlayer = soundPlayer) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

    private val tetrisViewModel by viewModels<TetrisViewModel> {
        TetrisViewModelFactory(soundPlayer = AndroidSoundPlayer(application = application))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            TetrisPage(
                modifier = Modifier
                    .windowInsetsPadding(insets = WindowInsets.statusBarsIgnoringVisibility)
                    .navigationBarsPadding(),
                tetrisViewModel = tetrisViewModel
            )
        }
    }

    override fun onResume() {
        super.onResume()
        tetrisViewModel.dispatch(action = Action.Resume)
    }

    override fun onPause() {
        super.onPause()
        tetrisViewModel.dispatch(action = Action.Background)
    }

}