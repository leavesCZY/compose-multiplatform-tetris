package github.leavesczy.compose_tetris.desktop

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:09
 * @Desc:
 */
object DesktopCoroutineScope : CoroutineScope {

    override val coroutineContext = SupervisorJob() + Dispatchers.Default

}