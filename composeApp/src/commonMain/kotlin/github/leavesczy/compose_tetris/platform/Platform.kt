package github.leavesczy.compose_tetris.platform

import github.leavesczy.compose_tetris.platform.logic.GameStatus
import github.leavesczy.compose_tetris.platform.logic.Location

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:03
 * @Desc:
 */
expect fun getScreenSize(): Location

expect fun getFontSize(gameStatus: GameStatus): Int