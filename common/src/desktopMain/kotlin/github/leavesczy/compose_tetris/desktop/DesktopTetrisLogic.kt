package github.leavesczy.compose_tetris.desktop

import github.leavesczy.compose_tetris.common.logic.Action
import github.leavesczy.compose_tetris.common.logic.ITetrisLogic
import github.leavesczy.compose_tetris.common.logic.TetrisLogic
import github.leavesczy.compose_tetris.common.logic.TetrisState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.StateFlow

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:09
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
class DesktopTetrisLogic : ITetrisLogic {

    private val tetrisLogic = TetrisLogic(GlobalScope)

    override val tetrisStateFlow: StateFlow<TetrisState>
        get() = tetrisLogic.tetrisStateFlow

    override fun dispatch(action: Action) {
        tetrisLogic.dispatch(action = action)
    }

}