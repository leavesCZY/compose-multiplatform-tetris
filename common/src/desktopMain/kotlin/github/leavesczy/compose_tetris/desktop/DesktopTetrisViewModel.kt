package github.leavesczy.compose_tetris.desktop

import github.leavesczy.compose_tetris.common.logic.ITetrisLogic
import github.leavesczy.compose_tetris.common.logic.TetrisLogicImpl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:09
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
@OptIn(DelicateCoroutinesApi::class)
class DesktopTetrisViewModel(delegate: TetrisLogicImpl) : ITetrisLogic by delegate {

    init {
        provideScope(GlobalScope)
    }

}