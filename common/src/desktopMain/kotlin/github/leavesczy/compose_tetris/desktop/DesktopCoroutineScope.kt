package github.leavesczy.compose_tetris.desktop

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
object DesktopCoroutineScope : CoroutineScope {

    override val coroutineContext = SupervisorJob() + Dispatchers.Default

}