package github.leavesczy.compose_tetris.ui

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import github.leavesczy.compose_tetris.logic.PlayListener
import github.leavesczy.compose_tetris.logic.TetrisState
import github.leavesczy.compose_tetris.ui.theme.ComposeTetrisTheme

/**
 * @Author: leavesC
 * @Date: 2022/1/17 23:12
 * @Desc:
 * @Github：https://github.com/leavesC
 */
@Composable
fun MainScreen(tetrisState: TetrisState, playListener: PlayListener) {
    ComposeTetrisTheme {
        ProvideWindowInsets {
            Scaffold(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .systemBarsPadding()
            ) {
                TetrisBody(tetrisScreen = {
                    TetrisScreen(tetrisState = tetrisState)
                }, tetrisButton = {
                    TetrisButton(tetrisState = tetrisState, playListener = playListener)
                })
            }
        }
    }
}