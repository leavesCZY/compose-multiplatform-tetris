package github.leavesczy.compose_tetris

import github.leavesczy.compose_tetris.base.logic.GameStatus
import github.leavesczy.compose_tetris.base.logic.Location

/**
 * @Author: leavesCZY
 * @Date: 2026/4/16 20:03
 * @Desc:
 */
actual fun getScreenSize(): Location {
    return Location(x = 33, y = 20)
}

actual fun getFontSize(gameStatus: GameStatus): Int {
    return 160
}