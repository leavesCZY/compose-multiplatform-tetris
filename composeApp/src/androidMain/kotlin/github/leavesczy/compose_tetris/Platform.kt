package github.leavesczy.compose_tetris

import github.leavesczy.compose_tetris.logic.GameStatus
import github.leavesczy.compose_tetris.logic.Location

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:03
 * @Desc:
 */
actual fun getScreenSize(): Location {
    return Location(x = 12, y = 25)
}

actual fun getFontSize(gameStatus: GameStatus): Int {
    return when (gameStatus) {
        GameStatus.Welcome,
        GameStatus.Running,
        GameStatus.Paused,
        GameStatus.LineClearing,
        GameStatus.ScreenClearing -> {
            60
        }

        GameStatus.GameOver -> {
            45
        }
    }
}