package github.leavesczy.compose_tetris.platform

import github.leavesczy.compose_tetris.platform.logic.GameStatus
import github.leavesczy.compose_tetris.platform.logic.Location

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:09
 * @Desc:
 */
actual fun getScreenSize(): Location {
    return Location(x = 33, y = 20)
}

actual fun getFontSize(gameStatus: GameStatus): Int {
    return 160
}