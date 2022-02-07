package github.leavesczy.compose_tetris.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import github.leavesczy.compose_tetris.common.logic.ITetrisLogic
import github.leavesczy.compose_tetris.common.logic.TetrisLogicImpl

/**
 * @Author: leavesCZY
 * @Date: 2021/5/31 18:20
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class AndroidTetrisViewModel(delegate: TetrisLogicImpl) : ViewModel(), ITetrisLogic by delegate {

    init {
        provideScope(coroutineScope = viewModelScope)
    }

}