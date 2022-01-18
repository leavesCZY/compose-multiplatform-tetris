package github.leavesczy.compose_tetris.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
        Scaffold {
            TetrisBody(tetrisScreen = {
                TetrisScreen(tetrisState = tetrisState)
            }, tetrisButton = {
                TetrisButton(tetrisState = tetrisState, playListener = playListener)
            })
        }
    }
}