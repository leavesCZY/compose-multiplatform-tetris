package github.leavesczy.compose_tetris

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import github.leavesczy.compose_tetris.logic.Action
import github.leavesczy.compose_tetris.logic.TetrisLogic
import github.leavesczy.compose_tetris.ui.TetrisPage

/**
 * @Author: leavesCZY
 * @Date: 2021/6/3 22:06
 * @Desc:
 */
class MainActivity : AppCompatActivity() {

    private val tetrisLogic by lazy(mode = LazyThreadSafetyMode.NONE) {
        TetrisLogic(
            coroutineScope = lifecycleScope,
            soundPlayer = AndroidSoundPlayer(application = application)
        )
    }

    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setSystemBarUi()
        super.onCreate(savedInstanceState)
        setContent {
            TetrisPage(
                modifier = Modifier
                    .windowInsetsPadding(insets = WindowInsets.statusBarsIgnoringVisibility)
                    .navigationBarsPadding(),
                tetrisLogic = tetrisLogic
            )
        }
    }

    override fun onResume() {
        super.onResume()
        tetrisLogic.dispatch(action = Action.Resume)
    }

    override fun onPause() {
        super.onPause()
        tetrisLogic.dispatch(action = Action.Background)
    }

    override fun onDestroy() {
        super.onDestroy()
        tetrisLogic.release()
    }

    private fun setSystemBarUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.statusBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        }
    }

}