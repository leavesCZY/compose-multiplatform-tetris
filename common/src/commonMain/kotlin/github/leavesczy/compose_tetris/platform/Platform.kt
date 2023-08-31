package github.leavesczy.compose_tetris.platform

import github.leavesczy.compose_tetris.common.logic.GameStatus
import github.leavesczy.compose_tetris.common.logic.Location

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:03
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
expect fun getScreenSize(): Location

expect fun getFontSize(gameStatus: GameStatus): Int