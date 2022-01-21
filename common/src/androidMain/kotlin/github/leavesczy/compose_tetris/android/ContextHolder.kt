package github.leavesczy.compose_tetris.android

import android.app.Application

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 14:57
 * @Desc:
 */
object ContextHolder {

    lateinit var context: Application
        private set

    fun init(context: Application) {
        ContextHolder.context = context
    }

}