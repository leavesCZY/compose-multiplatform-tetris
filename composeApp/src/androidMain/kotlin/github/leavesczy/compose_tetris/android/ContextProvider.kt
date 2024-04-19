package github.leavesczy.compose_tetris.android

import android.app.Application

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 14:57
 * @Desc:
 */
object ContextProvider {

    lateinit var context: Application
        private set

    fun init(context: Application) {
        ContextProvider.context = context
    }

}