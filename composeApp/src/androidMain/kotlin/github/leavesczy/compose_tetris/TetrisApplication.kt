package github.leavesczy.compose_tetris

import android.app.Application
import github.leavesczy.compose_tetris.android.ContextProvider

/**
 * @Author: leavesCZY
 * @Date: 2021/6/3 22:15
 * @Desc:
 */
class TetrisApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextProvider.init(context = this)
    }

}