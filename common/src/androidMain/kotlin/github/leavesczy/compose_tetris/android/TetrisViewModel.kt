package github.leavesczy.compose_tetris.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import github.leavesczy.compose_tetris.common.logic.Action
import github.leavesczy.compose_tetris.common.logic.ITetrisLogic
import github.leavesczy.compose_tetris.common.logic.TetrisLogic
import github.leavesczy.compose_tetris.common.logic.TetrisState
import kotlinx.coroutines.flow.StateFlow

/**
 * @Author: leavesCZY
 * @Date: 2021/5/31 18:20
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class TetrisViewModel : ViewModel(), ITetrisLogic {

    private val tetrisLogic =
        TetrisLogic(coroutineScope = viewModelScope)

    override val tetrisStateFlow: StateFlow<TetrisState>
        get() = tetrisLogic.tetrisStateFlow

    override fun dispatch(action: Action) {
        tetrisLogic.dispatch(action = action)
    }

}