package github.leavesczy.compose_tetris.platform

import github.leavesczy.compose_tetris.common.logic.GameStatus
import github.leavesczy.compose_tetris.common.logic.Location

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:09
 * @Desc:
 */
actual fun getScreenSize(): Location {
    return Location(x = 38, y = 20)
}

actual fun getFontSize(gameStatus: GameStatus): Int {
    return 160
}