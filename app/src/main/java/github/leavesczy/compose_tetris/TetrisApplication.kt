package github.leavesczy.compose_tetris

import android.app.Application
import github.leavesczy.compose_tetris.utils.SoundUtil

/**
 * @Author: leavesCZY
 * @Date: 2021/6/3 22:15
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class TetrisApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SoundUtil.init(this)
    }

}