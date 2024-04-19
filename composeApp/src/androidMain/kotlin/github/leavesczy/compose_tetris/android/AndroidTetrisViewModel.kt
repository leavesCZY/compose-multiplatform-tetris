package github.leavesczy.compose_tetris.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import github.leavesczy.compose_tetris.common.logic.Action
import github.leavesczy.compose_tetris.common.logic.TetrisLogic

/**
 * @Author: leavesCZY
 * @Date: 2021/5/31 18:20
 * @Desc:
 */
class AndroidTetrisViewModel : ViewModel() {

    val tetrisLogic = TetrisLogic(
        coroutineScope = viewModelScope,
        soundPlayer = AndroidSoundPlayer()
    )

    fun dispatch(action: Action) {
        tetrisLogic.dispatch(action = action)
    }

    override fun onCleared() {
        super.onCleared()
        tetrisLogic.release()
    }

}