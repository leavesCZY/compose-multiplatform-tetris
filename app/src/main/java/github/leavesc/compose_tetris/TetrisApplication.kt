package github.leavesc.compose_tetris

import android.app.Application
import github.leavesc.compose_tetris.utils.SoundUtil

/**
 * @Author: leavesC
 * @Date: 2021/6/3 22:15
 * @Desc:
 * @Github：https://github.com/leavesC
 */
class TetrisApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SoundUtil.init(this)
    }

}